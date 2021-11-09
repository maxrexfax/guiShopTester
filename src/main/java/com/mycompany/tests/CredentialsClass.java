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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class CredentialsClass{
    
    public String email;
    public String password;
    public String mainUrl;
    
    
    public CredentialsClass() throws FileNotFoundException, IOException {
//        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        String appConfigPath = rootPath + "baseConf.cfg";
//        String catalogConfigPath = rootPath + "catalog";
        boolean isSettingsFileFound = false;
        Properties baseConf = new Properties();
        //baseConf.load(new FileInputStream(appConfigPath));
        try{
            baseConf.load(new FileReader("baseConf.cfg"));
            isSettingsFileFound = true;
        } catch(FileNotFoundException fex) {
            
        } catch(IOException iex) {
            
        }
        
        if (isSettingsFileFound) {
            this.email = baseConf.getProperty("emailToLogin", "enducareadmin@arproactive.com");
            this.password = baseConf.getProperty("password", "Vn@rh1111");
            this.mainUrl = baseConf.getProperty("mainUrl", "http://localhost:7370/#/providers/login");
        } else {
            createConfigFile();
            this.email = "enducareadmin@arproactive.com";
            this.password = "Vn@rh1111";
            this.mainUrl = "http://localhost:7370/#/providers/login";
        }
    }
    
    public void SetMainUrl(String urlIN) {
        this.mainUrl = urlIN;
    }
    
    public void SetEmailToLogin(String text) {
        this.email = text;
    }
    
    public void SetPasswordToLogin(String text) {
        this.password = text;
    }
    
    public String getMainUrl() {
        return this.mainUrl;
    }
    
    public String getEmailToLogin(){
        return this.email;
    }
    
    public String getPasswordToLogin(){
        return this.password;
    }
    
    public void createConfigFile() {  
    try {
      File baseConfigFile = new File("baseConf.cfg");
      FileWriter myWriter = new FileWriter("baseConf.cfg");
      myWriter.write("info=Please Fill this file with correct data\nemailToLogin=enducareadmin@arproactive.com\npassword=Vn@rh1111\nmainUrl=http://localhost:7370/#/providers/login");
      myWriter.close();
      
//      BufferedWriter writer = new BufferedWriter(new FileWriter("baseConf.cfg", true));
//      writer.write("emailToLogin=admin@mail.com");
//      writer.newLine();
//      writer.write("emailToLogin=admin@mail.com");
//      writer.newLine();
//      writer.close();
      if (baseConfigFile.createNewFile()) {
        System.out.println("File created: " + baseConfigFile.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }    
  }
            
}