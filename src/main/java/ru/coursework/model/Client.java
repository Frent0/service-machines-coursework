package ru.coursework.model;
import jakarta.persistence.*;
@Entity @Table(name="Клиенты")
public class Client {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_клиента") private Integer id;
 @Column(name="название",nullable=false,unique=true) private String name;
 @Column(name="адрес",nullable=false) private String address;
 @Column(name="номер_телефона",nullable=false,unique=true) private String phone;
 @Column(name="контактное_лицо",nullable=false) private String contactPerson;
 public Client(){}
 public Integer getId(){return id;} public String getName(){return name;} public String getAddress(){return address;} public String getPhone(){return phone;} public String getContactPerson(){return contactPerson;}
 public void setId(Integer id){this.id=id;} public void setName(String v){name=v;} public void setAddress(String v){address=v;} public void setPhone(String v){phone=v;} public void setContactPerson(String v){contactPerson=v;}
}
