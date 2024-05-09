package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

public class LoadingInsecurePagesTest {

    WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() throws MalformedURLException {

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);  // enable capability to allow insecure certs

        //URL seleniumServerURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerURL));

        //driver = new RemoteWebDriver(seleniumServerURL, options);
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    public void testInsecure() {
        driver.get("https://self-signed.badssl.com");

        String bgColor = driver.findElement(By.tagName("body")).getCssValue("background-color");
        Color red = new Color(255, 0, 0, 1);
        assertEquals(Color.fromString(bgColor), red);

    }

} // end class
