package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.*;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.*;

import java.net.MalformedURLException;
import java.net.URL;


public class HeadlessChromeJupiterTest {

    WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless"); // CLI argument when starting Chrome: headless mode


        URL seleniumServerURL = new URL("http://localhost:4444");
        assertTrue(isOnline(seleniumServerURL));

        driver = new RemoteWebDriver(seleniumServerURL, options);

    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testHeadless() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        assertTrue (driver.getTitle().contains("Selenium WebDriver"));

    }

}
