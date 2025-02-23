package br.com.nlw.devstage.services;

import br.com.nlw.devstage.models.Event;
import br.com.nlw.devstage.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EventService {
  @Autowired
  private EventRepository eventRepository;

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
}
