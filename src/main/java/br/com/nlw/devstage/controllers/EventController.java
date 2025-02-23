package br.com.nlw.devstage.controllers;

import br.com.nlw.devstage.dto.ErrorMessage;
import br.com.nlw.devstage.exceptions.EventNotFoundException;
import br.com.nlw.devstage.exceptions.InvalidIndicationException;
import br.com.nlw.devstage.models.Event;
import br.com.nlw.devstage.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
  @Autowired
  private EventService eventService;

  @PostMapping("/events")
  public Event addEvent(@RequestBody Event event) {
    return eventService.addEvent(event);
  }

  @GetMapping("/events")
  public List<Event> getAllEvents() {
    return eventService.getAllEvents();
  }

  @GetMapping("/events/{prettyName}")
  public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName) {
    Event event = eventService.getEventByPrettyName(prettyName);

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(event);
  }

  @GetMapping("/events/{eventName}/rankings/subscribers")
  public ResponseEntity<?> getSubscribersRanking(@PathVariable String eventName) {
    try {
      return ResponseEntity.ok().body(eventService.getSubscribersRanking(eventName).subList(0, 3));
    } catch (EventNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
    }
  }

  @GetMapping("/events/{eventName}/rankings/subscribers/users/{subscriberId}")
  public ResponseEntity<?> getSubscribersRanking(
    @PathVariable String eventName,
    @PathVariable Integer subscriberId
  ) {
    try {
      return ResponseEntity.ok().body(eventService.getSubscribersRankingByUser(eventName, subscriberId));
    } catch (EventNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
    } catch (InvalidIndicationException e) {
      return ResponseEntity.status(400).body(new ErrorMessage(e.getMessage()));
    }
  }
}
