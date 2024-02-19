package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.*;

public class GeolocationTest {


    WebDriver driver;

    @BeforeTest
    void setup() {
        //  This section is to enable the test to dismiss the prompt to access location services
        //
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.geolocation", 1);  // value=1 to enable prompt bypass
        options.setExperimentalOption("prefs", prefs);

        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterTest
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
