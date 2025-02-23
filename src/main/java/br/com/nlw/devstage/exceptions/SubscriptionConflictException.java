package br.com.nlw.devstage.exceptions;

import br.com.nlw.devstage.models.Subscription;

public class SubscriptionConflictException extends RuntimeException {
  public SubscriptionConflictException(Subscription subscription) {
    super("User '" + subscription.getSubscriber().getName() + "' is already subscribed to event '" + subscription.getEvent().getTitle() + "'.");
  }
}
