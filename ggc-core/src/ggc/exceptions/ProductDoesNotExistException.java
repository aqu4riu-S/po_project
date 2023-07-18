package ggc.exceptions;

public class ProductDoesNotExistException extends Exception{
  private String _unknownKey;

  public ProductDoesNotExistException(String unknownKey) {
    _unknownKey = unknownKey;
  }

  public String getUnknownKey() {
    return _unknownKey;
  }
}
