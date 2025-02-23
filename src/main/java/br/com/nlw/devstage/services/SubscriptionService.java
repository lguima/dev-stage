package br.com.nlw.devstage.services;

import br.com.nlw.devstage.dto.SubscriptionResponse;
import br.com.nlw.devstage.exceptions.EventNotFoundException;
import br.com.nlw.devstage.exceptions.IndicationUserNotFoundException;
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

  public SubscriptionResponse createSubscription(String eventName, User user, Integer indicationUserId) {
    Event event = eventRepository.findByPrettyName(eventName);

    if (event == null) {
      throw new EventNotFoundException(eventName);
    }

    User existingUser = userRepository.findByEmail(user.getEmail());
    User subscriber = Objects.requireNonNullElseGet(existingUser, () -> userRepository.save(user));

    User indicationUser = null;

    if (indicationUserId != null) {
      indicationUser = userRepository.findById(indicationUserId).orElse(null);

      if (indicationUser == null) {
        throw new IndicationUserNotFoundException(indicationUserId);
      }
    }

    Subscription existingSubscription = subscriptionRepository.findByEventAndSubscriber(event, subscriber);

    if (existingSubscription != null) {
      throw new SubscriptionConflictException(existingSubscription);
    }

    Subscription subscription = new Subscription();
    subscription.setEvent(event);
    subscription.setSubscriber(subscriber);

    if (indicationUser != null) {
      subscription.setIndication(indicationUser);
    }

    Subscription savedSubscription = subscriptionRepository.save(subscription);

    return new SubscriptionResponse(savedSubscription.getSubscriptionNumber(), this.getIndicationUrl(event, subscriber));
  }

  public String getIndicationUrl(Event event, User subscriber) {
    try {
      return new URI("http://localhost:8080/subscriptions/" + event.getPrettyName() + "/" + subscriber.getId()).toURL().toString();
    } catch (URISyntaxException | MalformedURLException e) {
      return null;
    }
  }
}
