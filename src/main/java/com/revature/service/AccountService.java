package com.revature.service;

import com.revature.dao.AccountDAO;
import com.revature.model.Account;
import com.revature.util.Factory;
import org.apache.log4j.Logger;

import java.util.List;

public class AccountService {
    static Logger logger = org.apache.log4j.Logger.getLogger(AccountService.class);
    private final AccountDAO dao;

    public AccountService() {
        this.dao = Factory.getAccountDao();
    }

    public Account insertAccount(Account account) {
        if(account.getEmail() != null){
            return dao.insert(account);
        }
        logger.error("Cannot insert a null object.");
        return null;
    }

    public List<Account> getAccountByEmail(String email) {
        return dao.selectByEmail(email);
    }

    public Account selectAccount(int parseInt) {
        return dao.select(Integer.toString(parseInt));
    }

    public void update(String num, Account account) {
        try{
            double i = Double.parseDouble(num);

            if(account.getBalance() + i >= 0){
                account.setBalance(account.getBalance() + i);
                dao.update(account);
            }else{
                logger.error("Cannot withdraw more than available.");
            }
        }catch(NumberFormatException e){
            logger.error("Please input a valid number.");
        }
    }

    public List<Account> getUnapporvedAccounts() {
        return dao.selectByApproved();
    }

    public void approveAccount(String in) {
        Account account = selectAccount(Integer.parseInt(in));
        account.setAccepted(true);
        dao.approve(account);
    }

    public void removeAccount(String in) {
        Account account = selectAccount(Integer.parseInt(in));
        dao.delete(account);
    }
}
