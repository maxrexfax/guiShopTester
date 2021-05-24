/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tests;

import com.mycompany.shoptester.MainJFrame;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author user
 */
public class CrudLocalesClass {
    
    private JavascriptExecutor js;
    private HelperClass helperClass = new HelperClass();
    private CredentialsClass credentialsClass;
    private String dateTimeOfSession;
    private String pathToLogFileFolder;
    private File fileToWriteLogsOfTesting;
    private File fileToWriteErrorLogOfTesting;
    private WebDriver webDriver = null;
    private String mainUrl = "http://shop.loc/";
    private String testUrl = "http://shop.loc/admin/locales/list/";
    private String osName;
    private final int countOfSymbols = 15;
    private boolean isLocaleDeleteAfterCreation; 
    
    public CrudLocalesClass(String pathToFileFolderIn, String osNameIn, boolean isDeleteLocale){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.isLocaleDeleteAfterCreation = isDeleteLocale;
    }
    
    public void crudTestOfLocales() {
        String localeName = "Test locale name";
        String localeCode = "TcodeT";
        String pathToImageFolder = "/home/user/Downloads/";
        String logoName = "upload.jpg";
        String fullPath = pathToImageFolder + logoName;
        
        credentialsClass = new CredentialsClass();
        dateTimeOfSession = helperClass.getDateInStringForWindowsLinux();    
        String fileName = "";
        String fileNameERRORS = "";
        
        fileName = this.pathToLogFileFolder + "testLocaleCrudLogFile_" + dateTimeOfSession + ".txt";
        fileNameERRORS = this.pathToLogFileFolder + "testLocaleCrudLogFile_ERRORS_" + dateTimeOfSession + ".txt";        
        
        try {
            fileToWriteLogsOfTesting = new File(fileName);
            fileToWriteErrorLogOfTesting = new File(fileNameERRORS);
            System.out.println("Path to logfile:" + fileName);
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
            System.out.println("Error file creation, test log will be only in terminal");
        }
        
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Locale create, edit, delete test starts at: " + dateTimeOfSession +" OS: " + osName);
        try {
            if(MainJFrame.CURRENT_BROWSER == MainJFrame.CHANGE_CHROME_BROWSER) {
                webDriver = new ChromeDriver();
            } else {
                webDriver = new FirefoxDriver();
            }
            //login to site START
            js = (JavascriptExecutor)webDriver;
            webDriver.manage().window().maximize();            
            startAndLoginToSite(webDriver, fileToWriteLogsOfTesting, mainUrl);           
            Thread.sleep(500);
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "home", fileToWriteLogsOfTesting);
            
            webDriver.get("http://shop.loc/product/category/1");
            
            webDriver.findElement(By.id("adminConrol")).click();
            Thread.sleep(300); 
            webDriver.findElement(By.id("settings")).click();
            Thread.sleep(300); 
            webDriver.findElement(By.id("settingsLocales")).click();
            Thread.sleep(300); 
            webDriver.findElement(By.id("btnCreateLocale")).click();
            Thread.sleep(300);  
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "locale/create", fileToWriteLogsOfTesting);
            Thread.sleep(500);  
            
            fillUserDataAndSave(localeName, localeCode, fullPath);
            
            Thread.sleep(500);
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "admin/locales/list", fileToWriteLogsOfTesting);
            
            //findTestCreatedLocale();
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            helperClass.printToFileAndConsoleInformation(fileToWriteErrorLogOfTesting, "ERROR: Error in main try block of CrudUserClass"); 
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
    
    private void fillUserDataAndSave(String localeName, String localeCode, String pathToFileToUpload) throws InterruptedException {
        helperClass.editDataInTextInputWithLabel(webDriver, localeName, "id", "locale_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(3) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);  
        helperClass.editDataInTextInputWithLabel(webDriver, localeCode, "id", "locale_code", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);  
        WebElement fileInput = webDriver.findElement(By.id("store_logo"));
        fileInput.sendKeys(pathToFileToUpload);
        Thread.sleep(200);  
        webDriver.findElement(By.id("btnCreateLocale")).click();
        Thread.sleep(500); 
    } 

    private void findTestCreatedLocale() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
