package com.github.restapi.provision.model;

import java.io.Serializable;


public class Operation implements Serializable {

  /**
   * 
   */
	  private static final long serialVersionUID = 1L;
	  private int id;
	  private String method;
	  private Object params;
	
	  public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
}
