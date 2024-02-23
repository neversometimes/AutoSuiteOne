package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.*;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DeviceEmulationTest {

    WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() throws MalformedURLException {

        URL seleniumServerUrl = new URL("http://localhost:4444/");
        assertTrue(isOnline(seleniumServerUrl));

        ChromeOptions options = new ChromeOptions();

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 6/7/8");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        driver = new RemoteWebDriver(seleniumServerUrl, options);

    }

    @AfterTest
    void teardown() {
        driver.quit();
    }

    @Test
    void testDeviceEmulation() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        WebElement btnElement = driver.findElement(By.linkText("GitHub"));
        btnElement.click();
        assertTrue(driver.getCurrentUrl().contains("github.com"));

    }

}// end class
