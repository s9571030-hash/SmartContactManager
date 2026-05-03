package com.smart.entities;




import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int cid;
private String name;
private String secname;
private String  phone;
private String email;
private String work;
@Column(length=1000)
private String description;
private String image;

@ManyToOne
@JsonIgnore
private  User user;



public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public int getCid() {
	return cid;
}
public void setCid(int cid) {
	this.cid = cid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getSecname() {
	return secname;
}
public void setSecname(String secname) {
	this.secname = secname;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getWork() {
	return work;
}
public void setWork(String work) {
	this.work = work;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public Contact() {
	super();
	// TODO Auto-generated constructor stub
}
//@Override
//public String toString() {
//	return "Contact [cid=" + cid + ", name=" + name + ", secname=" + secname + ", phone=" + phone + ", email=" + email
//			+ ", work=" + work + ", description=" + description + ", image=" + image + ", user=" + user + "]";
//}
@Override
public boolean equals(Object obj) {
	return this.cid==((Contact)obj).getCid();
}

}
