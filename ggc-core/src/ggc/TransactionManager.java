package ggc;

import java.util.*;
import java.io.Serializable;
import ggc.exceptions.InvalidTransactionIDException;

public class TransactionManager implements Serializable {
  private LinkedList<Transaction> _transactions = new LinkedList<>();
  private HashMap<Partner, LinkedList<Acquisition>> _acquisitionTransactions = new HashMap<>();
  private HashMap<Partner, LinkedList<Sale>> _saleTransactions = new HashMap<>();
  private HashMap<Partner, LinkedList<Breakdown>> _breakdownTransactions = new HashMap<>();




  public void addTransaction(Transaction transaction) {
    _transactions.add(transaction);
  }

  public LinkedList<Transaction> getTransactions() { return _transactions; }


  public void addAcquisitionTransaction(Acquisition acquisition) {
    if (_acquisitionTransactions.containsKey(acquisition.getPartner())) {
      _acquisitionTransactions.get(acquisition.getPartner()).add(acquisition);
    } else {
      LinkedList<Acquisition> partnerAcquisitions = new LinkedList<>();
      partnerAcquisitions.add(acquisition);
      _acquisitionTransactions.put(acquisition.getPartner(), partnerAcquisitions);
    }
    addTransaction(acquisition);
  }

  public void addSaleTransaction(Sale sale) {
    if (_saleTransactions.containsKey(sale.getPartner())) {
      _saleTransactions.get(sale.getPartner()).add(sale);
    } else {
      LinkedList<Sale> partnerSales = new LinkedList<>();
      partnerSales.add(sale);
      _saleTransactions.put(sale.getPartner(), partnerSales);
    }
    addTransaction(sale);
  }

  public void addBreakdownTransaction(Breakdown breakdown) {
    if (_breakdownTransactions.containsKey(breakdown.getPartner())) {
      _breakdownTransactions.get(breakdown.getPartner()).add(breakdown);
    } else {
      LinkedList<Breakdown> partnerBreakdowns = new LinkedList<>();
      partnerBreakdowns.add(breakdown);
      _breakdownTransactions.put(breakdown.getPartner(), partnerBreakdowns);
    }
    addTransaction(breakdown);
  }

  public Transaction getTransaction(int id) throws InvalidTransactionIDException {
    if (id >= 0 && id < _transactions.size()) {
      return _transactions.get(id);
    } else {
      throw new InvalidTransactionIDException(id);
    }
  }

  public LinkedList<Acquisition> getAcquisitionTransactionsByPartner(Partner partnerKey) {
    return _acquisitionTransactions.get(partnerKey);
  }

  public LinkedList<Sale> getSaleTransactionsByPartner(Partner partnerKey) {
    return _saleTransactions.get(partnerKey);
  }

  public LinkedList<Transaction> getPaymentsByPartner(Partner partnerKey) {
    LinkedList<Transaction> payments = new LinkedList<>();
    for (Transaction t : getSaleAndBreakdownMergedTransactions(partnerKey)) {
      if (t.isPayed()) {
        payments.add(t);
      }
    }
    return payments;
  }

  public LinkedList<Breakdown> getBreakdownTransactionsByPartner(Partner partnerKey) {
    return _breakdownTransactions.get(partnerKey);
  }

  public ArrayList<Transaction> getSaleAndBreakdownMergedTransactions(Partner partnerKey) {
    int i = 0;
    int indexS = 0;
    int indexB = 0;
    int sizeS = 0;
    int sizeB = 0;
    if (_saleTransactions.get(partnerKey) != null) {
      sizeS = _saleTransactions.get(partnerKey).size();
    }
    if (_breakdownTransactions.get(partnerKey) != null) {
      sizeB = _breakdownTransactions.get(partnerKey).size();
    }
    ArrayList<Transaction> saleAndBreakdownTransactions = new ArrayList<>(sizeS + sizeB);

    while (indexS < sizeS && indexB < sizeB) {
      if (_saleTransactions.get(partnerKey).get(indexS).getId() < _breakdownTransactions.get(partnerKey).get(indexB).getId()) {
        saleAndBreakdownTransactions.add(i, _saleTransactions.get(partnerKey).get(indexS));
        indexS++;
      } else {
        saleAndBreakdownTransactions.add(i, _breakdownTransactions.get(partnerKey).get(indexB));
        indexB++;
      }
      i++;
    }
    if (indexS == sizeS) {
      for (; i < sizeS + sizeB; i++) {
        saleAndBreakdownTransactions.add(i, _breakdownTransactions.get(partnerKey).get(indexB));
        indexB++;
      }
    } else {
      for (; i < sizeS + sizeB; i++) {
        saleAndBreakdownTransactions.add(i, _saleTransactions.get(partnerKey).get(indexS));
        indexS++;
      }
    }
    return saleAndBreakdownTransactions;
  }

  public Transaction pay(int transactionID, int date) throws InvalidTransactionIDException {
    Transaction transaction = getTransaction(transactionID);
    if (getSaleTransactionsByPartner(transaction.getPartner()).contains(transaction)){
      transaction.updatePriceToPay(date);
      return transaction;
    }
    return null;
  }

  public void updateAllPrices(int date) {
    for (LinkedList<Sale> sales : _saleTransactions.values()) {
      for (Sale s : sales) {
        s.updatePriceToPay(date);
      }
    }
  }
}