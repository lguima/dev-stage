package br.com.nlw.devstage.exceptions;

public class InvalidIndicationUserException extends RuntimeException {
  public InvalidIndicationUserException(Integer subscriptionNumber) {
    super("Invalid indication user.");
  }
}
