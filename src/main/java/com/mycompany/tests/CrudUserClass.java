/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tests;

import com.mycompany.shoptester.MainJFrame;
import java.io.File;
import java.util.List;
import java.util.UUID;
import org.openqa.selenium.Alert;
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
public class CrudUserClass {
    
    private JavascriptExecutor js;
    private HelperClass helperClass = new HelperClass();
    private CredentialsClass credentialsClass;
    private String dateTimeOfSession;
    private String pathToLogFileFolder;
    private File fileToWriteLogsOfTesting;
    private File fileToWriteErrorLogOfTesting;
    private WebDriver webDriver = null;
    private String mainUrl = "http://shop.loc/";
    private String testUrl = "http://shop.loc/admin/users/list/";
    private String osName;
    private final int countOfSymbols = 15;
    private boolean isUserDeleteAfterCreation; 
    
    public CrudUserClass(String pathToFileFolderIn, String osNameIn, boolean isDeleteUser){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.isUserDeleteAfterCreation = isDeleteUser;
    }
    
    public void startCrudTestUsers() {
        
        String userLogin = "" + helperClass.getRandomStringWithLength(countOfSymbols);
        userLogin = userLogin.replace("-", "");
        String firstName = "FN" + helperClass.getRandomStringWithLength(countOfSymbols);
        firstName = firstName.replace("-", "");
        String secondName = "SN" + helperClass.getRandomStringWithLength(countOfSymbols);
        secondName = secondName.replace("-", "");
        String lastName = "LN" + helperClass.getRandomStringWithLength(countOfSymbols);
        lastName = lastName.replace("-", "");
        String emailToUse = helperClass.getRandomStringWithLength(countOfSymbols / 2) + "@mail.com";
        emailToUse = emailToUse.replace("-", "");
        
        //fullName = lastName + ", " + firstName;
        credentialsClass = new CredentialsClass();
        dateTimeOfSession = helperClass.getDateInStringForWindowsLinux();    
        String fileName = "";
        String fileNameERRORS = "";
        
        fileName = this.pathToLogFileFolder + "testUserCrudLogFile_" + dateTimeOfSession + ".txt";
        fileNameERRORS = this.pathToLogFileFolder + "testUserCrudLogFile_ERRORS_" + dateTimeOfSession + ".txt";        
        
        try {
            fileToWriteLogsOfTesting = new File(fileName);
            fileToWriteErrorLogOfTesting = new File(fileNameERRORS);
            System.out.println("Path to logfile:" + fileName);
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
            System.out.println("Error file creation, test log will be only in terminal");
        }
        
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "User create, edit, delete test starts at: " + dateTimeOfSession +" OS: " + osName);
        
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
            
