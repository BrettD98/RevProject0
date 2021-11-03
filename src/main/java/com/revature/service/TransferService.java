package com.revature.service;

import com.revature.dao.TransferDAO;
import com.revature.model.Account;
import com.revature.model.Transfer;
import com.revature.util.Factory;
import org.apache.log4j.Logger;

import java.util.List;

public class TransferService {
    static Logger logger = org.apache.log4j.Logger.getLogger(AccountService.class);
    private final TransferDAO dao;

    public TransferService() {
        this.dao = Factory.getTransferDAO();
    }


    public void newTransfer(String amount, String accountTo, Account account) {
        try{
            Transfer newTransfer = Factory.newTransfer();

            newTransfer.setAmount(Double.parseDouble(amount));
            newTransfer.setToId(Integer.parseInt(accountTo));
            newTransfer.setFromId(account.getAccountId());

            dao.insert(newTransfer);
        }catch(NumberFormatException e){
            logger.error("Please input a valid amount / account number.");
        }
    }

    public List<Transfer> getTransfers(Account account) {
        return dao.selectByAccount(account.getAccountId());
    }

    public Transfer getSingleTransfer(String in) {

        try{
            Integer.parseInt(in);
            return dao.select(in);
        }catch(NumberFormatException e){
            logger.error("Please input a valid id number");
        }
        return null;
    }

    public void acceptTransfer(Transfer t) {
        dao.update(t);
    }

    public void deleteTransfer(Transfer t) {
        dao.delete(t);
    }

    public List<Transfer> showTransfers() {
        return dao.selectAll();
    }
}
