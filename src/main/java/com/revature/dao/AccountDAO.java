package com.revature.dao;

import com.revature.model.Account;
import com.revature.util.Factory;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements DAO<Account>{
    static Logger logger = Logger.getLogger(AccountDAO.class);

    @Override
    public Account insert(Account obj) {
        String sql = "insert into Account (email, balance, accepted) values (?,?,?);";
        Connection connection = Factory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, obj.getEmail());
            ps.setDouble(2, obj.getBalance());
            ps.setBoolean(3, false);
            if(ps.execute()){
                logger.error("Failure to insert into database.");
            }
        } catch (SQLException throwables) {
            logger.fatal("Unable to insert into database.");
        }
        return obj;
    }

    public List<Account> selectByEmail(String email) {
        String sql = "select * from account where email = ?;";
        Connection connection = Factory.getConnection();
        List<Account> accounts = new ArrayList<>();

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = Factory.newAccount();
                account.setAccountId(rs.getInt(1));
                account.setEmail(rs.getString(2));
                account.setBalance(rs.getDouble(3));
                account.setAccepted(rs.getBoolean(4));
                accounts.add(account);
            }
        }catch (SQLException e){
            logger.fatal("Unable to read from database.");
        }
        return accounts;
    }

    @Override
    public List<Account> selectAll() {
        String sql = "select * from account";
        Connection connection = Factory.getConnection();
        List<Account> accounts = new ArrayList<>();

        try{
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = Factory.newAccount();
                account.setAccountId(rs.getInt(1));
                account.setEmail(rs.getString(2));
                account.setBalance(rs.getDouble(3));
                account.setAccepted(rs.getBoolean(4));
                accounts.add(account);
            }

        }catch (SQLException e){
            logger.fatal("Unable to read from database.");
        }
        return accounts;
    }

    @Override
    public Account select(String value) {
        String sql = "select * from account where account_id = ? ;";
        Connection connection = Factory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(value));
            ResultSet rs = ps.executeQuery();
            Account account = Factory.newAccount();

            rs.next();
            account.setAccountId(rs.getInt(1));
            account.setEmail(rs.getString(2));
            account.setBalance(rs.getDouble(3));
            account.setAccepted(rs.getBoolean(4));

            return account;
        } catch (SQLException throwables) {
            logger.fatal("Unable to read from database.");
        }
        return null;
    }

    @Override
    public Account update(Account obj) {
        String sql = "update account set balance = ? where account_id = ?;";
        Connection connection = Factory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, obj.getBalance());
            ps.setInt(2,obj.getAccountId());

            if(ps.execute()){
                logger.error("Unable to update balance.");
            }
        } catch (SQLException throwables) {
            logger.fatal("Unable to update the database.");
        }
        return obj;
    }

    @Override
    public boolean delete(Account obj) {
        String sql = "delete from account where account_id = ?;";
        Connection connection = Factory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, obj.getAccountId());
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.fatal("Unable to remove from database.");
            return false;
        }
        return true;
    }

    public List<Account> selectByApproved() {
        String sql = "select * from account where accepted = false;";
        Connection connection = Factory.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Account account = Factory.newAccount();
                account.setAccountId(rs.getInt(1));
                account.setEmail(rs.getString(2));
                account.setBalance(rs.getDouble(3));
                account.setAccepted(rs.getBoolean(4));
                accounts.add(account);
            }

        } catch (SQLException throwables) {
            logger.fatal("Unable to read from database");
        }
        return accounts;
    }

    public void approve(Account obj) {
        String sql = "update account set accepted = true where account_id = ?;";
        Connection connection = Factory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,obj.getAccountId());

            if(ps.execute()){
                logger.error("Unable to update balance.");
            }
        } catch (SQLException throwables) {
            logger.fatal("Unable to update the database.");
        }
    }
}