            webDriver.findElement(By.id("adminConrol")).click();
            Thread.sleep(300); 
            webDriver.findElement(By.id("management")).click();
            Thread.sleep(300); 
            webDriver.findElement(By.id("managementUsers")).click();
            Thread.sleep(300); 
            webDriver.findElement(By.id("userCreateButton")).click();
            Thread.sleep(300);  
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "user/create", fileToWriteLogsOfTesting);
            Thread.sleep(500);  
            
            try {                
                fillUserDataAndSave(userLogin, firstName, secondName, lastName, emailToUse);
                Thread.sleep(300); 
            } catch (Exception ex) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Unable to fill user data", ex.getMessage());
            }          
            
            Thread.sleep(500);            
            
            int[] arrWithIdAndPagination = new int[2];

            if (helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "admin/users/list", fileToWriteLogsOfTesting)) {
                try {
                    arrWithIdAndPagination = helperClass.getIdAndPaginationNumberOfModelOnPage(userLogin, "tableWithUsersData", "id", "page-item", webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
                    Thread.sleep(300); 
                } catch (Exception ex) {
                    helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Unable to check saving user data", ex.getMessage());
                } 
            } else {
                helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: Error - not on page with users list");
            }                        
            Thread.sleep(500);       
            clickOnUserEditButton(arrWithIdAndPagination);
            Thread.sleep(500);  
            helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "user/edit/" + arrWithIdAndPagination[0], fileToWriteLogsOfTesting);
            Thread.sleep(500);
            String appendixToAdd = "tmp";
            Thread.sleep(500);
            editSavedUserData(userLogin, firstName, secondName, lastName, emailToUse, appendixToAdd);
            Thread.sleep(500);
            
            checkUserData(arrWithIdAndPagination, userLogin, firstName, secondName, lastName, emailToUse, appendixToAdd);
            Thread.sleep(500);
            webDriver.get(mainUrl + "admin/users/list?page=" + arrWithIdAndPagination[1]);
            Thread.sleep(500);
            js.executeScript("window.scrollBy(0,245)");
            Thread.sleep(500);
            helperClass.deleteModelById(webDriver, arrWithIdAndPagination, "tableWithUsersData", this.isUserDeleteAfterCreation, fileToWriteLogsOfTesting, 7);
            
            Thread.sleep(1500); 
            helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: END");
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            helperClass.printToFileAndConsoleInformation(fileToWriteErrorLogOfTesting, "ERROR: Error in main try block of CrudUserClass"); 
        } finally {
            webDriver.close();
            webDriver.quit();
        }
    }   

    private void fillUserDataAndSave(String userLogin, String firstName, String secondName, String lastName, String emailToUse) throws InterruptedException {
        helperClass.editDataInTextInputWithLabel(webDriver, userLogin, "id", "login", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(3) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);  
        helperClass.editDataInTextInputWithLabel(webDriver, firstName, "id", "first_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200); 
        helperClass.editDataInTextInputWithLabel(webDriver, secondName, "id", "second_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(5) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200); 
        helperClass.editDataInTextInputWithLabel(webDriver, lastName, "id", "last_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(6) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200); 
        helperClass.editDataInTextInputWithLabel(webDriver, emailToUse, "id", "email", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(7) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);
        helperClass.editDataInTextInputWithLabel(webDriver, "123456", "id", "password", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(8) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);
        webDriver.findElement(By.id("userCreateEditBtn")).click();
    }    

    private void editSavedUserData(String userLogin, String firstName, String secondName, String lastName, String emailToUse, String appendix) throws InterruptedException {
        helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: Try to edit user data");
        helperClass.editDataInTextInputWithLabel(webDriver, userLogin + appendix, "id", "login", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(3) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);  
        helperClass.editDataInTextInputWithLabel(webDriver, firstName + appendix, "id", "first_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200); 
        helperClass.editDataInTextInputWithLabel(webDriver, secondName + appendix, "id", "second_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(5) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200); 
        helperClass.editDataInTextInputWithLabel(webDriver, lastName + appendix, "id", "last_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(6) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200); 
        helperClass.editDataInTextInputWithLabel(webDriver, emailToUse + appendix, "id", "email", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(7) > label", fileToWriteLogsOfTesting);
        Thread.sleep(200);
        webDriver.findElement(By.id("userCreateEditBtn")).click();
        Thread.sleep(500); 
    }
    
    private void clickOnUserEditButton(int[] arrayIdAndPagination) throws InterruptedException {
        js.executeScript("window.scrollBy(0,245)");
        if(arrayIdAndPagination[0] != 0) {
            webDriver.get(mainUrl + "admin/users/list?page=" + arrayIdAndPagination[1]);
            Thread.sleep(500);
            WebElement editButton = webDriver.findElement(By.id("edit" + arrayIdAndPagination[0]));
            editButton.click();
            Thread.sleep(500);
        }
    }
    
    private void checkUserData(int[] arrWithIdAndPagination, String userLogin, String firstName, String secondName, String lastName, String emailToUse, String appendixToAdd) throws InterruptedException {
        helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: Try to find after editing user data appendix " + helperClass.leftDemarkator + appendixToAdd + helperClass.rightDemarkator);
        webDriver.get(mainUrl + "user/edit/" + arrWithIdAndPagination[0]);
        Thread.sleep(500);
        if(helperClass.checkInputContent(webDriver, userLogin, appendixToAdd, "id", "login", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(3) > label", fileToWriteLogsOfTesting)) {
             helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: login edited successfully!");
        } else {
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: login editing not complete", "Error");
        }
        Thread.sleep(200); 
        
        if(helperClass.checkInputContent(webDriver, firstName, appendixToAdd, "id", "first_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(4) > label", fileToWriteLogsOfTesting)) {
             helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: first Name edited successfully!");
        } else {
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: login editing not complete", "Error");
        }
        Thread.sleep(200); 
        
        if(helperClass.checkInputContent(webDriver, secondName, appendixToAdd, "id", "second_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(5) > label", fileToWriteLogsOfTesting)) {
             helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: second Name edited successfully!");
        } else {
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: login editing not complete", "Error");
        }
        Thread.sleep(200); 
        
        if(helperClass.checkInputContent(webDriver, lastName, appendixToAdd, "id", "last_name", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(6) > label", fileToWriteLogsOfTesting)) {
             helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: last Name edited successfully!");
        } else {
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: login editing not complete", "Error");
        }
        Thread.sleep(200); 
        
        if(helperClass.checkInputContent(webDriver, emailToUse, appendixToAdd, "id", "email", "cssSelector", "#app > main > div > div > div > div > div > div.col-9.p-0.bg-secondary > main > div > div > div > div > div.d-flex.justify-content-between.flex-wrap > div > div.card-body > form > div:nth-child(7) > label", fileToWriteLogsOfTesting)) {
             helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: email edited successfully!");
        } else {
            helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: login editing not complete", "Error");
        }
        Thread.sleep(200); 
        webDriver.findElement(By.id("userCreateEditBtn")).click();
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
    
    
}
