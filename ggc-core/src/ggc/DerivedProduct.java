package ggc;

import ggc.exceptions.NoRecipeException;

public class DerivedProduct extends Product {

  private Recipe _recipe;

  public DerivedProduct(String name, double maxPrice, Recipe recipe) {
    super(name, maxPrice);
    setInterval(3);
    _recipe = recipe;
  }

  public DerivedProduct(String name, double maxPrice, int totalStock, Recipe recipe) {
    super(name, maxPrice, totalStock);
    setInterval(3);
    _recipe = recipe;
  }

  public void setRecipe(Recipe recipe) {
    _recipe = recipe;
  }

  @Override
  public Recipe getRecipe() throws NoRecipeException {
    if (_recipe != null) {
      return _recipe;
    } else {
      throw new NoRecipeException();
    }
  }

  @Override
  public String toString() {
    return super.toString() + "|" + _recipe.getProductionCost() + "|" + _recipe.toString();
  }
}