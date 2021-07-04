/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tests;

import com.mycompany.shoptester.MainJFrame;
import java.io.File;
import java.util.List;
import javax.swing.JProgressBar;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author maxim
 */
public class OrderCreateClass {
    private JavascriptExecutor js;
    private HelperClass helperClass = new HelperClass();
    private CredentialsClass credentialsClass;
    private String dateTimeOfSession;
    private String pathToLogFileFolder;
    private File fileToWriteLogsOfTesting;
    private File fileToWriteErrorLogOfTesting;
    private WebDriver webDriver = null;
    private String mainUrl = "http://shop.loc/";
    //private String testUrl = "http://shop.loc/admin/locales/list/";
    private String osName;
    private final int countOfSymbols = 15; 
    private JProgressBar jProgressBar;
    
    public OrderCreateClass(String pathToFileFolderIn, String osNameIn, JProgressBar jProgressBarIn, CredentialsClass credentialsClassIn){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.jProgressBar = jProgressBarIn;
        this.credentialsClass = credentialsClassIn;
        helperClass.setProgressBarValue(1, this.jProgressBar);
    }
    
    public void testOrderCreation(){
        dateTimeOfSession = helperClass.getDateInStringForWindowsLinux();    
        String fileName = "";
        String fileNameERRORS = "";
        
        fileName = this.pathToLogFileFolder + "testOrderInCartCreationLogFile_" + dateTimeOfSession + ".txt";
        fileNameERRORS = this.pathToLogFileFolder + "testOrderInCartCreationLogFile_ERRORS_" + dateTimeOfSession + ".txt";        
        
        try {
            fileToWriteLogsOfTesting = new File(fileName);
            fileToWriteErrorLogOfTesting = new File(fileNameERRORS);
            System.out.println("Path to logfile:" + fileName);
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
            System.out.println("Error file creation, test log will be only in terminal");
        }
        
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Order in Cart creation test starts at: " + dateTimeOfSession +" OS: " + osName);
        helperClass.setProgressBarValue(2, this.jProgressBar);
        try {
            if(MainJFrame.CURRENT_BROWSER == MainJFrame.CHANGE_CHROME_BROWSER) {
                webDriver = new ChromeDriver();
            } else {
                webDriver = new FirefoxDriver();
            }
            //add product to cart
            js = (JavascriptExecutor)webDriver;
            webDriver.manage().window().maximize();  
            webDriver.get(mainUrl);
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl, fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "!!! Return...", "Empty");
                return;
            }
            
            webDriver.findElement(By.id("btnShowParentCategories")).click();
            Thread.sleep(300);
            List<WebElement> listOfCategories = webDriver.findElements(By.className("popup-root-categories-item"));
            listOfCategories.get(1).click();
            Thread.sleep(500);
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "product/category/2", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "product/category/2!!! Return...", "Empty");
                return;
            }
            
            List<WebElement> listOfProducts = webDriver.findElements(By.className("item"));
            System.out.println(listOfProducts.get(0).getSize());
            //listOfProducts.get(0).click();
            new Actions(webDriver).moveToElement(listOfProducts.get(0)).perform();
            Thread.sleep(1000);
            listOfProducts.get(0).findElement(By.className("btn-adder-to-cart")).click();
            Thread.sleep(1000);            
            new Actions(webDriver).moveToElement(listOfProducts.get(1)).perform();
            Thread.sleep(1000);
            listOfProducts.get(1).findElement(By.className("btn-adder-to-cart")).click();
            Thread.sleep(1000);
            webDriver.findElement(By.id("btnButtonCardShower")).click();
            Thread.sleep(1000);
            
            WebElement selectDeliveries = webDriver.findElement(By.id("selectTypeOfDeliveryInCart"));
            new Actions(webDriver).moveToElement(selectDeliveries).perform();
            Thread.sleep(100);
            List<WebElement> listOfOptions = selectDeliveries.findElements(By.tagName("option"));
            listOfOptions.get(1).click();
            webDriver.findElement(By.id("btnToAddPromoCode")).click();
            Thread.sleep(500);
            webDriver.findElement(By.id("promoCodeInput")).sendKeys("PROMOTEST1");
            webDriver.findElement(By.cssSelector("#divCartElement > div:nth-child(1) > div.col-md-4.col-sm-12.pt-3 > div > a")).click();
            Thread.sleep(1000);
            
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "cart/checkout", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "cart/checkout!!! Return...", "Empty");
                return;
            }
            
            
            
            
            
            
            
            helperClass.setProgressBarValue(8, this.jProgressBar);
            //
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            helperClass.printToFileAndConsoleInformation(fileToWriteErrorLogOfTesting, "ERROR: Error in main try block of CrudLocaleClass"); 
        } finally {
            helperClass.setProgressBarValue(0, this.jProgressBar);
            webDriver.close();
            webDriver.quit();
        }
    }
    
}
