package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.*;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.*;


public class HeadlessChromeJupiterTest {

    WebDriver driver;

    @BeforeSuite
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeTest
    void setup() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        // options.setHeadless(true);         Not sure why this doesn't recognize options.setHeadless

        driver = new ChromeDriver(options);
    }

    @AfterTest
    void teardown() {
        driver.quit();
    }

    @Test
    void testHeadless() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        assertTrue (driver.getTitle().contains("Selenium WebDriver"));

    }




}
