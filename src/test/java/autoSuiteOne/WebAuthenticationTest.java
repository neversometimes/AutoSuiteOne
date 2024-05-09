package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.virtualauthenticator.HasVirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;
import org.testng.annotations.*;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

public class WebAuthenticationTest {

     WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup () throws MalformedURLException {

        //URL seleniumServerURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerURL));

        ChromeOptions options = new ChromeOptions();
        //driver = new RemoteWebDriver(seleniumServerURL, options);

        driver = new ChromeDriver();
    }

    @AfterMethod
    void teardown() {driver.quit();}

    @Test
    void testWebAuthN() {

        driver.get("https://webauthn.io");  // go to site protected with Web Auth API

        // cast driver object to HasVirtualAuthenticator type
        HasVirtualAuthenticator virtualAuth = (HasVirtualAuthenticator) driver;
        // create and register a new virtual authenticator
        VirtualAuthenticator authenticator = virtualAuth.addVirtualAuthenticator(new VirtualAuthenticatorOptions());

        // send random key string to web form
        String randomID = UUID.randomUUID().toString();
        driver.findElement(By.id("input-email")).sendKeys(randomID);

        // wait then try to submit using random string with virtual authenticator credentials
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        driver.findElement(By.id("register-button")).click();  // try to register with random string
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("alert-success"), "Success! Now try to authenticate..."));

        driver.findElement(By.id("login-button")).click();  // try to login in once registered
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("main-content"), "You're logged in!")); // verify logged in success

        String successText = driver.findElement(By.className("main-content")).getText();
        assertTrue(successText.contains("You're logged in!")); // assert log in success

        virtualAuth.removeVirtualAuthenticator(authenticator);  //

    }


} // end class
