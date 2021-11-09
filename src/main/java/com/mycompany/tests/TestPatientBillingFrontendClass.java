/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
public class TestPatientBillingFrontendClass {
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
    
    public TestPatientBillingFrontendClass(String pathToFileFolderIn, String osNameIn, boolean isDeleteData, 
            JProgressBar jProgressBarIn, CredentialsClass credentialsClassIn, String mainUrlIncome){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.jProgressBar = jProgressBarIn;
        this.credentialsClass = credentialsClassIn;
        helperClass.setProgressBarValue(1, this.jProgressBar);
        this.mainUrl = mainUrlIncome;
    }
    
    public void startTestOfFrontend(){
        String localeName = "Test frontend";
        String appendixToAdd = "_tmp";
        int maxLoops = 1000;
        dateTimeOfSession = helperClass.getDateInStringForWindowsLinux();    
        String fileName = "";
        String fileNameERRORS = "";
        
        fileName = this.pathToLogFileFolder + "testFrontendLogFile_" + dateTimeOfSession + ".txt";
        fileNameERRORS = this.pathToLogFileFolder + "testFrontendLogFile_ERRORS_" + dateTimeOfSession + ".txt";        
        
        try {
            fileToWriteLogsOfTesting = new File(fileName);
            fileToWriteErrorLogOfTesting = new File(fileNameERRORS);
            System.out.println("Path to logfile:" + fileName);
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
            System.out.println("Error file creation, test log will be only in terminal");
        }
        
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Frontend test starts at: " + dateTimeOfSession +" OS: " + osName);
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
            Thread.sleep(500);  
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: Stage - Login");             
            startAndLoginToSite(webDriver, fileToWriteLogsOfTesting, mainUrl);         
            helperClass.setProgressBarValue(3, this.jProgressBar);
            Thread.sleep(1500);  
            webDriver.get("http://localhost:7370/#/providers/accounts");
            Thread.sleep(1500);  
            webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/div[2]/div[2]/div/div/div/button[4]")).click();
            Thread.sleep(1500);  
            
            List<WebElement> listElements = null;
            List<WebElement> listLis = null;
            
            for (int i = 0; i < maxLoops; i++) {
                helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Number of loop:" + i + " of " + maxLoops);
                Thread.sleep(2500); 
                WebElement tableWithAccounts = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/div[2]/table"));
                listElements = tableWithAccounts.findElements(By.tagName("tr"));                
                helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: listElements.size():" + listElements.size());   
                Thread.sleep(3500); 
                int numberToClick = helperClass.getRandomDigit(2, listElements.size()-2);
                helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: listElements click N:" + numberToClick);   
                listElements.get(numberToClick).click();
                Thread.sleep(6000); 
                Actions builder = new Actions(webDriver);
                WebElement hoverElement = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/div[2]/div[1]/div[3]/div[2]/div[1]/div"));
                builder.moveToElement(hoverElement).perform();
                Thread.sleep(1000); 
                listLis = hoverElement.findElements(By.tagName("li"));
                int numberLiToClick = helperClass.getRandomDigit(0, listLis.size()-1);
                helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "\nWork: list of li-s click N:" + numberLiToClick);   
                listLis.get(numberLiToClick).click();
                Thread.sleep(7000); 
                
                webDriver.get("http://localhost:7370/#/providers/accounts");
                Thread.sleep(1500); 
            }
            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            helperClass.printToFileAndConsoleInformation(fileToWriteErrorLogOfTesting, "ERROR: Error in main try block of TestPatientBillingFrontendClass"); 
        } finally {
            helperClass.setProgressBarValue(0, this.jProgressBar);
            webDriver.close();
            webDriver.quit();
        }
        
    }
    
    private void startAndLoginToSite(WebDriver webDriver, File fileToWriteLogsOfTesting, String mainUrl) throws InterruptedException{
        helperClass.writeStringToFile(fileToWriteLogsOfTesting, "Work: go to url:" + mainUrl);
        Thread.sleep(1500);
        webDriver.get(mainUrl);
        Thread.sleep(500);
        WebElement login = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/div[2]/div/div/form/div[1]/input"));
        WebElement passwd = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/div[2]/div/div/form/div[2]/input"));
        WebElement btnLogin = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/div[2]/div/div/form/button"));
        login.sendKeys(credentialsClass.getEmailToLogin());
        passwd.sendKeys(credentialsClass.getPasswordToLogin());
        Thread.sleep(300);            
        helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Work: trying to login with email " + credentialsClass.getEmailToLogin() + " and pswd " + credentialsClass.getPasswordToLogin());
        btnLogin.click();
        Thread.sleep(1500);
    }
}
