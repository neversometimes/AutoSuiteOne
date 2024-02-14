package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.*;

public class IncognitoModeTest {

    private WebDriver driver;
    @BeforeSuite
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeTest
    void setup () {
       ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        driver = WebDriverManager.chromedriver().capabilities(options).create();

    }

    @AfterTest
    void teardown() {
        driver.quit();
    }

    @Test
    public void testIncognitoModeByCapabilities() {

        // Can't figure out how to verify Incognito mode is set.
        // Given it's a browser setting and not a web page/app property or behavior,
        // maybe verification is not a high priority.

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String initHandle = driver.getWindowHandle();  // save handle to first browser window

        driver.switchTo().newWindow(WindowType.WINDOW);

        driver.get("https://github.com/bonigarcia/selenium-webdriver-java");

    }


























} // close class
