package com.github.restapi.provision.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EmailUtils {

	private static Logger log = Logger.getLogger("App");

	public static void send(String to,String subect,String bodyText){  
        //Get properties object    
        Properties props = new Properties();    
        try {
            props.load(new FileInputStream(new File("src/main/resources/settings.properties")));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }  
        //get Session   
        Session session = Session.getDefaultInstance(props,    
         new javax.mail.Authenticator() {    
         protected PasswordAuthentication getPasswordAuthentication() {    
         return new PasswordAuthentication("exercisecoding@gmail.com","cloviatest");  
         }    
        });    
        
        //compose message    
        try {    
         MimeMessage message = new MimeMessage(session);    
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
         message.setSubject(subect);    
         message.setText(bodyText);    
         //send message  
         Transport.send(message);    
         log.debug("message sent successfully");    
        } catch (MessagingException e) {
        	log.error("Failed to locate message service ",e);
        	throw new RuntimeException(e);
        }    
	}
	
	public static void main(String[] args) {    
	     //from,password,to,subject,message  
	     EmailUtils.send("rchandrasekhar.reddy@gmail.com","hello javatpoint","How r u?");  
	     //change from, password and to  
	 } 
}
