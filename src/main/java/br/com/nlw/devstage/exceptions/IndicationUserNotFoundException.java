package br.com.nlw.devstage.exceptions;

public class IndicationUserNotFoundException extends RuntimeException {
  public IndicationUserNotFoundException(Integer indicationUserId) {
    super("Indication user ID '" + indicationUserId + "' not found.");
  }
}
