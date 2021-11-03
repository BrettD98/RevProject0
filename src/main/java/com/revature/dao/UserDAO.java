package com.revature.dao;

import com.revature.model.User;
import com.revature.util.Factory;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User>{
    static Logger logger = Logger.getLogger(UserDAO.class);

    @Override
    public User insert(User obj) {
        String sql = "insert into user (first_name, last_name, password, email, employee) values (?,?,?,?,?);";
        Connection connection = Factory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,obj.getFirstName());
            ps.setString(2,obj.getLastName());
            ps.setString(3,obj.getPassword());
            ps.setString(4,obj.getEmail());
            ps.setBoolean(5,obj.isEmployee());

            if(ps.execute()){
                logger.error("Email is taken.");
            }
        } catch (SQLException throwables) {
            logger.fatal("Unable to insert into database.");
        }
        return select(obj.getEmail());
    }

    @Override
    public List<User> selectAll() {
        String sql = "select * from user;";
        Connection connection = Factory.getConnection();
        List<User> users = new ArrayList<User>();

        try{
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User user = Factory.newUser();
                user.setFirstName(rs.getString(1));
                user.setLastName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setEmployee(rs.getBoolean(5));
                users.add(user);
            }

        }catch (SQLException e){
            logger.fatal("Unable to read from database.");
        }
        return users;
    }

    @Override
    public User select(String value) {
        String sql = "call sp_get_user_by_email(?);";
        Connection connection = Factory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            User user = Factory.newUser();

            rs.next();
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setEmployee(rs.getBoolean("employee"));

            return user;
        } catch (SQLException throwables) {
            logger.fatal("Unable to read from database.");
        }
        return null;

    }

    @Override
    public User update(User obj) {
        return null;
    }

    @Override
    public boolean delete(User obj) {
        return false;
    }
}
