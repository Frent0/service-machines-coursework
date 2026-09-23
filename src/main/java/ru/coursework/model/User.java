package ru.coursework.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Пользователи_системы")
public class User {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_пользователя")
 private Integer id;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_клиента")
 private Client client;

 @Column(name = "логин", nullable = false, unique = true)
 private String login;

 @Column(name = "хэш_пароля", nullable = false)
 private String passwordHash;

 public User() {}

 public Integer getId() {
  return id;
 }

 public Client getClient() {
  return client;
 }

 public String getLogin() {
  return login;
 }

 public String getPasswordHash() {
  return passwordHash;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public void setClient(Client client) {
  this.client = client;
 }

 public void setLogin(String login) {
  this.login = login;
 }

 public void setPasswordHash(String passwordHash) {
  this.passwordHash = passwordHash;
 }
}