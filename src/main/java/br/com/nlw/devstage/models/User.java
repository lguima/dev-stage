package br.com.nlw.devstage.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users", schema = "app")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer id;

  @Column(name = "user_name", nullable = false)
  private String name;

  @Column(name = "user_email", nullable = false, unique = true)
  private String email;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
