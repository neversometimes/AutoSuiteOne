package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;


import org.testng.annotations.*;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class PageLoadChromeJupiterTest {

    WebDriver driver;

    PageLoadStrategy pageLoadStrategy;

    @BeforeClass
    void setupClass(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() throws MalformedURLException {

        ChromeOptions options = new ChromeOptions();
        pageLoadStrategy = PageLoadStrategy.NORMAL;   // set page load strategy to NORMAL
        options.setPageLoadStrategy(pageLoadStrategy);

        URL seleniumServerURL = new URL("http://localhost:4444");
        assertTrue(isOnline(seleniumServerURL));

        driver = new RemoteWebDriver(seleniumServerURL, options);

    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testPageLoad(){
        long initMillis = System.currentTimeMillis();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - initMillis);

        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        Object pageLoad = capabilities.getCapability(CapabilityType.PAGE_LOAD_STRATEGY);
        String browserName = capabilities.getBrowserName();
        System.out.println(browserName);
        assertEquals(pageLoad, (pageLoadStrategy.toString()));
    }

} // end class
