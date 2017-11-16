package com.github.restapi.provision.controllers;


import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.github.restapi.provision.dao.UserDAO;
import com.github.restapi.provision.model.Operation;
import com.github.restapi.provision.model.User;
import com.github.restapi.provision.utils.EmailUtils;

@Controller
@RequestMapping("/provision")
public class RestController implements ServletContextAware {

	public static UserDAO userDAO;

	@Override
	public void setServletContext(ServletContext servletContext) {
		userDAO = new UserDAO();
	}

	private static Logger Provlog = Logger.getLogger("Pro");
	private static Logger Applog = Logger.getLogger("App");
	private static int status = 1;

	// all supported post method operations
	public enum Operations {
		create, login, update, delete, get;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	  public static @ResponseBody String get(HttpServletRequest req,HttpServletResponse res) {
		try {
				User u = userDAO.getById("_id",req.getParameter("validationString"));
				if(u==null) {
					return "{\"get\":\"failed\",\"reason\":\"User not exists\"}";
				}
				Applog.info("get,"+u.getId()+","+u.getName()+","+u.getEmail_id()+","+u.getpin_number()+",success");
				return "{\"id\":\""+u.getId()+"\",\"name\":"+u.getName()+",\"status\":"+u.getStatus()+",\"email_id\":\""+u.getEmail_id()+",\"pincode\":\""+u.getpin_number()+"\"}";
		} catch (Exception e) {
			return "{\"get\":\"failed\",\"reason\":\"User not exists\"}";
	  }
	}

