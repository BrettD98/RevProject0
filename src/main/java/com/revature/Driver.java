package com.revature;
import com.revature.util.Factory;
import com.revature.view.View;
import org.apache.log4j.Logger;

public class Driver {
    static Logger logger = Logger.getLogger(Driver.class);

    public static void main(String[] args){
        View view = Factory.getView();

            view.welcomeScreen();
    }
}

