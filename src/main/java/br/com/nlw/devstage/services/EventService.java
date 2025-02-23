package br.com.nlw.devstage.services;

import br.com.nlw.devstage.dto.SubscriptionRankingItem;
import br.com.nlw.devstage.dto.SubscritpionRankingByUser;
import br.com.nlw.devstage.exceptions.EventNotFoundException;
import br.com.nlw.devstage.exceptions.InvalidIndicationException;
import br.com.nlw.devstage.models.Event;
import br.com.nlw.devstage.repositories.EventRepository;
import br.com.nlw.devstage.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EventService {
  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  public Event addEvent(Event event) {
    event.setPrettyName(
      Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        .matcher(Normalizer.normalize(event.getTitle().toLowerCase(), Normalizer.Form.NFD))
        .replaceAll("")
        .replaceAll("[^a-z0-9\\s]", "")
        .replace(" ", "-")
    );

    return eventRepository.save(event);
  }

  public List<Event> getAllEvents() {
    return (List<Event>) eventRepository.findAll();
  }

  public Event getEventByPrettyName(String prettyName) {
    return eventRepository.findByPrettyName(prettyName);
  }

  public List<SubscriptionRankingItem> getSubscribersRanking(String eventName) {
    Event event = eventRepository.findByPrettyName(eventName);

    if (event == null) {
      throw new EventNotFoundException(eventName);
    }

    return subscriptionRepository.generateRankingByEvent(event.getEventId());
  }

  public SubscritpionRankingByUser getSubscribersRankingByUser(String eventName, Integer userId) {
    List<SubscriptionRankingItem> subscriptionRankingItems = this.getSubscribersRanking(eventName);

    SubscriptionRankingItem userRanking = subscriptionRankingItems.stream()
      .filter(i -> i.userId().equals(userId))
      .findFirst()
      .orElse(null);

    if (userRanking == null) {
      throw new InvalidIndicationException();
    }

    Integer position = subscriptionRankingItems.indexOf(userRanking) + 1;

    return new SubscritpionRankingByUser(userRanking, position);
  }
}
