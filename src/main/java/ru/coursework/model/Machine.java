package ru.coursework.model;
import jakarta.persistence.*;
@Entity @Table(name="Станки")
public class Machine {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_станка") private Integer id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_клиента") private Client client;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_типа") private MachineType type;
 @Column(name="инвентарный_номер",nullable=false,unique=true) private String inventoryNumber;
 public Machine(){}
 public Integer getId(){return id;} public Client getClient(){return client;} public MachineType getType(){return type;} public String getInventoryNumber(){return inventoryNumber;}
 public void setId(Integer id){this.id=id;} public void setClient(Client v){client=v;} public void setType(MachineType v){type=v;} public void setInventoryNumber(String v){inventoryNumber=v;}
}
