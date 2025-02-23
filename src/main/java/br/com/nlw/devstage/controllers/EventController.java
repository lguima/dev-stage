package br.com.nlw.devstage.controllers;

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
}
