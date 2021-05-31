/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tests;

/**
 *
 * @author maxim
 */
import com.mycompany.tests.CredentialsClass;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CredentialsClass{
    
    public String email;
    public String password;
    
    
    public CredentialsClass() throws FileNotFoundException, IOException{
//        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        String appConfigPath = rootPath + "baseConf.cfg";
//        String catalogConfigPath = rootPath + "catalog";

        Properties baseConf = new Properties();
        //baseConf.load(new FileInputStream(appConfigPath));
        baseConf.load(new FileReader("baseConf.cfg"));


        this.email = baseConf.getProperty("emailToLogin", "admin@mail.com");
        this.password = baseConf.getProperty("password", "123456");
    }
    
    public void SetEmailToLogin(String text) {
        this.email = text;
    }
    
    public void SetPasswordToLogin(String text) {
        this.password = text;
    }
    
    public String getEmailToLogin(){
        return this.email;
    }
    
    public String getPasswordToLogin(){
        return this.password;
    }
            
}