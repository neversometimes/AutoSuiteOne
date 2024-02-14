package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;


import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

import java.time.Duration;

public class PageLoadChromeJupiterTest {

    WebDriver driver;

    PageLoadStrategy pageLoadStrategy;

    @BeforeTest
    void setup() {
        System.out.println("setup PageLoadChromeJupiterTest");
        ChromeOptions options = new ChromeOptions();
        pageLoadStrategy = PageLoadStrategy.NORMAL;   // set page load strategy to NORMAL
        options.setPageLoadStrategy(pageLoadStrategy);

        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterTest
    void teardown() {
        System.out.println("teardown PageLoadChromeJupiterTest");
        driver.quit();
    }

    @Test
    void testPageLoad(){
        System.out.println("testPageLoad");
        long initMillis = System.currentTimeMillis();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - initMillis);

        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        Object pageLoad = capabilities.getCapability(CapabilityType.PAGE_LOAD_STRATEGY);
        String browserName = capabilities.getBrowserName();

        assertEquals(pageLoad, (pageLoadStrategy.toString()));
    }

} // end class
