package br.com.nlw.devstage.services;

import br.com.nlw.devstage.dto.SubscriptionResponse;
import br.com.nlw.devstage.exceptions.EventNotFoundException;
import br.com.nlw.devstage.exceptions.InvalidIndicationException;
import br.com.nlw.devstage.exceptions.SubscriptionConflictException;
import br.com.nlw.devstage.models.Event;
import br.com.nlw.devstage.models.Subscription;
import br.com.nlw.devstage.models.User;
import br.com.nlw.devstage.repositories.EventRepository;
import br.com.nlw.devstage.repositories.SubscriptionRepository;
import br.com.nlw.devstage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Service
public class SubscriptionService {
  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  public SubscriptionResponse createSubscription(String eventName, User user, Integer subscriptionNumber) {
    Event event = eventRepository.findByPrettyName(eventName);

    if (event == null) {
      throw new EventNotFoundException(eventName);
    }

    User existingUser = userRepository.findByEmail(user.getEmail());

    Subscription subscriptionIndication = null;

    if (subscriptionNumber != null) {
      subscriptionIndication = subscriptionRepository.findBySubscriptionNumber(subscriptionNumber);

      if (subscriptionIndication == null) {
        throw new InvalidIndicationException();
      }
    }

    // Same subscriber and indicator.
    if (existingUser != null && subscriptionIndication != null && subscriptionIndication.getSubscriber().getId().equals(existingUser.getId())) {
      throw new InvalidIndicationException();
    }

    // Different event from subscription indication.
    if (subscriptionIndication != null && subscriptionIndication.getEvent().getEventId() != event.getEventId()) {
      throw new InvalidIndicationException();
    }

    if (existingUser != null) {
      Subscription existingSubscription = subscriptionRepository.findByEventAndSubscriber(event, existingUser);

      if (existingSubscription != null) {
        throw new SubscriptionConflictException(existingSubscription);
      }
    }

    User subscriber = Objects.requireNonNullElseGet(existingUser, () -> userRepository.save(user));

    Subscription subscription = new Subscription();
    subscription.setEvent(event);
    subscription.setSubscriber(subscriber);

    if (subscriptionIndication != null) {
      subscription.setIndication(subscriptionIndication.getSubscriber());
    }

    Subscription savedSubscription = subscriptionRepository.save(subscription);

    return new SubscriptionResponse(savedSubscription.getSubscriptionNumber(), this.getIndicationUrl(event, subscription));
  }

  public String getIndicationUrl(Event event, Subscription subscription) {
    try {
      return new URI("http://localhost:8080/subscriptions/" + event.getPrettyName() + "/" + subscription.getSubscriptionNumber()).toURL().toString();
    } catch (URISyntaxException | MalformedURLException e) {
      return null;
    }
  }
}
