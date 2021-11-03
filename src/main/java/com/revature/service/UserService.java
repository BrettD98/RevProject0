package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.model.User;
import com.revature.util.Factory;
import org.apache.log4j.Logger;

public class UserService {
    static Logger logger = org.apache.log4j.Logger.getLogger(UserService.class);
    private final UserDAO dao;

    public UserService() {
        this.dao = Factory.getUserDAO();
    }

    public UserService(UserDAO userDAO) {
        dao = userDAO;
    }

    public User insertUser(User user){
        if(user.getEmail() == null){
            logger.error("Cannot insert a null object");
            return null;
        }else{
            return dao.insert(user);
        }
    }

    public User getUserByEmail(User user) {
        if(user.getEmail() == null){
            logger.error("User does not exist.");
            return null;
        }else{
            User compare = dao.select(user.getEmail());
            if(compare.getEmail().equals(user.getEmail()) && compare.getPassword().equals(user.getPassword())){
                return compare;
            }else{
                logger.error("Email or Password does not match.");
                return null;
            }
        }
    }
}
