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
public class FillTestFormClass {
    
    private JavascriptExecutor js;
    private HelperClass helperClass = new HelperClass();
    private CredentialsClass credentialsClass;
    private String dateTimeOfSession;
    private String pathToLogFileFolder;
    private File fileToWriteLogsOfTesting;
    private File fileToWriteErrorLogOfTesting;
    private WebDriver webDriver = null;
    private String mainUrl;
    private String osName;
    private final int countOfSymbols = 15; 
    private JProgressBar jProgressBar;
    
    private String nameToFill, emailToFill, someTextToFill, phoneToFill, passwordToFill, passwordConfirmToFill;
    
    public FillTestFormClass(String pathToFileFolderIn, String osNameIn, boolean deleteResult, JProgressBar jProgressBarIn, CredentialsClass credentialsClassIn, String mainUrlIncome){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.jProgressBar = jProgressBarIn;
        this.credentialsClass = credentialsClassIn;
        helperClass.setProgressBarValue(1, this.jProgressBar);
        this.mainUrl = mainUrlIncome;
    }
    
    public void fillForm() {
        
        fillNecessaryData();
        dateTimeOfSession = helperClass.getDateInStringForWindowsLinux();    
        String fileName = "";
        String fileNameERRORS = "";
        
        fileName = this.pathToLogFileFolder + "testFillTestFormLogFile_" + dateTimeOfSession + ".txt";
        fileNameERRORS = this.pathToLogFileFolder + "testFillTestFormLogFile_ERRORS_" + dateTimeOfSession + ".txt";        
        
        try {
            fileToWriteLogsOfTesting = new File(fileName);
            fileToWriteErrorLogOfTesting = new File(fileNameERRORS);
            System.out.println("Path to logfile:" + fileName);
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
            System.out.println("Error file creation, test log will be only in terminal");
        }
        
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Fill test form starts at: " + dateTimeOfSession +" OS: " + osName);
        helperClass.setProgressBarValue(2, this.jProgressBar);
        
        try {
            if(MainJFrame.CURRENT_BROWSER == MainJFrame.CHANGE_CHROME_BROWSER) {
                webDriver = new ChromeDriver();
            } else {
                webDriver = new FirefoxDriver();
            }
            //fill example form
            js = (JavascriptExecutor)webDriver;
            webDriver.manage().window().maximize();  
            webDriver.get(mainUrl);
                        
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl, fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "!!! Return...", "Empty");
                return;
            }
            Thread.sleep(1000);
            webDriver.get(mainUrl + "exampleform");
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "exampleform", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "!!! Return...", "Empty");
                return;
            }
            Thread.sleep(1000);
            
            WebElement inputName = webDriver.findElement(By.id("test_name"));
            inputName.clear();
            inputName.sendKeys(nameToFill);
            Thread.sleep(100);
            
            WebElement inputEmail = webDriver.findElement(By.id("test_email"));
            inputEmail.clear();
            inputEmail.sendKeys(emailToFill);
            Thread.sleep(100);
            
            WebElement inputTextarea = webDriver.findElement(By.id("test_textarea"));
            inputTextarea.clear();
            inputTextarea.sendKeys(someTextToFill);
            Thread.sleep(100);
            
            WebElement inputPhoneNumber = webDriver.findElement(By.id("test_phone_number"));
            inputPhoneNumber.clear();
            inputPhoneNumber.sendKeys(phoneToFill);
            Thread.sleep(100);
            
            List<WebElement> checkboxList = webDriver.findElements(By.name("test_checkboxes[]"));
            System.out.println("checkboxList.size()" + checkboxList.size());
            Thread.sleep(10000);
            
            
            
            
            WebElement dropdown = webDriver.findElement(By.id("test_dropdown"));
            dropdown.click();
            Thread.sleep(2000);
            List<WebElement> listOfOptions = dropdown.findElements(By.tagName("option"));
            int indexToClick1 = helperClass.getRandomDigit(1, (listOfOptions.size()-1));
            listOfOptions.get(indexToClick1).click();
            dropdown.click();
            Thread.sleep(2000);
            dropdown.click();
            Thread.sleep(2000);            
            int indexToClick2 = helperClass.getRandomDigit(0, (listOfOptions.size()-1));
            listOfOptions.get(indexToClick2).click();
            dropdown.click();
            Thread.sleep(2000);            
            
            
            js.executeScript("let infoDiv = document.getElementById('info'); infoDiv.innerText = infoDiv.dataset.info");
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            helperClass.printToFileAndConsoleInformation(fileToWriteErrorLogOfTesting, "ERROR: Error in main try block of FillTestFormClass"); 
        } finally {
            helperClass.setProgressBarValue(0, this.jProgressBar);
            webDriver.close();
            webDriver.quit();
        }
        
    }

    private void fillNecessaryData() {
        nameToFill = "Test name";
        emailToFill = "someEmail@mail.mail";
        someTextToFill = "<p>Some test <b>text</b> for testing!</p>";
        phoneToFill = "+1234567890";
        passwordToFill = "password1234";
        passwordConfirmToFill = "password1234";        
    }
    
}
