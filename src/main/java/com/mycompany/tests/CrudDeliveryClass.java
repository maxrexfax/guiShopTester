/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tests;

import com.mycompany.shoptester.MainJFrame;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import junit.awtui.ProgressBar;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author maxrexfax
 */
public class CrudDeliveryClass {
    
    private JavascriptExecutor js;
    private HelperClass helperClass = new HelperClass();
    private CredentialsClass credentialsClass;
    private String dateTimeOfSession;
    private String pathToLogFileFolder;
    private File fileToWriteLogsOfTesting;
    private File fileToWriteErrorLogOfTesting;
    private WebDriver webDriver = null;
    private String mainUrl = "http://shop.loc/";
    private String testUrl = "http://shop.loc/admin/deliveries/list/";
    private String osName;
    private final int countOfSymbols = 15;
    private boolean isLocaleDeleteAfterCreation; 
    private JProgressBar jProgressBar;
    
    public CrudDeliveryClass(String pathToFileFolderIn, String osNameIn, boolean isDeleteLocale, JProgressBar jProgressBarIn){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.isLocaleDeleteAfterCreation = isDeleteLocale;
        this.jProgressBar = jProgressBarIn;
        //frame.setTextInAreaInformation.setText("CrudDeliveryClass Constructor worked");
    }
    
//    public void setTextInAreaInformation(int complete, JProgressBar jProgressBar) {
//        new Thread(new Runnable() {
//            public void run() {  
//                try {
//                    final int fComplete = complete;
//                    SwingUtilities.invokeLater(new Runnable() {
//                        public void run() {
//                          jProgressBar.setValue(fComplete);
//                        }
//                    });              
//                }
//                catch(Exception e) { }
//            }
//        }).start();        
//    }
    
    public void crudTestOfDeliveries() {
        helperClass.setProgressBarValue(1, this.jProgressBar);
        String deliveryName = "Test delivery name";
        String deliveryDescription = "Test delivery Description. Text text text text text text text";
        double deliveryPrice = 22.0;
        String appendixToAdd = "_tmp";
        String pathToImageFolder = "/home/user/Downloads/";
        String logoName = "dlogo.jpg";
        String logoNameAlt = "dlogo(1).jpg";
        String fullPath = pathToImageFolder + logoName;
        
        credentialsClass = new CredentialsClass();
        dateTimeOfSession = helperClass.getDateInStringForWindowsLinux();    
        String fileName = "";
        String fileNameERRORS = "";
        
        fileName = this.pathToLogFileFolder + "testDeliveryCrudLogFile_" + dateTimeOfSession + ".txt";
        fileNameERRORS = this.pathToLogFileFolder + "testDeliveryCrudLogFile_ERRORS_" + dateTimeOfSession + ".txt";  
        try {
            fileToWriteLogsOfTesting = new File(fileName);
            fileToWriteErrorLogOfTesting = new File(fileNameERRORS);
            System.out.println("Path to logfile:" + fileName);
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
            System.out.println("Error file creation, test log will be only in terminal");
        }
        helperClass.setProgressBarValue(2, this.jProgressBar);
        
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Delivery create, edit, delete test starts at: " + dateTimeOfSession +" OS: " + osName);
        try {
            if(MainJFrame.CURRENT_BROWSER == MainJFrame.CHANGE_CHROME_BROWSER) {
                webDriver = new ChromeDriver();
            } else {
                webDriver = new FirefoxDriver();
            }
            //login to site START
            js = (JavascriptExecutor)webDriver;
            webDriver.manage().window().maximize(); 
            //driver.manage().window().setPosition(new Point(0, -2000));
            
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(68);
            robot.keyRelease(68);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            robot.keyRelease(KeyEvent.VK_CONTROL);
//            robot.keyPress(KeyEvent.VK_ALT);
//robot.keyPress(KeyEvent.VK_SPACE);
//robot.keyPress(KeyEvent.VK_N);
//robot.keyRelease(KeyEvent.VK_ALT);
//robot.keyRelease(KeyEvent.VK_SPACE);
//robot.keyRelease(KeyEvent.VK_N);
//            robot.mouseMove(630, 420); // move mouse point to specific location	
//            robot.delay(1500);        // delay is to make code wait for mentioned milliseconds before executing next step	
//            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // press left click	
//            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // release left click	
//            robot.delay(1500);	
//            robot.keyPress(KeyEvent.VK_DOWN); // press keyboard arrow key to select Save radio button	
//            Thread.sleep(2000);	
//            robot.keyPress(KeyEvent.VK_ENTER);	

            helperClass.setProgressBarValue(3, this.jProgressBar);
            //LOGIN TO SITE
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - Login"); 
            startAndLoginToSite(webDriver, fileToWriteLogsOfTesting, mainUrl);           
            Thread.sleep(500);
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "home", fileToWriteLogsOfTesting);
            
            goThroughMenuToCreation();
            Thread.sleep(500);
            
            helperClass.setProgressBarValue(5, this.jProgressBar);
            //TEST CREATION OF DELIVERY
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - create delivery"); 
            fillDataAndSave(deliveryName, deliveryDescription, deliveryPrice, fullPath);
            
            
            //SEARCH CREATED DELIVERY
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - SEARCH delivery"); 
            Thread.sleep(500);
            helperClass.setProgressBarValue(8, this.jProgressBar);
            int[] arrWithIdAndPagination = new int[2];
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "admin/deliveries/list", fileToWriteLogsOfTesting);
            arrWithIdAndPagination = helperClass.getIdAndPaginationNumberOfModelOnPage(deliveryName, 1, "tableWithDeliveriesData", "id", "page-item", webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            Thread.sleep(500);
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "admin/deliveries/list", fileToWriteLogsOfTesting); 
            Thread.sleep(500);
            
            
            
