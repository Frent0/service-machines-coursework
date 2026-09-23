package ru.coursework.model;
import java.io.Serializable; import java.util.Objects;
public class UserRoleId implements Serializable {
 private Integer userId; private Integer roleId;
 public UserRoleId(){}
 public UserRoleId(Integer u,Integer r){userId=u;roleId=r;}
 @Override public boolean equals(Object o){if(this==o)return true;if(!(o instanceof UserRoleId x))return false;return Objects.equals(userId,x.userId)&&Objects.equals(roleId,x.roleId);}
 @Override public int hashCode(){return Objects.hash(userId,roleId);}
}
