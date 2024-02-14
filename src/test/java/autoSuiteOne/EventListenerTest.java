package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;

// NOTE: JUnit annotations replaced with TestNG versions; commented out within class below
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


public class EventListenerTest {
    WebDriver driver;

    // @BeforeEach
    @BeforeTest
    void setup() {
        System.out.println("Setup EventListenerTest");
        MyEventListener listener = new MyEventListener();  // construct listener object
        WebDriver originalDriver = WebDriverManager.chromedriver().create();  // create driver
        driver = new EventFiringDecorator<>(listener).decorate(originalDriver);  // decorate driver with listener
    }

    // @AfterEach
    @AfterTest
    void teardown() {
        System.out.println("teardown EventListenerTest");
        driver.quit();
    }

    @Test
    void testEventListener() {
        System.out.println("testEventListener");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");  // driver + listener "afterGet" takes screenshot

        //assertThat(driver.getTitle()).isEqualTo("Hands-On Selenium WebDriver with Java");
        assertEquals(driver.getTitle(),  "Hands-On Selenium WebDriver with Java");

        driver.findElement(By.linkText("Web form")).click();
        assertEquals(driver.getCurrentUrl(), "https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

    }

} // end class
