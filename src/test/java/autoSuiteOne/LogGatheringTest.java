package autoSuiteOne;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.*;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class LogGatheringTest {

    WebDriver driver;

    static final Logger log = getLogger(lookup().lookupClass());

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() throws MalformedURLException {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);

        ChromeOptions options = new ChromeOptions();
        options.setCapability(ChromeOptions.LOGGING_PREFS, logs);

        //URL seleniumServerURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerURL));

        //driver = new RemoteWebDriver(seleniumServerURL, options);

        driver = new ChromeDriver();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void gatherBrowserLogs() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/console-logs.html");

        LogEntries browserLogs = driver.manage().logs().get(LogType.BROWSER);

        assertFalse(browserLogs.getAll().isEmpty());  //verify logs not empty

        //browserLogs.forEach(System.out::println);  // prints out the browser log references ;

        browserLogs.forEach(l -> log.debug("{}", l));
    }

} //end class
