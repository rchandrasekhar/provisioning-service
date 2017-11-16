package com.tecnotree.ivr.provision.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.restapi.provision.controllers.RestController;
import com.github.restapi.provision.model.Operation;
import com.github.restapi.provision.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserDAOTest extends BaseUnitTester {
	
	
	@Test
	public void testEmailVerify() throws JSONException {
		request.addParameter("validationString", "3206");
		RestController.setUserDAO(userDAO);
		String res = RestController.get(request, response);
		assertNotNull(res);
	}
	
	@Test
	public void testProvisionOperations() throws ParseException {
		testInsertData();
		testLogin();
	}
	
	public void testInsertData() {
		User u = new User();
		
  	  	u.setId((new Random().nextInt(10000)));
  	    u.setName("chandra");
  	  	u.setpin_number("987654322");
  	  	u.setEmail_id("csr@gmail.com");
  	  	u.setCreated_time(new Date());
  	  	//long unixTime = System.currentTimeMillis() / 1000L;
  	  	//u.setCreated_time(unixTime);
  	  	assertTrue(userDAO.insertData(u));
	}
	
	public void testLogin() {
		Operation op = new Operation();
		op.setMethod("login");
		op.setParams("{name=chandra, email_id=sekhar@tecnotree.com, pin_number=560037}");
		RestController.setUserDAO(userDAO);
		
  	  	assertNotNull(RestController.post(op));
	}

	public void testUpdateData() {
		User u = userDAO.getData("pin_number","9916713413");
  	  	u.setEmail_id("test@f2m.com");
		
  	  	assertTrue(userDAO.updateData(u));
	}

	public void testGetData() {
		assertNotNull(userDAO.getData("pin_number","987654321"));
	}

	public void testDeleteData() {
		String u = userDAO.deleteData(123,"pin_number","987654321");
		System.out.println(u);
		String u2 = userDAO.deleteData(123,"email_id","csr@gmail.com");
		System.out.println(u2);
		assertNotNull(u);
	}
	
}