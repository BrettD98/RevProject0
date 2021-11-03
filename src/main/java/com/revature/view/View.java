package com.revature.view;

import com.revature.dao.UserDAO;
import com.revature.model.Account;
import com.revature.model.Transfer;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.TransferService;
import com.revature.service.UserService;
import com.revature.util.AccountNotAccepted;
import com.revature.util.Factory;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class View {
    static Logger logger = org.apache.log4j.Logger.getLogger(View.class);
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final AccountService accountService;
    private final TransferService transferService;

    private User currentUser;


    public View() {
        userService = Factory.getUserService();
        accountService = Factory.getAccountService();
        transferService = Factory.getTransferService();
    }

    /**
     * Welcome screen, just used for looks
     * calls preLoginMenu()
     */
    public void welcomeScreen() {
        System.out.println("**********************************");
        System.out.println("\tWelcome to the Bread Bank");
        System.out.println("**********************************");
        preLoginMenu();
    }

    /**
     * Loops indefinitely for usability
     * Waits for user to login() or register()
     * then calls mainMenuEmployee() or mainMenuUser() if user is employee or user respectively
     */
    private void preLoginMenu() {
        while(true) {
            while (currentUser == null) {
                System.out.println("Please 1) Login or 2) Register:");
                switch (scanner.nextLine()) {
                    case "1":
                        loginMenu();
                        break;
                    case "2":
                        registerMenu();
                        break;
                    default:
                        System.out.println("Please choose a valid option.");
                }
            }
            System.out.println("Successful Login");
            if (currentUser.isEmployee()) {
                mainMenuEmployee();
            } else {
                mainMenuUser();
            }
            currentUser = null;
        }
    }

    /**
     * Calls the Factory.newUser to receive a new user and then populates user data with scanner input
     * then calls userService to compare login credentials.
     */
    private void loginMenu() {
        User user = Factory.newUser();

        System.out.print("Email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Password: ");
        user.setPassword(scanner.nextLine());

        currentUser = userService.getUserByEmail(user);
    }

    /**
     * Calls the Factory.newUser to receive a new user and then populates user data with scanner input
     * then calls userService to insert into the database.
     */
    private void registerMenu() {
        User user = Factory.newUser();

        System.out.print("First Name: ");
        user.setFirstName(scanner.nextLine());
        System.out.print("Last Name: ");
        user.setLastName(scanner.nextLine());
        System.out.print("Email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Password: ");
        user.setPassword(scanner.nextLine());

        currentUser = userService.insertUser(user);
    }

    /**
     * Employee's Main Menu
     * Using a scanner input the employee chooses 5 options and calls the respective accountService method or view method
     */
    private void mainMenuEmployee() {
        while(currentUser!= null){
            System.out.println("1) See Unapproved Accounts\n" +
                    "2) View User Accounts\n" +
                    "3) View All Transactions\n" +
                    "4) User Menu\n" +
                    "5) Logout");
            switch (scanner.nextLine()){
                case "1":
                    employeeAccountMenu();
                    break;
                case "2":
                    System.out.println("User Email: ");
                    for(Account a : accountService.getAccountByEmail(scanner.nextLine())){
                        System.out.println(a.toString());
                    }
                    break;
                case "3":
                    for(Transfer t : transferService.showTransfers()){
                        System.out.println(t.toString());
                    }
                    break;
                case "4":
                    mainMenuUser();
                    break;
                case "5":
                    currentUser = null;
                default:
                    System.out.println("Please input a valid choice.");
            }
        }
    }

    /**
     * The account menu for an employee to view unapproved accounts and approve or reject them using accountService methods
     */
    private void employeeAccountMenu() {
        for(Account a : accountService.getUnapporvedAccounts()){
            System.out.println(a.toString());
        }
        System.out.println("1) Approve account\n" +
                "2) Reject Account\n" +
                "3) Back");
        switch (scanner.nextLine()){
            case "1":
                System.out.println("Account number to approve.");
                accountService.approveAccount(scanner.nextLine());
                break;
            case "2":
                System.out.println("Account to reject.");
                accountService.removeAccount(scanner.nextLine());
                break;
            case "3":
                break;
            default:
                System.out.println("Please input a valid choice.");
                break;
        }
    }

    /**
     * Main Menu for general Users.
     * Using 4 options the user can view accounts, apply for accounts, view a single account and logout back to the preLoginMenu
     */
    private void mainMenuUser() {
        while(currentUser != null){
            System.out.println("1) View Accounts.\n" +
                    "2) Apply for an account.\n" +
                    "3) View single account.\n" +
                    "0) Logout");
            switch (scanner.nextLine()){
                case "1":
                    accountsMenu();
                    break;
                case "2":
                    applyMenu();
                    break;
                case "3":
                    singleAccountMenu();
                    break;
                case "0":
                    currentUser = null;
                    break;
                default:
                    System.out.println("Please input an accepted choice.");
                    break;
            }
        }
    }

    /**
     * helper method to print out all accounts registered to one email
     */
    private void accountsMenu() {
        for(Account a : accountService.getAccountByEmail(currentUser.getEmail())){
            System.out.println(a.toString() + "\n");
        }
    }

    /**
     * Using the scanner, the user builds an account object that is saved to the database
     */
    private void applyMenu() {
        Account account = Factory.newAccount();

        account.setEmail(currentUser.getEmail());
        System.out.println("How much do you want to deposit.");
        String in = scanner.nextLine();
        try{
            account.setBalance(Double.parseDouble(in));

            accountService.insertAccount(account);
            System.out.println("Successfully applied for account.");
        }catch(NumberFormatException e){
            System.out.println("Please input a valid amount.");
        }
    }

    /**
     * The User can view a specific account and withdraw, deposit or view transactions on that account
     */
    private void singleAccountMenu() {
        System.out.println("Input account number to view: ");
        String in = scanner.nextLine();
        Account account;

        try{
            account = accountService.selectAccount(Integer.parseInt(in));
            System.out.println(account);
            if(!account.isAccepted()){
                throw new AccountNotAccepted();
            }
            System.out.println("1) Deposit\n" +
                    "2) Withdraw\n" +
                    "3) Transfer\n" +
                    "4) View Transfers to this account");
            switch(scanner.nextLine()){
                case "1":
                    System.out.print("How much to deposit: ");
                    accountService.update(scanner.nextLine(), account);
                    break;
                case "2":
                    System.out.print("How much to withdraw: ");
                    String s = scanner.nextLine();
                    if(!s.startsWith("-")){
                        s = "-" + s;
                    }
                    accountService.update(s, account);
                    break;
                case "3":
                    System.out.print("How much to transfer: ");
                    String amount = scanner.nextLine();
                    System.out.print("Which account to transfer to: ");
                    String accountTo = scanner.nextLine();
                    transferService.newTransfer(amount, accountTo, account);
                    System.out.println("Transfer Posted");
                    break;
                case "4":
                    transferMenu(account);
                    break;
                default:
                    System.out.println("Please choose a valid option.");
                    break;
            }
            account = accountService.selectAccount(Integer.parseInt(in));
        }catch(NumberFormatException e){
            System.out.println("Please input a valid account.");
        } catch (AccountNotAccepted accountNotAccepted) {
            System.out.println("This account has not been accepted by an employee, please wait to use this account.");
        }

    }

    /**
     * Shows all transfers to the inputted account and allows the user to accept or reject them
     * @param account The account to view
     */
    private void transferMenu(Account account) {
        for(Transfer t : transferService.getTransfers(account)){
            if(!t.isAccepted()){
                System.out.println(t);
            }
        }
        Transfer t;
        System.out.println("1) Accept Transfer\n" +
                "2) Reject Transfers\n" +
                "3) Exit");
        switch (scanner.nextLine()){
            case "1":
                System.out.print("Which transfer to accept:");
                t = transferService.getSingleTransfer(scanner.nextLine());
                if(!t.isAccepted()) {
                    transferService.acceptTransfer(t);
                    accountService.update(Double.toString(t.getAmount()), accountService.selectAccount(t.getToId()));
                    accountService.update(Double.toString(-t.getAmount()), accountService.selectAccount(t.getFromId()));
                }
                break;
            case "2":
                System.out.print("Which transfer to reject:");
                t = transferService.getSingleTransfer(scanner.nextLine());
                transferService.deleteTransfer(t);
                break;
            case "3":
                break;
            default:
                System.out.println("Please input a valid choice.");
                break;
        }
    }








    
}
