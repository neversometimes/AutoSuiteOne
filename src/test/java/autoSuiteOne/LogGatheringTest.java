package autoSuiteOne;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.logging.Level;

public class LogGatheringTest {

    WebDriver driver;

    static final Logger log = getLogger(lookup().lookupClass());


    @BeforeTest
    void setup() {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);

        ChromeOptions options = new ChromeOptions();
        options.setCapability(ChromeOptions.LOGGING_PREFS, logs);

        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterTest
    void teardown() {
        driver.quit();
    }

    @Test
    void gatherBrowserLogs() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/console-logs.html");

        LogEntries browserLogs = driver.manage().logs().get(LogType.BROWSER);

        assertFalse(browserLogs.getAll().isEmpty());  //verify logs not empty

        // browserLogs.forEach(System.out::println);  // prints out the browser logs - note lambda syntax!

        browserLogs.forEach(l -> log.debug("{}", l));  // this currently doesn't work because
                                                        // SLF4J provider doesn't currently load properly :-P
    }

} //end class