	/**this method response to POST request http://localhost/spring-mvc-json/rest/cont/person
	receives json data sent by client --> map it to Person object
	 return Person object as json */
	@RequestMapping(value="execute", method = RequestMethod.POST)
	public static @ResponseBody String post( @RequestBody final  Operation operation) {	

		String name = null, pin_number = null,email_id = null;
		try {

			final Operations o = Operations.valueOf(operation.getMethod());
			switch(o)
			{
			case create:
				try {
					JSONObject createJson = new JSONObject(operation.getParams().toString());
					try {
						pin_number = verifyAndFixPinNumber(createJson.get("pin_number").toString());
					} catch (InvalidPinNumberException e) {
						Provlog.info("create,"+operation.getId()+","+createJson.optString("name")+","+createJson.optString("name")+","+""+createJson.optString("pin_number")+","+createJson.optString("email_id")+",failed,\"reason\":\""+e.getMessage()+"\"}");
						return "{\"id\":\""+operation.getId()+"\",\"name\":"+createJson.optString("name")+"\",\"pin_number\":"+createJson.optString("pin_number")+",\"create\":\"failed\",\"reason\":\""+e.getMessage()+"\"}";
					}
					if(createJson.has("name") && createJson.has("pin_number") && createJson.has("email_id"))
					{
						if((userDAO.getData("name",name) == null)) {
							User u = new User();
							u.setName(createJson.get("name").toString());
							u.setpin_number(pin_number);
							u.setEmail_id(createJson.get("email_id").toString());
							if(createJson.has("status")) {
								u.setStatus(Integer.parseInt(createJson.get("status").toString()));
							} else {
								u.setStatus(status);
							}
							u.setCreated_time(new Date());
							u.setRole("user");

							if (userDAO.insertData(u)) {
								Provlog.info("create,"+u.getId()+","+createJson.optString("name")+","+createJson.optString("pin_number")+","+createJson.optString("email_id")+",success");
								return "{\"id\":\""+u.getId()+"\",\"create\":\"success\"}";
							}
						}
						Provlog.info("create,"+operation.getId()+""+createJson.optString("pin_number")+","+createJson.optString("email_id")+",failed,\"reason\":\"User already exists\"}");
						return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"create\":\"failed\",\"reason\":\"User already exists\"}";
					}
					Provlog.info("create,"+operation.getId()+","+createJson.optString("name")+","+createJson.optString("pin_number")+","+createJson.optString("email_id")+",failed");
					return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"create\":\"failed\"}";
				} catch (JSONException e) {
					Provlog.info("create,"+operation.getId()+","+name+","+pin_number+","+email_id+",failed");
					Applog.debug("Error:"+e.getMessage());
					return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"create\":\"failed\"}";
				}
			
			/** -	Call login api with UserId
				-	Send login link (http-link) to his registered email.
				-	Login-link expires in 15 min
			*/
			case login:
				try {
					JSONObject createJson = new JSONObject(operation.getParams().toString());

					if((createJson.has("name")))
					{
						User u = userDAO.getData("name",createJson.optString("name"));
						if(u==null) {
							return "{\"id\":\""+operation.getId()+"\",\"name\":"+name+",\"login\":\"failed\",\"reason\":\"User not exists\"}";
						}
						Applog.info("login,"+operation.getId()+","+createJson.optString("fax_number")+","+createJson.optString("email_id")+",success");
						
						// Send email with http-link to user email id
						String bodyText = "Dear "+u.getName()+",\nCongratulations! You have been registered successfully on test service!!\n\nPlease click the link below to activate your account:\n http://localhost:8080/rest/get/confirmAction?validationString="+u.getId()+" \n Best regards,\nCommunications Team\nNote: This is a system generated e-mail, please do not reply to it.\n\n*** This message is intended only for the person or entity to which it is addressed and may contain confidential and/or privileged information. If you have received this message in error, please notify the sender immediately and delete this message from your system ***";
						try {
							EmailUtils.send("rchandrasekhar.reddy@gmail.com", "Registration Confirmation", bodyText);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						return "{\"id\":\""+operation.getId()+"\",\"params\":{\"name\":"+u.getName()+",\"status\":"+u.getStatus()+",\"email_id\":\""+u.getEmail_id()+"\"}}";
					} else if((createJson.has("email_id"))) {
						email_id = createJson.get("email_id").toString();
						User u = userDAO.getData("email_id",email_id);
						if(u==null) {
							return "{\"id\":\""+operation.getId()+"\",\"email_id\":"+email_id+",\"login\":\"failed\",\"reason\":\"User not exists\"}";
						}
						Provlog.info("get,"+operation.getId()+","+createJson.optString("fax_number")+","+createJson.optString("email_id")+",success");
						return "{\"id\":\""+operation.getId()+"\",\"params\":{\"name\":"+u.getName()+",\"status\":"+u.getStatus()+",\"email_id\":\""+u.getEmail_id()+"\"}}";
					}
					Provlog.info("login,"+operation.getId()+","+name+","+email_id+",failed");
					return "{\"id\":\""+operation.getId()+"\",\"name\":"+name+",\"email_id\":\""+email_id+"\",\"get\":\"failed\"}";
				} catch (JSONException e) {
					Provlog.info("login,"+operation.getId()+","+name+","+email_id+",failed");
					Applog.debug("Error:"+e.getMessage());
					return "{\"id\":\""+operation.getId()+"\",\"name\":"+name+",\"email_id\":\""+email_id+"\",\"get\":\"failed\"}";
				}catch (NullPointerException e) {
					Provlog.info("login,"+operation.getId()+","+name+","+email_id+",failed");
					Applog.debug("Error:"+e.getMessage());
					return "{\"id\":\""+operation.getId()+"\",\"name\":"+name+",\"email_id\":\""+email_id+"\",\"get\":\"failed\",\"reason\":\""+e.getMessage()+"\"}";
				} 

			case update:
				Provlog.info("update,"+operation.getId()+","+name+","+pin_number+","+email_id+",Method not found");
				Applog.debug("{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}");
				return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}";

			case delete:
				Provlog.info("delete,"+operation.getId()+","+name+","+pin_number+","+email_id+",Method not found");
				Applog.debug("{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}");
				return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}";

			case get:
				Provlog.info("get,"+operation.getId()+","+name+","+pin_number+","+email_id+",Method not found");
				Applog.debug("{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}");
				return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}";

			default:
				Provlog.info("get,"+operation.getId()+","+name+","+pin_number+","+email_id+",Method not found");
				Applog.debug("{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}");
				return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}";
			}
		}catch(IllegalArgumentException Ille) {
			Provlog.info("get,"+operation.getId()+","+name+","+pin_number+","+email_id+",Method not found");
			return "{\"id\":\""+operation.getId()+"\",\"pin_number\":"+pin_number+",\"error\": {\"code\": -32601, \"message\": \"Method not found\"}}";
		}
	}

	private static String verifyAndFixPinNumber(String number) throws InvalidPinNumberException {
		if(number.length() < 6 && number.length() > 6) {
			throw new InvalidPinNumberException("PIN should be of 6 digits length");
		}
		return number;
	}

	public static UserDAO getUserDAO() {
		return userDAO;
	}

	public static void setUserDAO(UserDAO userDAO) {
		RestController.userDAO = userDAO;
	}
}

