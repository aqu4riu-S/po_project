package ggc.exceptions;

public class PartnerDoesNotExistException extends Exception{
  private String _unknownKey;

  public PartnerDoesNotExistException(String unknownKey) {
    _unknownKey = unknownKey;
  }

  public String getUnknownKey() {
    return _unknownKey;
  }
}
