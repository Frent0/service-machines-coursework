package ru.coursework.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Ремонты")
public class Repair {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_ремонта")
 private Integer id;

 @ManyToOne(fetch = FetchType.LAZY, optional = false)
 @JoinColumn(name = "id_станка")
 private Machine machine;

 @ManyToOne(fetch = FetchType.LAZY, optional = false)
 @JoinColumn(name = "id_вида_ремонта")
 private RepairType repairType;

 @Column(name = "дата_начала", nullable = false)
 private LocalDate startDate;

 @Column(name = "дата_окончания")
 private LocalDate endDate;

 @Column(name = "статус", nullable = false)
 private String status;

 public Repair() {
 }

 public Integer getId() {
  return id;
 }

 public Machine getMachine() {
  return machine;
 }

 public RepairType getRepairType() {
  return repairType;
 }

 public LocalDate getStartDate() {
  return startDate;
 }

 public LocalDate getEndDate() {
  return endDate;
 }

 public String getStatus() {
  return status;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public void setMachine(Machine machine) {
  this.machine = machine;
 }

 public void setRepairType(RepairType repairType) {
  this.repairType = repairType;
 }

 public void setStartDate(LocalDate startDate) {
  this.startDate = startDate;
 }

 public void setEndDate(LocalDate endDate) {
  this.endDate = endDate;
 }

 public void setStatus(String status) {
  this.status = status;
 }
}