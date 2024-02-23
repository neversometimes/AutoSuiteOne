package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

public class IncognitoModeTest {

    private WebDriver driver;
    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeMethod
    void setup () throws MalformedURLException {
       ChromeOptions options = new ChromeOptions();
       options.addArguments("--incognito");

       URL seleniumServerURL = new URL("http://localhost:4444");
       assertTrue(isOnline(seleniumServerURL));
       driver = new RemoteWebDriver(seleniumServerURL, options);

    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    public void testIncognitoModeByCapabilities() {

        // Can't figure out how to verify Incognito mode has been correctly set.
        // Given it's a browser setting and not a web page/app property or behavior,
        // maybe verification is not a high priority.

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String initHandle = driver.getWindowHandle();  // save handle to first browser window

        driver.switchTo().newWindow(WindowType.WINDOW);

        driver.get("https://github.com/bonigarcia/selenium-webdriver-java");

    }


























} // close class