            //TEST EDIT OF DELIVERY
            
            
            
            
            
            helperClass.setProgressBarValue(10, this.jProgressBar);
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            helperClass.printToFileAndConsoleInformation(fileToWriteErrorLogOfTesting, "ERROR: Error in main try block of CrudDeliveryClass");
            helperClass.setProgressBarValue(0, this.jProgressBar);
        } finally {
            webDriver.close();
            webDriver.quit();
        }
        
    }
    
    private void startAndLoginToSite(WebDriver webDriver, File fileToWriteLogsOfTesting, String mainUrl) throws InterruptedException {
        helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: go to url:" + mainUrl);
        webDriver.get(mainUrl);
        Thread.sleep(500);
        webDriver.findElement(By.cssSelector("#navbarSupportedContent > ul.navbar-nav.ml-auto > li:nth-child(1) > a")).click();
        Thread.sleep(500);
        WebElement login = webDriver.findElement(By.id("email"));
        WebElement passwd = webDriver.findElement(By.id("password"));
        WebElement btnLogin = webDriver.findElement(By.cssSelector("#app > main > div > div > div > div > div.card-body > form > div.form-group.row.mb-0 > div > button"));
        login.sendKeys(credentialsClass.emailToLogin);
        passwd.sendKeys(credentialsClass.passwordToLogin);
        Thread.sleep(300);            
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Work: trying to login with email " + credentialsClass.emailToLogin + " and pswd " + credentialsClass.passwordToLogin);
        btnLogin.click();
    }

    private void goThroughMenuToCreation() throws InterruptedException {
        webDriver.get("http://shop.loc/product/category/1");
        Thread.sleep(500);
        webDriver.findElement(By.id("adminConrol")).click();
        Thread.sleep(300); 
        webDriver.findElement(By.id("settings")).click();
        Thread.sleep(300); 
        webDriver.findElement(By.id("settingsDeliveries")).click();
        Thread.sleep(300); 
        webDriver.findElement(By.id("btnCreateDelivery")).click();
        Thread.sleep(300);  
        helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "delivery/create", fileToWriteLogsOfTesting);
        Thread.sleep(500);  
    }

    private void fillDataAndSave(String deliveryName, String deliveryDescription, Double delivery_price, String fullPath) throws InterruptedException {
        helperClass.editDataInTextInputWithLabel(webDriver, deliveryName, "id", "delivery_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(3) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);  
        helperClass.editDataInTextInputWithLabel(webDriver, deliveryDescription, "id", "delivery_description", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);  
        helperClass.editDataInTextInputWithLabel(webDriver, delivery_price.toString(), "id", "currency_value", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);   
        Select dropdown = new Select(webDriver.findElement(By.id("selectIfDeliveryActive")));
        dropdown.selectByIndex(1);
        Thread.sleep(200);  
        webDriver.findElement(By.id("btnSaveDelivery")).click();
        Thread.sleep(500);         
    }
    
    
}
