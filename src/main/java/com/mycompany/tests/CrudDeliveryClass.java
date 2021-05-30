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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
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
    
    public CrudDeliveryClass(String pathToFileFolderIn, String osNameIn, boolean isDeleteLocale, JProgressBar jProgressBarIn, CredentialsClass credentialsClassIn){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.isLocaleDeleteAfterCreation = isDeleteLocale;
        this.jProgressBar = jProgressBarIn;
        this.credentialsClass = credentialsClassIn;
        helperClass.setProgressBarValue(1, this.jProgressBar);
    }    
   
    public void crudTestOfDeliveries() {
        String deliveryName = "Test delivery name";
        String deliveryDescription = "Test delivery Description. Text text text text text text text";
        double deliveryPrice = 22.0;
        String appendixToAdd = "_tmp";
        String pathToImageFolder = "/home/user/Downloads/";
        String logoName = "dlogo.jpg";
        String logoNameAlt = "dlogo(1).jpg";
        String fullPath = pathToImageFolder + logoName;
        
        
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
            try{
                if(MainJFrame.CURRENT_BROWSER == MainJFrame.CHANGE_CHROME_BROWSER) {
                    webDriver = new ChromeDriver();
                } else {
                    webDriver = new FirefoxDriver();
                }
            } catch(Exception | Error exe) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: in creating webDriver!", exe.getMessage());
            }
            Thread.sleep(500);

            //login to site START
            js = (JavascriptExecutor)webDriver;
            webDriver.manage().window().maximize(); 
            Thread.sleep(500);

            //LOGIN TO SITE
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - Login");
            startAndLoginToSite(webDriver, fileToWriteLogsOfTesting, mainUrl);
            helperClass.setProgressBarValue(3, this.jProgressBar);
            Thread.sleep(500);
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "home", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "home!!! Return...", "Empty");
                return;
            }
                       
            goThroughMenuToCreation();
            helperClass.setProgressBarValue(4, this.jProgressBar);
            Thread.sleep(500);
            
            //TEST CREATION OF DELIVERY
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - create delivery"); 
            fillDataAndSave(deliveryName, deliveryDescription, deliveryPrice, fullPath);
            helperClass.setProgressBarValue(5, this.jProgressBar);
            
            //SEARCH CREATED DELIVERY
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - SEARCH delivery"); 
            Thread.sleep(500);
            int[] arrWithIdAndPagination = new int[2];
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "admin/deliveries/list", fileToWriteLogsOfTesting);
            arrWithIdAndPagination = helperClass.getIdAndPaginationNumberOfModelOnPage(deliveryName, 1, "tableWithDeliveriesData", "id", "page-item", webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            helperClass.setProgressBarValue(6, this.jProgressBar);
            Thread.sleep(500);
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "admin/deliveries/list", fileToWriteLogsOfTesting); 
            Thread.sleep(500);
            
            
            
            //EDIT CREATED DELIVERY            
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - EDIT delivery"); 
            webDriver.get(mainUrl + "admin/locales/list?page=" + arrWithIdAndPagination[1]);
            Thread.sleep(500); 
            helperClass.clickOnEditButtonByModelId(arrWithIdAndPagination[0], 4, "tableWithLocalesData", "id", null, webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
//            editDataInFoundElement(localeName, localeCode, pathToImageFolder, logoNameAlt, appendixToAdd);
//            Thread.sleep(500); 
//            webDriver.get(mainUrl + "admin/locales/list?page=" + arrWithIdAndPagination[1]);
//            Thread.sleep(500); 
//            helperClass.clickOnEditButtonByModelId(arrWithIdAndPagination[0], 4, "tableWithLocalesData", "id", null, webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
//            
//            checkData(arrWithIdAndPagination, localeName, localeCode, appendixToAdd);
            helperClass.setProgressBarValue(7, this.jProgressBar);
            
            
            
            
            helperClass.setProgressBarValue(8, this.jProgressBar);
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Error in main try block of CrudDeliveryClass", ex.getMessage());
            
        } finally {
            helperClass.setProgressBarValue(0, this.jProgressBar);
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
        login.sendKeys(credentialsClass.getEmailToLogin());
        passwd.sendKeys(credentialsClass.getPasswordToLogin());
        Thread.sleep(300);            
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Work: trying to login with email " + credentialsClass.getEmailToLogin() + " and pswd " + credentialsClass.getPasswordToLogin());
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
