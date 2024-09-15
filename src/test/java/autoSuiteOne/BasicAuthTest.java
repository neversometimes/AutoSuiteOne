package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

public class BasicAuthTest {
    WebDriver driver;

    @BeforeMethod
    void setup () throws MalformedURLException {

        //URL seleniumServerURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerURL));
        //driver = new RemoteWebDriver(seleniumServerURL, options);


        // THIS TEST IS RUN ON CHROME ONLY
        // get target browser from CLI to check for headless mode param
        String browserName = System.getProperty("browser");

        ChromeOptions options = new ChromeOptions();
        if (browserName.contains("headless")) {
            options.addArguments("headless");
        }
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1440, 900));
        }

    @AfterMethod
    void teardown() {driver.quit();}

    @Test
    void testBasicAuth() {

        // NOTE:  RemoteWebDriver doesn't support HasAuthentication (nor do other non-Chrome browsers)
        //
        //((HasAuthentication) driver).register(() -> new UsernameAndPassword("guest", "guest"));
        //driver.get("https://jigsaw.w3.org/HTTP/Basic/");  // test site protected with basic auth

        // ** Send creds using protocol://username:password@domain  **  PRIMO WORKAROUND for remote or local runs
        String authURL = "https://guest:guest@jigsaw.w3.org/HTTP/Basic/";
        driver.get(authURL);

        WebElement body = driver.findElement(By.tagName("body"));
        assertTrue(body.getText().contains("Your browser made it!"));  //verify creds worked
    }

} // end class
