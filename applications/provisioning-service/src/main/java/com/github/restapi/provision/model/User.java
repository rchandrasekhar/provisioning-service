package com.github.restapi.provision.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "users")
public class User {
 
	@Id
	private long id;
	private String name;
	private String pin_number;
	private int status;
	private String email_id;
	private String role;
	private Date created_time;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreated_time(Date created_time) {
		this.created_time = created_time;
	}

	public Date getCreated_time() {
		return created_time; //Calendar.getInstance().getTime();
	}

	public String getpin_number() {
		return pin_number;
	}

	public void setpin_number(String pin_number) {
		this.pin_number = pin_number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	
 	
 	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
