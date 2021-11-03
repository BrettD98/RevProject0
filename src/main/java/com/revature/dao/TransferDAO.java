package com.revature.dao;

import com.revature.model.Transfer;
import com.revature.util.Factory;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransferDAO implements DAO<Transfer>{
    static Logger logger = Logger.getLogger(TransferDAO.class);

    @Override
    public Transfer insert(Transfer obj) {
        String sql = "insert into transfer (accepted, from_id, to_id, amount) values (?,?,?,?);";
        Connection connection = Factory.getConnection();

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setBoolean(1, false);
            ps.setInt(2, obj.getFromId());
            ps.setInt(3, obj.getToId());
            ps.setDouble(4,obj.getAmount());

            ps.execute();
        }catch (SQLException e){
            logger.fatal("Unable to insert into database.");
        }
        return obj;
    }

    @Override
    public List<Transfer> selectAll() {
        List<Transfer> transfers = new ArrayList<>();
        Connection connection = Factory.getConnection();
        String sql = "select * from transfer;";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Transfer transfer = Factory.newTransfer();
                transfer.setTransferId(rs.getInt(1));
                transfer.setAccepted(rs.getBoolean(2));
                transfer.setFromId(rs.getInt(3));
                transfer.setToId(rs.getInt(4));
                transfer.setAmount(rs.getDouble(5));
                transfers.add(transfer);
            }

        } catch (SQLException throwables) {
            logger.fatal("Unable to read from database.");
        }
        return transfers;
    }

    @Override
    public Transfer select(String value) {
        String sql = "select * from transfer where transfer_id = ?";
        Connection connection = Factory. getConnection();
        Transfer transfer = Factory.newTransfer();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(value));
            ResultSet rs = ps.executeQuery();
            rs.next();

            transfer.setTransferId(rs.getInt(1));
            transfer.setAccepted(rs.getBoolean(2));
            transfer.setFromId(rs.getInt(3));
            transfer.setToId(rs.getInt(4));
            transfer.setAmount(rs.getDouble(5));
        } catch (SQLException throwables) {
            logger.fatal("Unable to read from database.");
        }
        return transfer;
    }

    @Override
    public Transfer update(Transfer obj) {
        String sql = "update transfer set accepted = true where transfer_id = ?;";
        Connection connection = Factory. getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,obj.getTransferId());
            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.fatal("Unable to update database.");
        }
        obj.setAccepted(true);
        return obj;
    }

    @Override
    public boolean delete(Transfer obj) {
        String sql = "delete from transfer where transfer_id = ?;";
        Connection connection = Factory. getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,obj.getTransferId());
            ps.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.fatal("Unable to delete from database.");
        }
        return false;
    }

    public List<Transfer> selectByAccount(int accountId) {
        String sql = "select * from transfer where to_id = ?;";
        Connection connection = Factory.getConnection();
        List<Transfer> transfers = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Transfer transfer = Factory.newTransfer();

                transfer.setTransferId(rs.getInt(1));
                transfer.setAccepted(rs.getBoolean(2));
                transfer.setFromId(rs.getInt(3));
                transfer.setToId(rs.getInt(4));
                transfer.setAmount(rs.getDouble(5));
                transfers.add(transfer);
            }
        } catch (SQLException throwables) {
            logger.fatal("Unable to read from database");
        }
        return transfers;
    }
}
