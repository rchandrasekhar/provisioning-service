package com.github.restapi.provision.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event {
	private int eventid; 		//0 a
	private String event; 		//1 b
	private String email_from; 	//4 e
	private String email_to;	//5 f
	private String status;			//10 k
	private String desc; 			//11 l
	private Date event_date;	//12 m
	private Date last_updated_date;	//13 n
	
	public Event() {
		// TODO Auto-generated constructor stub
	}
	
	public Event(int a, String b, String c, String d, String e, String f, int g, int h, double i, String j, String k, String l, Date m, Date n, String o){
				this.eventid= a;
				this.event= b;
				this.email_from= e; 
				this.email_to= f;
				this.status= k;
				this.desc= l;
				this.last_updated_date= m;
				this.event_date= n;
	}

	public int getEventid() {
		return eventid;
	}

	public void setEventid(int eventid) {
		this.eventid = eventid;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEmail_from() {
		return email_from;
	}

	public void setEmail_from(String email_from) {
		this.email_from = email_from;
	}

	public String getEmail_to() {
		return email_to;
	}

	public void setEmail_to(String email_to) {
		this.email_to = email_to;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getEvent_date() {
		return event_date;
	}

	public void setEvent_date(Date event_date) {
		this.event_date = event_date;
	}

	public Date getLast_updated_date() {
		return last_updated_date;
	}

	public void setLast_updated_date(Date last_updated_date) {
		this.last_updated_date = last_updated_date;
	}

}
