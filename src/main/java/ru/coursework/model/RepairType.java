package ru.coursework.model;
import jakarta.persistence.*; import java.math.BigDecimal;
@Entity @Table(name="Виды_ремонта")
public class RepairType {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_вида_ремонта") private Integer id;
 @Column(name="название_ремонта",nullable=false,unique=true) private String name;
 @Column(name="длительность",nullable=false) private Integer duration;
 @Column(name="стоимость",nullable=false,precision=12,scale=2) private BigDecimal cost;
 public RepairType(){}
 public Integer getId(){return id;} public String getName(){return name;} public Integer getDuration(){return duration;} public BigDecimal getCost(){return cost;}
 public void setId(Integer id){this.id=id;} public void setName(String v){name=v;} public void setDuration(Integer v){duration=v;} public void setCost(BigDecimal v){cost=v;}
}
