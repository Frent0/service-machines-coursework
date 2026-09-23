package ru.coursework.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Роли")
public class Role {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_роли")
 private Integer id;

 @Column(name = "название_роли", nullable = false, unique = true)
 private String name;

 public Role() {}

 public Integer getId() {
  return id;
 }

 public String getName() {
  return name;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public void setName(String name) {
  this.name = name;
 }

 @Override
 public String toString() {
  return name;
 }
}