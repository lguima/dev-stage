package br.com.nlw.devstage.exceptions;

public class EventNotFoundException extends RuntimeException {
  public EventNotFoundException(String eventName) {
    super("Event '" + eventName + "' not found.");
  }
}
