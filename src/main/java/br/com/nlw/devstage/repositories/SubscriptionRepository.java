package br.com.nlw.devstage.repositories;

import br.com.nlw.devstage.models.Event;
import br.com.nlw.devstage.models.Subscription;
import br.com.nlw.devstage.models.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
  public Subscription findByEventAndSubscriber(Event event, User subscriber);

  public Subscription findBySubscriptionNumber(Integer subscriptionNumber);
}
