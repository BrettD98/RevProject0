package com.revature.util;

import com.revature.dao.AccountDAO;
import com.revature.dao.TransferDAO;
import com.revature.dao.UserDAO;
import com.revature.model.Account;
import com.revature.model.Transfer;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.TransferService;
import com.revature.service.UserService;
import com.revature.view.View;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Factory {
    static Logger logger = Logger.getLogger(Factory.class);
    private static Connection connection;
    private static UserDAO userDAO;
    private static UserService userService;
    private static AccountDAO accountDAO;
    private static AccountService accountService;
    private static TransferDAO transferDAO;
    private static TransferService transferService;
    private static View view;

    public static Connection getConnection(){
        if(connection == null){
            ResourceBundle rb = ResourceBundle.getBundle("connection");
            String URL = rb.getString("URL");
            String USERNAME = rb.getString("USERNAME");
            String PASSWORD = rb.getString("PASSWORD");
            try {
                connection = DriverManager.getConnection(URL , USERNAME, PASSWORD);
            } catch (SQLException throwables) {
                logger.fatal("Unable to connect to database. Make sure the database exists and the .properties file is configured properly.");
                throwables.printStackTrace();
            }
        }
        return connection;
    }

    public static UserDAO getUserDAO() {
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    public static UserService getUserService() {
        if(userDAO == null){
            userService = new UserService();
        }
        return userService;
    }

    public static View getView(){
        if(view == null){
            view = new View();
        }
        return view;
    }

    public static User newUser() {
        return new User();
    }

    public static Account newAccount() {
        return new Account();
    }

    public static Transfer newTransfer() {
        return new Transfer();
    }

    public static AccountService getAccountService() {
        if( accountService == null){
            accountService = new AccountService();
        }
        return accountService;
    }

    public static AccountDAO getAccountDao() {
        if(accountDAO == null){
            accountDAO = new AccountDAO();
        }
        return accountDAO;
    }

    public static TransferDAO getTransferDAO() {
        if(transferDAO == null){
            transferDAO = new TransferDAO();
        }
        return transferDAO;
    }

    public static TransferService getTransferService(){
        if(transferService == null){
            transferService = new TransferService();
        }
        return transferService;
    }
}
