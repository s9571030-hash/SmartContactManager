package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name ="USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
	@NotBlank(message="Name should not be Blank!!")
	@Size(min=2,max=20,message="min 2 and max 20 character are allowed!!")
private String name;
private String role;
@Column(unique = true)
@NotBlank(message="Email cannaot be empty!!")
@Email(message="Invalid email!!")
private String email;
private String password;
private String about;
private String imageUrl;
@Column(length=500)
private boolean enabled;
@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user",orphanRemoval = true)
private List<Contact>contacts=new ArrayList<>();
public List<Contact> getContacts() {
	return contacts;
}
public void setContacts(List<Contact> contacts) {
	this.contacts = contacts;
}
public User() {
	super();
	// TODO Auto-generated constructor stub
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getAbout() {
	return about;
}
public void setAbout(String about) {
	this.about = about;
}
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public boolean isEnabled() {
	return enabled;
}
public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}
@Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", role=" + role + ", email=" + email + ", password=" + password
			+ ", about=" + about + ", imageUrl=" + imageUrl + ", enabled=" + enabled + ", contacts=" + contacts + "]";
}


}
