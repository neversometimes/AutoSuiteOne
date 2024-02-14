package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;



public class DeviceEmulationTest {

    WebDriver driver;


    @BeforeTest
    void setup() {
        System.out.println("Setup DeviceEmulationTest");
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 6/7/8");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterTest
    void teardown() {
        System.out.println("teardown DeviceEmulationTest");
        driver.quit();
    }

    @Test
    void testDeviceEmulation() {
        System.out.println("testDeviceEmulation");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        WebElement btnElement = driver.findElement(By.linkText("GitHub"));
        btnElement.click();
        assertTrue(driver.getCurrentUrl().contains("github.com"));

    }

}// end class
