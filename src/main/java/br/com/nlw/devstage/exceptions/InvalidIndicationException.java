package br.com.nlw.devstage.exceptions;

public class InvalidIndicationException extends RuntimeException {
  public InvalidIndicationException() {
    super("Invalid indication.");
  }
}
