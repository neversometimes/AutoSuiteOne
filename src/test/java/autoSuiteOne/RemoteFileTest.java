package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

public class RemoteFileTest {


    WebDriver driver;

     @BeforeClass
    public void setupClass() {

         WebDriverManager.chromedriver().setup();
         //WebDriverManager.firefoxdriver().setup();

     }
    
    @BeforeMethod
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        //FirefoxOptions options1 = new FirefoxOptions();

        // set driver to run tests remotely
        //URL seleniumServerHubURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerHubURL));  // verify remote is online

        //driver = new RemoteWebDriver(seleniumServerHubURL, options);
        driver = new ChromeDriver();
    }

    @AfterMethod public void teardown() { driver.quit(); }
    
    @Test
    void testUpLoadFile() throws IOException {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        String initURL = driver.getCurrentUrl();

        WebElement inputFile = driver.findElement(By.name("my-file"));  //select file input element
        Path tempFile = Files.createTempFile("tempfiles", ".tmp");
        String filename = tempFile.toAbsolutePath().toString();

        //((RemoteWebElement) inputFile).setFileDetector(new LocalFileDetector());  // used for remoteWebDriver only

        inputFile.sendKeys(filename); // local test file

        driver.findElement(By.tagName("form")).submit();
        assertNotSame(driver.getCurrentUrl(), initURL);

    }

}
