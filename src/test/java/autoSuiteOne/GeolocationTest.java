package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

public class GeolocationTest {
    WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() throws MalformedURLException {


        ChromeOptions options = new ChromeOptions();

        //  This section enables the test to dismiss the prompt to access location services
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.geolocation", 1);  // value=1 to enable prompt bypass
        options.setExperimentalOption("prefs", prefs);

        // set driver to run tests remotely
        //URL seleniumServerURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerURL));  // verify remote is online

        //driver = new RemoteWebDriver(seleniumServerURL, options);
        driver = new ChromeDriver();

    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    public void testGeolocation () {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        WebElement pageBtn = driver.findElement(By.linkText("Geolocation"));
        pageBtn.click();

        // find coordinates button and click it
        WebElement coordBtn = driver.findElement(By.xpath("//*[@id='get-coordinates']"));
        assertEquals(coordBtn.getText(), "Get coordinates");  // verify button text

        coordBtn.click();  // get geo-coordinates

        // find element that displays the geo-coordinates
        WebElement element = driver.findElement(By.xpath("//*[@id='coordinates']"));

        // ** WAIT until the geo-coordinates text to appear on the page **
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.attributeToBeNotEmpty(element, "innerHTML"));

        assertTrue(element.getText().contains("Latitude") && element.getText().contains("Longitude"));

    }

} // end class
