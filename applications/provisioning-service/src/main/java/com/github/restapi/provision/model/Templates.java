package com.github.restapi.provision.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "templates")
public class Templates {
 
	@Id
	private Object id;
	private String name;
	private String content;

	public Templates() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setValue(String content) {
		this.content = content;
	}
}
