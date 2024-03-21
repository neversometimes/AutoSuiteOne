package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

public class BasicAuthTest {
    WebDriver driver;

    @BeforeClass
    void setupClass() {WebDriverManager.chromedriver().setup();}

    @BeforeMethod
    void setup () throws MalformedURLException {

        URL seleniumServerURL = new URL("http://localhost:4444");
        assertTrue(isOnline(seleniumServerURL));

        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(seleniumServerURL, options);

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
