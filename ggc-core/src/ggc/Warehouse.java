package ggc;

import java.io.Serializable;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import ggc.exceptions.BadEntryException;
import ggc.exceptions.PartnerAlreadyExistsException;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.exceptions.ProductDoesNotExistException;
import ggc.exceptions.InsuficientStockException;
import ggc.exceptions.NoRecipeException;
import ggc.exceptions.InvalidTransactionIDException;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110282338L;
  
  private Integer _date = 0;

  private LinkedList<Partner> _partners = new LinkedList<>();
  private TreeMap<Product, TreeSet<Batch>> _stock = new TreeMap<>();
  private TransactionManager _transactionManager = new TransactionManager();
  private double _availableBalance = 0;
  private double _accountingBalance = 0;
  private int _transactionID = 0;





  public double MediaTransacoes(String partnerID) {
    double sum = 0;
    int no = 0;
    for (Transaction t : _transactionManager.getTransactions()) {
      if (t.getPartner().getName().equals(partnerID)) {
        sum += t.getPrice();
        no++;
      }
    }
    return sum / no;
  }


  public double getProductMonetaryValue(Product p) {
    double monetaryValue = 0;
    TreeSet<Batch> _batches = _stock.get(p);
    if (_batches != null) {
      for (Batch b : _batches) {
        monetaryValue += (b.getPriceUnit() * b.getRemainingUnits());
      }
      return monetaryValue;
    }
    return -1;
  }


  public Product getProductWithLeastMonetaryValue() {
    double leastMonetaryValue = 0;
    boolean firstProduct = true;
    Product product = null;
    for (Product p : _stock.keySet()) {
      if (firstProduct) {
        leastMonetaryValue = getProductMonetaryValue(p);
        product = p;
        firstProduct = false;
      }
      else {
        double currentMonetaryValue = getProductMonetaryValue(p);
        if (currentMonetaryValue < leastMonetaryValue) {
          leastMonetaryValue = currentMonetaryValue;
          product = p;
        }
      }
    }
    return product;
  }




  public double getPartnerAcquisitionValue(Partner p) {
    return p.getSalesValue();
  }


  public Partner getPartnerWithLeastAcquisitionValue() {
    double leastAcquisitionValue = 0;
    boolean firstPartner = true;
    Partner partner = null;
    for (Partner p : _partners) {
      if (firstPartner) {
        leastAcquisitionValue = getPartnerAcquisitionValue(p);
        partner = p;
        firstPartner = false;
      }
      else {
        double currentAcquisitionValue = getPartnerAcquisitionValue(p);
        if (currentAcquisitionValue < leastAcquisitionValue) {
          leastAcquisitionValue = currentAcquisitionValue;
          partner = p;
        }
      }
    }
    return partner;
  }



  /**
   * @return real balance
   */
  public double getAvailableBalance() {
    return _availableBalance;
  }

  /**
   * @param increment will increment available balance (positive or negative)
   */
  public void updateAvailableBalance(double increment) {
    _availableBalance += increment;
  }

  /**
   * @return accounting Balance
   */
  public double getAccountingBalance() {
    return _accountingBalance;
  }

  /**
   * @param increment will increment accounting balance (positive or negative)
   */
  public void updateAccountingBalance(double increment) {
    _accountingBalance += increment;
  }

  /**
   * @return the new transaction ID
   */
  public int getTransactionID() {
    return _transactionID;
  }

  /**
   * Increments transaction ID counter
   */
  public void incrementTransactionID() {
    _transactionID++;
  }

  /**
   *
   * @param id product ID
   * @return returns the product of the _stock whose ID matches the given argument, null otherwise
   */
  public Product getProduct(String id) {
    Set<Product> products = _stock.keySet();
    for (Product p : products) {
      if (p.getName().toUpperCase().equals(id.toUpperCase())) {
        return p;
      }
    }
    return null;
  }


  /**
   * @param id product ID
   * @param price product cost (by unit)
   */
  public void addProduct(String id, double price) {
    Product p = new Product(id, price);
    _stock.put(p, new TreeSet<>());
  }


  /**
   * @param id product ID
   * @param price product cost (by unit)
   * @param _inProducts derived product's recipe components
   * @param alpha multiplication factor associated with production cost
   */
  public void addDerivedProduct(String id, double price, LinkedHashMap<String, Integer> _inProducts, double alpha) {
    Product p = new DerivedProduct(id, price, new Recipe(_inProducts, alpha));
    _stock.put(p, new TreeSet<>());
  }


  /**
   * @param partnerID partner ID
   * @param productID product ID
   * @param price price
   * @param amount amount
   * @throws PartnerDoesNotExistException if partner ID doesn't match any partner
   * @throws ProductDoesNotExistException if product ID doesn't match any product
   */
  public void registerAcquisition(String partnerID, String productID, double price, int amount)
    throws PartnerDoesNotExistException, ProductDoesNotExistException{

    Partner partner = findPartner(partnerID);
    if (partner == null) throw new PartnerDoesNotExistException(partnerID);
    Product product = getProduct(productID);
    if (product == null) throw new ProductDoesNotExistException(productID);

    // add new batch of the product to the tree set
    addBatch(product, new Batch(product, partner.getId(), amount, price));
    Acquisition acquisition = new Acquisition(getDate(), partner, product, amount, price * amount);
    acquisition.setId(getTransactionID());
    incrementTransactionID();
    _transactionManager.addAcquisitionTransaction(acquisition);
    updateAccountingBalance(-price * amount);
    updateAvailableBalance(-price * amount);
    partner.updateAcquistionsValue(price * amount);

  }


  /**
   * @param id Transaction id
   * @return transaction toString() according to its subclass
   * @throws InvalidTransactionIDException if transaction ID doesn't match any transaction
   */
  public String getTransactionString(int id) throws InvalidTransactionIDException {
    return _transactionManager.getTransaction(id).accept(new ToStringDisplay());
  }

  /**
   * @param productID product ID
   * @return Product object with the product ID, null if there is no Product with the given product ID
   */
  public Product findProduct(String productID) {
    for (Product p : _stock.keySet()) {
      if (p.getName().toUpperCase().equals(productID.toUpperCase())) {
        return p;
      }
    }
    return null;
  }

  /**
   * @param productKey Product object with a name only
   * @return Recipe associated with the Product
   * @throws NoRecipeException if the product is simple, i.e., doesn't have a recipe
   */
  public Recipe getRecipeFromProductKey(Product productKey) throws NoRecipeException {
    for (Product p : _stock.keySet()) {
      if (p.equals(productKey)) {
        return p.getRecipe();
      }
    }
    return null;
  }

  /**
   * @param batches TreeSet with all the batches of the given product
   * @param product Product object
   * @param amount amount that will be deducted
   * @return sell price of the deducted items
   */
  public float getPriceAndUpdateStock(TreeSet<Batch> batches, Product product, int amount) {
    float price = 0;

    product.updateTotalStock(-amount);
    for (Batch b : batches) {
      if (amount > 0) {
        int batchRemainingUnits = b.getRemainingUnits();
        if (amount >= batchRemainingUnits) {
          price += batchRemainingUnits * b.getPriceUnit();
          _stock.get(product).remove(b);
          amount -= batchRemainingUnits;
        } else {
          price += amount * b.getPriceUnit();
          b.updateBatch(-amount);
          amount = 0;
        }
      }
    }
    return price;
  }

  /**
   * If it's possible to manufacture the derived product, the components will have neededStock lower or equal than
   * their actual stock.
   *
   * @param components LinkedHashMap in which the keys are products and the values are ArrayList of (3) Integers.
   *                   0:  NeededStock; 1:  NeededStock * pricePerUnit; 2:  AvailableStock
   * @param derivedProduct derived product composed by the given components
   * @throws InsuficientStockException if there is no stock to create the derived product
   * @throws NoRecipeException if there is no recipe for the given product, i.e., it's actually a simple product
   */
  public void canManufacture(LinkedHashMap<Product, ArrayList<Integer>> components, Product derivedProduct)
    throws InsuficientStockException, NoRecipeException {

    int productStock = 0;
    for (Product p : components.keySet()) {
      int neededStock = components.get(p).get(0);
      productStock = findProduct(p.getName()).getTotalStock();
      components.get(p).set(0, neededStock - derivedProduct.getTotalStock() * derivedProduct.getRecipe().getProportion(p));
      components.get(p).set(2, productStock);
      if (neededStock > productStock) {
        throw new InsuficientStockException(p.getName(), components.get(p).get(0), components.get(p).get(2));
      }
    }
  }

  /**
   * @param partnerID partner ID
   * @param deadline deadline to pay without penalities
   * @param productID product ID
   * @param amount amount
   * @throws InsuficientStockException if there is no stock to perform the sale
   * @throws PartnerDoesNotExistException if partner ID doesn't match any partner
   * @throws ProductDoesNotExistException if product ID doesn't match any product
   */
  public void registerSale(String partnerID, int deadline, String productID, int amount)
    throws InsuficientStockException, PartnerDoesNotExistException, ProductDoesNotExistException {

    Partner partner = findPartner(partnerID);
    if (partner == null) throw new PartnerDoesNotExistException(partnerID);
    Product product = findProduct(productID);
    if (product == null) throw new ProductDoesNotExistException(productID);
    Sale sale = new Sale(deadline, amount);
    int availableStock = product.getTotalStock();
    float price = 0;
    TreeSet<Batch> batches = new TreeSet<>(new PriceComparator());
    batches.addAll(_stock.get(product));

    if (amount <= availableStock) { /** No manufacture needed */
      price = getPriceAndUpdateStock(batches, product, amount);
    } else {
      try {
        LinkedHashMap<Product, ArrayList<Integer>> components = 
          product.getRecipe().getComponentsProportional(amount);
        float productionCost = 0;
        canManufacture(components, product); /** throws InsuficientStockException */
        if (availableStock != 0) { /** In case there is some derived product already manufactured */
          price = getPriceAndUpdateStock(batches, product, availableStock);
          amount -= availableStock;
        }
        for (Product p : components.keySet()) { /** Updating component Stock and "Manufacturing" the derived product */
          batches.clear();
          batches.addAll(_stock.get(p));
          productionCost += getPriceAndUpdateStock(batches, findProduct(p.getName()), components.get(p).get(0));
        }
        productionCost *= (1 + product.getRecipe().getProductionCost());
        price += productionCost;
        updateBatchMaxMinPrices(product, productionCost / amount);
      } catch (NoRecipeException e) {
        throw new InsuficientStockException(productID, amount, product.getTotalStock());
      }
    }
    /** Set the transaction attributes */
    sale.setPartner(partner);
    sale.setProduct(product);
    sale.setPrice(price);
    sale.setId(getTransactionID());
    sale.updatePriceToPay(getDate());
    incrementTransactionID();
    _transactionManager.addSaleTransaction(sale);
    updateAccountingBalance(price);
    partner.updateSalesValue(price);
  }

  /**
   * @param partnerID partner ID
   * @param productID product ID
   * @param amount amount
   * @throws InsuficientStockException if there is no stock to perform the breakdown
   * @throws PartnerDoesNotExistException if partner ID doesn't match any partner
   * @throws ProductDoesNotExistException if product ID doesn't match any product
   */
  public void registerBreakdown(String partnerID, String productID, int amount)
    throws InsuficientStockException, PartnerDoesNotExistException, ProductDoesNotExistException {

    Partner partner = findPartner(partnerID);
    if (partner == null) throw new PartnerDoesNotExistException(partnerID);
    Product product = findProduct(productID);
    if (product == null) throw new ProductDoesNotExistException(productID);
    float price = 0;
    try {
      if (product.getTotalStock() >= amount) {
        LinkedHashMap<Product, ArrayList<Integer>> components = product.getRecipe().getComponentsProportional(amount);
        TreeSet<Batch> batches = new TreeSet<>(new PriceComparator());
        batches.addAll(_stock.get(product));
        float derivedProductPrice = getPriceAndUpdateStock(batches, product, amount);
        float componentSumPrice = 0;

        /** Adding new batches with the components from the breakdown */
        for (Map.Entry<Product, ArrayList<Integer>> e : components.entrySet()) {
          e.getValue().set(1, (int) Math.round(findProduct(e.getKey().getName()).getLowestPrice()) * e.getValue().get(0));
          componentSumPrice += e.getValue().get(1);
          addBatch(e.getKey(), 
            new Batch(
              e.getKey(), 
              partnerID, 
              e.getValue().get(0), 
              e.getKey().getLowestPrice()));
        }
        price = derivedProductPrice - componentSumPrice;
        /** Set transaction attributes */
        Breakdown breakdown = new Breakdown(getDate(), partner, product, amount, price);
        breakdown.setPriceToPay(Math.abs(price));
        breakdown.setComponents(components);
        breakdown.setId(getTransactionID());
        incrementTransactionID();
        _transactionManager.addBreakdownTransaction(breakdown);
        payTransaction(breakdown, price);
        partner.breakdownRegister(breakdown, getDate());
        partner.updateSalesValue(price);
      } else {
        throw new InsuficientStockException(productID, amount, product.getTotalStock());
      }
    } catch (NoRecipeException e) {
      /** Do nothing */
      /** Could be thrown an Exception to communicate that the given product was simple */
    }
  }

  /**
   * @param transaction transaction
   * @param price transaction (sale or breakdown) price
   */
  public void payTransaction(Transaction transaction, double price) {
    transaction.paymentComplete(getDate());
    updateAccountingBalance(price);
    updateAvailableBalance(price);
    transaction.getPartner().updatePaidSalesValue(price);
  }

  /**
   * @param transactionID transaction ID
   * @throws InvalidTransactionIDException if transaction ID doesn't match any transaction
   */
  public void registerPayment(int transactionID) throws InvalidTransactionIDException {
    Transaction transaction = 
      _transactionManager.pay(transactionID, getDate());
    
    if (transaction != null) {
      double price = transaction.getPriceToPay();
      payTransaction(transaction, price);
      transaction.getPartner().paymentRegister(transaction, getDate());
    }
  }

  /**
   * @param partnerID partner ID
   * @return LinkedList with all Partner's payments
   * @throws PartnerDoesNotExistException if partner ID doesn't match any partner
   */
  public LinkedList<Transaction> getPaymentsByPartner(String partnerID) throws PartnerDoesNotExistException {
    Partner partner = findPartner(partnerID);
    if (partner != null) {
      return _transactionManager.getPaymentsByPartner(partner);
    } else {
      throw new PartnerDoesNotExistException(partnerID);
    }
  }

  /**
   *
   * @param p product
   * @param price price per unit of the product's batch to be added
   */
  public void updateBatchMaxMinPrices(Product p, double price) {
    if (price > p.getMaxPrice()) {
      p.setMaxPrice(price);
    }
    if (price < p.getLowestPrice()) {
      p.setLowestPrice(price);
    }
  }

  /**
   *
   * @param p product
   * @param b first batch to be added to sector
   */
  public void addBatch(Product p, Batch b) {
    for (Product key : _stock.keySet()) { 
      if (key.equals(p)) {
        updateBatchMaxMinPrices(key, b.getPriceUnit());
        key.updateTotalStock(b.getRemainingUnits());
        b.setProduct(key);
        _stock.get(key).add(b);
        return;
      }
    }
    TreeSet<Batch> batches = new TreeSet<>();
    p.setMaxPrice(b.getPriceUnit());
    p.setLowestPrice(b.getPriceUnit());
    b.setProduct(p);
    batches.add(b);
    _stock.put(p, batches);
  }

  /**
   *
   * @param line array of strings with batch information
   */
  public void registerBatch(String[] line) {
    Batch b = new Batch(null, line[2], Integer.parseInt(line[4]), Integer.parseInt(line[3]));
    Product key;

    /* Simple Product */
    if (line.length == 5) {
      key = new Product(line[1], 
        Float.parseFloat(line[3]),
        Integer.parseInt(line[4]));
    }
    /* Derived Product */
    else {
      key = new DerivedProduct(line[1], 
          Float.parseFloat(line[3]), 
          Integer.parseInt(line[4]), 
          new Recipe(createRecipeList(line[6]), 
          Double.parseDouble(line[5])));
    }
    turnProductNotifiable(line[1]);
    addBatch(key, b);
  }

  /**
   *
   * @param recipeStr string containing products and their quantities info
   * @return map with key as product ID and value as its quantity
   */
  public LinkedHashMap<String, Integer> createRecipeList(String recipeStr) {
    String[] arrComponents = recipeStr.split("#");
    LinkedHashMap<String, Integer> productList = new LinkedHashMap<>();
    for (String c : arrComponents) {
      String[] arrComponent = c.split(":");
      productList.put(arrComponent[0], Integer.parseInt(arrComponent[1]));
    }
    return productList;
  }

  /** Partner related methods */
  /**
   *
   * @param id partner ID
   * @return pointer to partner if exists, null otherwise
   */
  public Partner findPartner(String id) {
    for (Partner p : _partners) {
      if (p.getId().toUpperCase().equals(id.toUpperCase())) {
        return p;
      }
    }
    return null;
  }

  /**
   *
   * @param partnerID partner ID
   * @return partner whose ID is given as argument
   * @throws PartnerDoesNotExistException if there is no partner registered with given ID
   */
  public Partner getPartner(String partnerID) throws PartnerDoesNotExistException {
    for (Partner p : _partners) {
      if (p.getId().equalsIgnoreCase(partnerID)) {
        return p;
      }
    }
    throw new PartnerDoesNotExistException(partnerID);
  }

  /**
   *
   * @return list of warehouse partners
   */
  public LinkedList<Partner> getPartners() {
    Collections.sort(_partners);
    return _partners;
  }

  /**
   *
   * @param id partner ID
   * @param name partner name
   * @param address partner address
   * @throws PartnerAlreadyExistsException if exists a partner registered with the given ID
   */
  public void registerPartner(String id, String name, String address) throws PartnerAlreadyExistsException {
    if (findPartner(id) != null){
      throw new PartnerAlreadyExistsException(id);
    } else {
      _partners.add(new Partner(id, name, address));
    }
  }


  // Notifies product if condition is met (for existing products only)
  /**
   *
   * @param productID product's ID
   * @param price new product's batch price
   */
  public void verifyNotificationConditions(String productID, Double price) {
    Product p = findProduct(productID);
    if (p != null) {
      if (p.getTotalStock() == 0) { notificatePartners(new New(p)); }
      else if (p.getLowestPrice() > price) { notificatePartners(new Bargain(p)); }
    }
  }


  // Product becomes notifiable to all partners (for new products only)
  /**
   *
   * @param productID product's ID
   */
  public void turnProductNotifiable(String productID) {
    for (Partner p : _partners) {
      p.becomeNotifiable(productID);
    }
  }


  /**
   * Reverts state of the notification toggle
   * 
   * @param partnerID partner's ID
   * @param productID product's ID
   */
  public void toggleProductNotifications(String partnerID, String productID) throws ProductDoesNotExistException, PartnerDoesNotExistException {
    if (findProduct(productID) != null) {
      if (findPartner(partnerID) != null) {
        for (Partner p : _partners) {
          if (p.getId().equals(partnerID) && p.hasProductToggle(productID)) {
            p.setToggle(productID);
          }
        }
      } else {
        throw new PartnerDoesNotExistException(partnerID); }
    } else {
      throw new ProductDoesNotExistException(productID); }
  }


  /**
   *
   * @param partnerID partner's ID
   * @return list of the partner's acquisitions
   * @throws PartnerDoesNotExistException if does not exist a partner with the given id
   */
  public LinkedList<Acquisition> getPartnerAcquisitions(String partnerID) throws PartnerDoesNotExistException {
    Partner partner = findPartner(partnerID);
    if (partner != null) {
      return _transactionManager.getAcquisitionTransactionsByPartner(findPartner(partnerID));
    } else {
      throw new PartnerDoesNotExistException(partnerID);
    }
  }


  /**
   *
   * @param partnerID partner's ID
   * @return list of the partner's sales and breakdowns
   * @throws PartnerDoesNotExistException if does not exist a partner with the given id
   */
  public ArrayList<Transaction> getPartnerSalesAndBreakdowns(String partnerID) throws PartnerDoesNotExistException {
    Partner partner = findPartner(partnerID);
    if (partner != null) {
      return _transactionManager.getSaleAndBreakdownMergedTransactions(partner);
    } else {
      throw new PartnerDoesNotExistException(partnerID);
    }
  }

  /**
   * Adds product notification to partners with toggle on
   *
   * @param notification Notification object
   */
  public void notificatePartners(Notification notification) {
    String productID = notification.getProductID();
    for (Partner p : _partners) {
      if (p.hasProductToggle(productID) && p.getToggleState(productID)) {
        p.addNotification(notification);
      }
    }
  }

  
  // Date related methods
  /**
   *
   * @return current date
   */
  public Integer getDate() {
    return _date;
  }
  public void incrementDate(int increment) {
    _date += increment;
    _transactionManager.updateAllPrices(getDate());
  }


  // Product related methods
  /**
   *
   * @return list of warehouse products
   */
  public ArrayList<Product> getProducts() {
    ArrayList<Product> products = new ArrayList<>(_stock.size());
    products.addAll(_stock.keySet());
    return products;
  }


  /**
   *
   * @param priceLimit price threshold
   * @return list of the warehouse batches under the price threshold
   */
  public LinkedList<Batch> getBatchesUnderPrice(double priceLimit) {
    LinkedList<Batch> batches = new LinkedList<>();
    for (Product p : _stock.keySet()) {
      if (p.getLowestPrice() < priceLimit) {
        for (Batch b : _stock.get(p)) {
          if (b.getPriceUnit() < priceLimit) {
            batches.add(b);
          }
        }
      }
    }
    return batches;
  }

  // Batch related methods
  /**
   *
   * @return list of warehouse batches
   */
  public LinkedList<Batch> getBatches() {
    LinkedList<Batch> batches = new LinkedList<>();

    for (Product key : _stock.keySet()) {
      batches.addAll(_stock.get(key));
    }
    return batches;
  }


  /**
   *
   * @param key Product object
   * @return set of batches of the given product
   * @throws ProductDoesNotExistException if given product does not exist in the warehouse
   */
  public TreeSet<Batch> getProductBatches(Product key) throws ProductDoesNotExistException {
    if (_stock.containsKey(key)){
      return _stock.get(key);
    } else {
      throw new ProductDoesNotExistException(key.getName());
    }
  }


  /**
   *
   * @param partnerID partner's ID
   * @return list of the partner's batches
   * @throws PartnerDoesNotExistException if partner with given ID does not exist
   */
  public LinkedList<Batch> getPartnerBatches(String partnerID) throws PartnerDoesNotExistException {
    if (findPartner(partnerID) != null) {
      LinkedList<Batch> batches = new LinkedList<>();
      for (Batch b : getBatches()) {
        if (b.getPartnerID().toUpperCase().equals(partnerID.toUpperCase())) {
          batches.add(b);
        } 
      }
      return batches;
    } else {
      throw new PartnerDoesNotExistException(partnerID);
    }
  }

  /**
   *
   * @param txtfile filename to be loaded
   * @throws IOException for file reading failures
   * @throws BadEntryException for unknown import file entries
   * @throws PartnerAlreadyExistsException for duplicate partner id cases
   */
  void importFile(String txtfile) throws IOException, BadEntryException, PartnerAlreadyExistsException {
    try {
      Scanner reader = new Scanner(new File(txtfile));
      while (reader.hasNextLine()) {
        String[] arrLine = reader.nextLine().split("\\|");
        if (arrLine[0].equals("PARTNER")) {
          registerPartner(arrLine[1], arrLine[2], arrLine[3]);
        }
        else {
          registerBatch(arrLine);
        }
      }
      reader.close();
    } catch (FileNotFoundException e) {
      throw new BadEntryException(txtfile);
    }
  }
}