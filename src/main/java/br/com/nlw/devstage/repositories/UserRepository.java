package br.com.nlw.devstage.repositories;

import br.com.nlw.devstage.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
  public User findByEmail(String email);
}
