package ru.coursework.model;
import jakarta.persistence.*;
@Entity @Table(name="Пользователи_Роли") @IdClass(UserRoleId.class)
public class UserRole {
 @Id @Column(name="id_пользователя") private Integer userId;
 @Id @Column(name="id_роли") private Integer roleId;
 public UserRole(){}
 public UserRole(Integer u,Integer r){userId=u;roleId=r;}
 public Integer getUserId(){return userId;} public Integer getRoleId(){return roleId;}
 public void setUserId(Integer v){userId=v;} public void setRoleId(Integer v){roleId=v;}
}
