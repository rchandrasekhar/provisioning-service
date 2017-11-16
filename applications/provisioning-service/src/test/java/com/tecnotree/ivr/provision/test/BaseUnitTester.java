package com.tecnotree.ivr.provision.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.github.restapi.provision.dao.UserDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/rest-servlet.xml" })
public abstract class BaseUnitTester {
	
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
    
	@Autowired
	protected UserDAO userDAO;

	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
	}

	@After
	public void anythingAfter() {

	}

}
