package com.github.restapi.provision.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.github.restapi.provision.model.Event;
import com.github.restapi.provision.model.Templates;
import com.github.restapi.provision.model.User;

public class UserDAO {
    
    private static Logger RMlog = Logger.getLogger("RM");
    private static Logger Applog = Logger.getLogger("App");
    private static MongoOperations mongoOperation;

    public UserDAO() {
		// TODO Auto-generated constructor stub
	}
    
    @SuppressWarnings("static-access")
	public UserDAO(MongoOperations injMongoTemplate){
    	this.mongoOperation =  injMongoTemplate;
    }
    
    public boolean insertData(User user) {
        boolean result = false;
       try {
    	    // save
    		mongoOperation.insert(user);
            result=true;
        } catch (Exception e) {
            result=false;
            Applog.debug(" INSERT :  Error log - " + e);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public boolean insertEvent(Event event) {
        boolean result = false;
       try {
    	    // save
    		mongoOperation.insert(event);
            result=true;
        } catch (Exception e) {
            result=false;
            Applog.debug(" INSERT :  Error log - " + e);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public boolean updateData(User user) {
        boolean result = false;
        try {
			mongoOperation.save(user);
            result=true;
        } catch (Exception e) {
            result=false;
            Applog.debug(" Update :  Error log - " + e);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public String deleteData(int id, String key, String value) {

    	// query to search user
    	Query searchUserQuery = new Query(Criteria.where(key).is(value));
	       try {
	    	    // delete
	    		User ud = mongoOperation.findAndRemove(searchUserQuery, User.class);
	    		if(ud == null) {
	    			RMlog.info("delete,"+id+","+key+","+value+",failed,User not exists");
	    			return "{\"id\":\""+id+"\",\""+key+"\":"+ ((key.equals("pin_number"))? value : "\""+value+"\"") +",\"delete\":\"failed\",\"reason\":\"User not exists\"}";
	    		} else {
	    			RMlog.info("delete,"+id+","+key+","+value+",success");
	    			return "{\"id\":\""+id+"\",\""+key+"\":"+ ((key.equals("pin_number"))? value : "\""+value+"\"") +",\"delete\":\"success\"}";
	    		}
	        } catch (Exception e) {
	            Applog.debug(" INSERT :  Error log - " + e);
	        } finally {
	            try {
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	       return "{\"id\":\""+id+"\",\""+key+"\":"+value+",\"delete\":\"failed\"}";
    }
    
    public User getData(String key, String value) {
    	Query query = new Query(Criteria.where(key).is(value));
    	User user= mongoOperation.findOne(query,User.class);
        return user;
    }
    
    public User getById(String key, String value) {
    	Query query = new Query(Criteria.where(key).is(Integer.parseInt(value)));
    	User user= mongoOperation.findOne(query,User.class);
        return user;
    }
    
    public List<User> listData(String pin_number) {
    	List<User> listUser = mongoOperation.findAll(User.class);
        return listUser;
    }
    
    public String getTemplateData(String key, String value, String hardcoded) {
    	Query query = new Query(Criteria.where(key).is(value));
    	Templates template= mongoOperation.findOne(query,Templates.class);
        if(template == null) {
        	return hardcoded;
        }
    	return template.getContent();
    }
}
