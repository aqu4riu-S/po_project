package ggc.exceptions;

public class PartnerAlreadyExistsException extends Exception {
  private String _duplicateKey;

  public PartnerAlreadyExistsException(String duplicateKey) {
    _duplicateKey = duplicateKey;
  }

  public String getDuplicateKey() {
    return _duplicateKey;
  }
}