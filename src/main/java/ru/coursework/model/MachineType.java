package ru.coursework.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "Типы_станков",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "страна_производитель",
                        "год_выпуска",
                        "бренд"
                }
        )
)
public class MachineType {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_типа")
 private Integer id;

 @Column(name = "страна_производитель", nullable = false)
 private String country;

 @Column(name = "год_выпуска", nullable = false)
 private Short productionYear;

 @Column(name = "бренд", nullable = false)
 private String brand;

 public MachineType() {}

 public Integer getId() {
  return id;
 }

 public String getCountry() {
  return country;
 }

 public Short getProductionYear() {
  return productionYear;
 }

 public String getBrand() {
  return brand;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public void setCountry(String v) {
  country = v;
 }

 public void setProductionYear(Short v) {
  productionYear = v;
 }

 public void setBrand(String v) {
  brand = v;
 }
}