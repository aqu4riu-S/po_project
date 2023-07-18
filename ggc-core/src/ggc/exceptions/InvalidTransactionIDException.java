package ggc.exceptions;

public class InvalidTransactionIDException extends Exception {
  private int _unknownID;
  
  public InvalidTransactionIDException(int unknownID) {
    _unknownID = unknownID;
  }

  public int getUnknownKey() {
    return _unknownID;
  }
}