package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.Color;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LoadingInsecurePagesTest {

    WebDriver driver;
    @BeforeTest
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);  // enable capability to allow insecure certs

        driver = WebDriverManager.chromedriver().capabilities(options).create();

    }

    @AfterTest
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
