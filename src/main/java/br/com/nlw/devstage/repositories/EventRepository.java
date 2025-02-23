package br.com.nlw.devstage.repositories;

import br.com.nlw.devstage.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Integer> {
  public Event findByPrettyName(String prettyName);
}
