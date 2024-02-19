package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BasicAuthTest {
    WebDriver driver;

    @BeforeSuite
    void setupClass() {WebDriverManager.chromedriver().setup();}

    @BeforeTest
    void setup () {driver = WebDriverManager.chromedriver().create();}

    @AfterTest
    void teardown() {driver.quit();}

    @Test
    void testBasicAuth() {

        // cast driver obj to HasAuthentication and register creds
        ((HasAuthentication) driver).register(() -> new UsernameAndPassword("guest", "guest"));

        driver.get("https://jigsaw.w3.org/HTTP/Basic/");  // test site protected with basic auth

        WebElement body = driver.findElement(By.tagName("body"));
        assertTrue(body.getText().contains("Your browser made it!"));
    }

} // end class
