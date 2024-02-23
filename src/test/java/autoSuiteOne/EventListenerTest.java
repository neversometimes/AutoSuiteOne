package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;

// NOTE: JUnit annotations replaced with TestNG versions; commented out within class below
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.*;


public class EventListenerTest {
    WebDriver driver;

    // @BeforeEach
    @BeforeMethod
    void setup() throws MalformedURLException {

        URL seleniumServerUrl = new URL("http://localhost:4444/");
        assertTrue(isOnline(seleniumServerUrl));

        ChromeOptions options = new ChromeOptions();
        WebDriver originalDriver = new RemoteWebDriver(seleniumServerUrl, options);


        MyEventListener listener = new MyEventListener();  // construct listener object
        driver = new EventFiringDecorator<>(listener).decorate(originalDriver);  // decorate driver with listener
    }

    // @AfterEach
    @AfterMethod
    void teardown() {

        driver.quit();
    }

    @Test
    void testEventListener() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");  // driver + listener "afterGet" takes screenshot

        //assertThat(driver.getTitle()).isEqualTo("Hands-On Selenium WebDriver with Java");
        assertEquals(driver.getTitle(),  "Hands-On Selenium WebDriver with Java");

        driver.findElement(By.linkText("Web form")).click();
        assertEquals(driver.getCurrentUrl(), "https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

    }

} // end class
