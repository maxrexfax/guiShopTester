/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tests;

import com.mycompany.shoptester.MainJFrame;
import java.io.File;
import javax.swing.JProgressBar;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author maxrexfax
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
    private JProgressBar jProgressBar;
    
    public CrudLocalesClass(String pathToFileFolderIn, String osNameIn, boolean isDeleteLocale, JProgressBar jProgressBarIn, CredentialsClass credentialsClassIn){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.isLocaleDeleteAfterCreation = isDeleteLocale;
        this.jProgressBar = jProgressBarIn;
        this.credentialsClass = credentialsClassIn;
        helperClass.setProgressBarValue(1, this.jProgressBar);
    }
    
    public void crudTestOfLocales() {
        String localeName = "Test locale name";
        String localeCode = "TcodeT";
        String appendixToAdd = "_tmp";
        String pathToImageFolder = "/home/user/Downloads/";
        String logoName = "upload.jpg";
        String logoNameAlt = "upload(1).jpg";
        String fullPath = pathToImageFolder + logoName;
        
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
        helperClass.setProgressBarValue(2, this.jProgressBar);
        try {
            if(MainJFrame.CURRENT_BROWSER == MainJFrame.CHANGE_CHROME_BROWSER) {
                webDriver = new ChromeDriver();
            } else {
                webDriver = new FirefoxDriver();
            }
            //login to site START
            js = (JavascriptExecutor)webDriver;
            webDriver.manage().window().maximize();      
            
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
            
            
            //TEST CREATION OF LOCALE
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - create locale"); 
            fillDataAndSave(localeName, localeCode, fullPath);
            helperClass.setProgressBarValue(5, this.jProgressBar);
            
            //SEARCH CREATED LOCALE
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - SEARCH locale"); 
            Thread.sleep(500);
            int[] arrWithIdAndPagination = new int[2];
            
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "admin/locales/list", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "admin/locales/list!!! Return...", "Empty");
                return;
            }
            
            arrWithIdAndPagination = helperClass.getIdAndPaginationNumberOfModelOnPage(localeName, 2, "tableWithLocalesData", "id", "page-item", webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            Thread.sleep(500);
            
            Thread.sleep(500);
            helperClass.setProgressBarValue(6, this.jProgressBar);
                       
            //EDIT CREATED LOCALE
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - EDIT locale"); 
            webDriver.get(mainUrl + "admin/locales/list?page=" + arrWithIdAndPagination[1]);
            Thread.sleep(500); 
            helperClass.clickOnEditButtonByModelId(arrWithIdAndPagination[0], 4, "tableWithLocalesData", "id", null, webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            editDataInFoundElement(localeName, localeCode, pathToImageFolder, logoNameAlt, appendixToAdd);
            Thread.sleep(500); 
            webDriver.get(mainUrl + "admin/locales/list?page=" + arrWithIdAndPagination[1]);
            Thread.sleep(500); 
            helperClass.clickOnEditButtonByModelId(arrWithIdAndPagination[0], 4, "tableWithLocalesData", "id", null, webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            helperClass.setProgressBarValue(7, this.jProgressBar);
            
            checkData(arrWithIdAndPagination, localeName, localeCode, appendixToAdd);
            //DELETE CREATED LOCALE
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - DELETE locale"); 
            Thread.sleep(500); 
            helperClass.deleteModelById(webDriver, arrWithIdAndPagination, "tableWithLocalesData", this.isLocaleDeleteAfterCreation, fileToWriteLogsOfTesting, 5);
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
    
    private void fillDataAndSave(String localeName, String localeCode, String pathToFileToUpload) throws InterruptedException {
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

    private void editDataInFoundElement(String localeName, String localeCode, String pathToImageFolder, String logoNameAlt, String appendix) throws InterruptedException {
        helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: Try to edit filled data");
        helperClass.editDataInTextInputWithLabel(webDriver, localeName + appendix, "id", "locale_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(3) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);  
        helperClass.editDataInTextInputWithLabel(webDriver, localeCode + appendix, "id", "locale_code", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200); 
        WebElement fileInput = webDriver.findElement(By.id("store_logo"));
        fileInput.sendKeys(pathToImageFolder + logoNameAlt);
        Thread.sleep(200);
        webDriver.findElement(By.id("btnCreateLocale")).click();
        Thread.sleep(500); 
        
    }    
    
    private void checkData(int[] arrWithIdAndPagination, String localeName, String localeCode, String appendixToAdd) throws InterruptedException {
        helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: Try to find after editing data appendix " + helperClass.leftDemarkator + appendixToAdd + helperClass.rightDemarkator);
        
        Thread.sleep(500);
        if(helperClass.checkInputContent(webDriver, localeName, appendixToAdd, "id", "locale_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(3) > label", fileToWriteLogsOfTesting)) {
             helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: locale name edited successfully!");
        } else {
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: login editing not complete", "Error");
        }
        Thread.sleep(200); 
        
        if(helperClass.checkInputContent(webDriver, localeCode, appendixToAdd, "id", "locale_code", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting)) {
             helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: locale code edited successfully!");
        } else {
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: login editing not complete", "Error");
        }
        Thread.sleep(200); 
        webDriver.findElement(By.id("btnCreateLocale")).click();
        Thread.sleep(500); 
    }

    private void goThroughMenuToCreation() throws InterruptedException {
        webDriver.get("http://shop.loc/product/category/1");
        Thread.sleep(500);
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
    }
}
