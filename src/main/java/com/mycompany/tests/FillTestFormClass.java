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
    private boolean isResultDelete;
    
    private String nameToFill, emailToFill, someTextToFill, phoneToFill, passwordToFill, passwordConfirmToFill;
    
    public FillTestFormClass(String pathToFileFolderIn, String osNameIn, boolean deleteResult, JProgressBar jProgressBarIn, 
            CredentialsClass credentialsClassIn, String mainUrlIncome){
        this.pathToLogFileFolder = pathToFileFolderIn;
        this.osName = osNameIn;
        this.jProgressBar = jProgressBarIn;
        this.credentialsClass = credentialsClassIn;
        this.mainUrl = mainUrlIncome;
        this.isResultDelete = deleteResult;
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
        helperClass.setProgressBarValue(1, this.jProgressBar);
        
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
            Thread.sleep(1000);
            helperClass.createMiniScreen(js);
            Thread.sleep(1000);
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "exampleform", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "exampleform!!! Return...", "Empty");
                return;
            }
            Thread.sleep(1000);
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to fill input for name with data:<br>" + nameToFill, js);
            Thread.sleep(500);
            WebElement inputName = webDriver.findElement(By.id("test_name"));
            inputName.clear();
            inputName.sendKeys(nameToFill);  
            Thread.sleep(1500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(2, this.jProgressBar);
            Thread.sleep(1000);
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to fill input for Email with data:<br>" + emailToFill, js);
            Thread.sleep(500);
            WebElement inputEmail = webDriver.findElement(By.id("test_email"));
            inputEmail.clear();
            inputEmail.sendKeys(emailToFill); 
            Thread.sleep(1500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(3, this.jProgressBar);
            Thread.sleep(1000);
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to fill input for Textarea with data:<br>" + someTextToFill, js);
            Thread.sleep(500);
            WebElement inputTextarea = webDriver.findElement(By.id("test_textarea"));
            inputTextarea.clear();
            inputTextarea.sendKeys(someTextToFill); 
            Thread.sleep(1500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(4, this.jProgressBar);
            Thread.sleep(1000);
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to fill input for phone number with data:<br>" + phoneToFill, js);
            Thread.sleep(500);
            WebElement inputPhoneNumber = webDriver.findElement(By.id("test_phone_number"));
            inputPhoneNumber.clear();
            inputPhoneNumber.sendKeys(phoneToFill); 
            Thread.sleep(1500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(5, this.jProgressBar);
            Thread.sleep(1000);
            
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to tick checkboxes", js);  
            Thread.sleep(500);
            List<WebElement> checkboxList = webDriver.findElements(By.name("test_checkboxes[]"));
            int numberOfElementsToTick = helperClass.getRandomDigit(1, (checkboxList.size() - 1));
            for(int i = 0; i < numberOfElementsToTick; i++) {
                checkboxList.get(i).click();
                Thread.sleep(500);
            }
            helperClass.setProgressBarValue(6, this.jProgressBar);
            Thread.sleep(1000);
            
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to tick radiobutton", js);  
            List<WebElement> radioButtonsList = webDriver.findElements(By.name("test_radio"));
            int numberOfElementToClick = helperClass.getRandomDigit(1, (radioButtonsList.size() - 1));
            radioButtonsList.get(numberOfElementToClick).click();
            Thread.sleep(1500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(7, this.jProgressBar);
            Thread.sleep(1000);
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to scroll page down", js);  
            Thread.sleep(1000);
            js.executeScript("window.scrollBy(0,245)");
            Thread.sleep(1000);
            
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to change element in dropdown", js);            
            Thread.sleep(500);
            WebElement dropdown = webDriver.findElement(By.id("test_dropdown"));
            dropdown.click();
            Thread.sleep(2000);
            List<WebElement> listOfOptions = dropdown.findElements(By.tagName("option"));
            int indexToClick1 = helperClass.getRandomDigit(1, (listOfOptions.size()-1));
            listOfOptions.get(indexToClick1).click(); 
            Thread.sleep(500);          
            helperClass.setProgressBarValue(8, this.jProgressBar);
            helperClass.appendTextToPrevious(js, "Done");
            Thread.sleep(500);          
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to change element in dropdown", js);  
            dropdown.click();
            Thread.sleep(2000);
            dropdown.click();
            Thread.sleep(2000);            
            int indexToClick2 = helperClass.getRandomDigit(0, (listOfOptions.size()-1));
            listOfOptions.get(indexToClick2).click(); 
            Thread.sleep(500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(9, this.jProgressBar);
            dropdown.click();
            Thread.sleep(2000);  
            
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to fill input for password with data:<br>" + passwordToFill, js);
            Thread.sleep(500);
            WebElement inputPassword = webDriver.findElement(By.id("test_password"));
            inputPassword.clear();
            inputPassword.sendKeys(passwordToFill); 
            Thread.sleep(1500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(10, this.jProgressBar);
            Thread.sleep(1000);
            
            helperClass.printToFileAndJsAndConsoleInformation(fileToWriteLogsOfTesting, "Try to fill input for password confirm with data:<br>" + passwordConfirmToFill, js);
            Thread.sleep(500);
            WebElement inputPasswordConfirm = webDriver.findElement(By.id("test_password_confirmation"));
            inputPasswordConfirm.clear();
            inputPasswordConfirm.sendKeys(passwordToFill); 
            Thread.sleep(1500);          
            helperClass.appendTextToPrevious(js, "Done");
            helperClass.setProgressBarValue(11, this.jProgressBar);
            Thread.sleep(1000);                        
            js.executeScript("let infoDiv = document.getElementById('info-button'); infoDiv.innerText = infoDiv.dataset.info");
            Thread.sleep(5000);
            //Nota Bene! Now save data to find results!! User may change it!
            nameToFill = inputName.getAttribute("value");
            
            webDriver.findElement(By.id("btnSaveTestData")).click();
            helperClass.setProgressBarValue(12, this.jProgressBar);
            Thread.sleep(1000);
            
            
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Try to login");
            webDriver.get(mainUrl + "login");
            Thread.sleep(1000);             
            
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "login", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "login!!! Return...", "Empty");
                return;
            }
            Thread.sleep(1000);
            
            WebElement inputEmalToLogin = webDriver.findElement(By.id("email"));
            inputEmalToLogin.clear();
            inputEmalToLogin.sendKeys(credentialsClass.getEmailToLogin());
            helperClass.setProgressBarValue(13, this.jProgressBar);
            
            WebElement inputPasswordToLogin = webDriver.findElement(By.id("password"));
            inputPasswordToLogin.clear();
            inputPasswordToLogin.sendKeys(credentialsClass.getPasswordToLogin());
            helperClass.setProgressBarValue(14, this.jProgressBar);
            
            webDriver.findElement(By.xpath("//*[@id=\"app\"]/main/div/div/div/div/div[2]/form/div[4]/div/button")). click();
            helperClass.setProgressBarValue(15, this.jProgressBar);
            Thread.sleep(1000);       
            //home
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "home", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "home!!! Return...", "Empty");
                return;
            }
            webDriver.get(mainUrl + "management/menu");            
            Thread.sleep(1000); 
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "management/menu", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "management/menu!!! Return...", "Empty");
                return;
            }
            helperClass.setProgressBarValue(16, this.jProgressBar);
            webDriver.findElement(By.id("accordionManagement")).click();
            Thread.sleep(1000); 
            webDriver.findElement(By.id("accordionElExampleList")).click();
            Thread.sleep(1000); 
            if(!helperClass.checkIfOnUrlNow(webDriver.getCurrentUrl(), mainUrl + "management/example/list", fileToWriteLogsOfTesting)) {
                helperClass.writeErrorsToFiles(fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting, "ERROR: Not on URL" + mainUrl + "management/example/list!!! Return...", "Empty");
                return;
            }
            helperClass.setProgressBarValue(17, this.jProgressBar);
            
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Try to find saved data.");
            int[] indexesOfCurrentTestData = helperClass.getIdAndPaginationNumberOfModelOnPageNextPrev(mainUrl, nameToFill, 2, "tableWithSampleResultsData", 
            "id", "//*[@aria-label='Pagination Navigation']", "xpath", "//*[@id=\"app\"]/main/div/div/div/div/div/div[2]/div/div/div[3]/nav", "xpath", webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            Thread.sleep(1000); 
            helperClass.setProgressBarValue(18, this.jProgressBar);
            
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Try to view saved data.");
            webDriver.get(mainUrl + "management/example/list?page=" + indexesOfCurrentTestData[1]);  
            helperClass.clickOnEditButtonByModelId(indexesOfCurrentTestData[0], 10, "tableWithSampleResultsData", "id", "", 
            webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            helperClass.setProgressBarValue(19, this.jProgressBar);
            Thread.sleep(3000);
            webDriver.navigate().back();
            Thread.sleep(1000);
             
            if (isResultDelete) {
                helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Try to delete saved data.");
                helperClass.clickOnEditButtonByModelId(indexesOfCurrentTestData[0], 11, "tableWithSampleResultsData", "id", "", 
                webDriver, js, fileToWriteLogsOfTesting, fileToWriteErrorLogOfTesting);
            } else {
                helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Do not delete result...");
            }
            helperClass.setProgressBarValue(20, this.jProgressBar);
            
            Thread.sleep(5000); 
            helperClass.printToFileAndConsoleInformation(fileToWriteLogsOfTesting, "Normal end of the programm");
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
        nameToFill = helperClass.getRandomStringWithLength(15);
        emailToFill = "someEmail@mail.mail";
        someTextToFill = "<p>Some test <b>text</b> for testing!</p>";
        phoneToFill = "1234567890";
        passwordToFill = "password1234";
        passwordConfirmToFill = "password1234";        
    }    
    
}
