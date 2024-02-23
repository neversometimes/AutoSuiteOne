package autoSuiteOne;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;



import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class KeyboardActionsTest {
    WebDriver driver;


    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeMethod
    void setup() throws MalformedURLException {


        URL seleniumServerURL = new URL("http://localhost:4444");
        assertTrue(isOnline(seleniumServerURL));

        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(seleniumServerURL, options);


        // ** load test web site page for ALL KeyboardActionsTest suite
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testSendKeys(){

        WebElement inputText = driver.findElement(By.name("my-text"));
        String textValue = "Hello World!";

        inputText.sendKeys(textValue);
        assertEquals(inputText.getAttribute("value"), textValue);

        inputText.clear();
        assertTrue(inputText.getAttribute("value").isEmpty() );
    }

 /*   @Test
    void testUpLoadFile() throws IOException {

        String initURL = driver.getCurrentUrl();

        WebElement inputFile = driver.findElement(By.name("my-file"));  //select file input element

        Path tempFile = Files.createTempFile("tempfiles", ".tmp"); // create temp file
        String filename = tempFile.toAbsolutePath().toString();
        inputFile.sendKeys(filename);

        driver.findElement(By.tagName("form")).submit();
        assertNotSame(driver.getCurrentUrl(), initURL);

    }  */

    @Test
    void testRangeSlider() {

        WebElement slider = driver.findElement(By.name("my-range"));
        String initValue = slider.getAttribute("value");

        for (int i = 0; i < 5; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);   // arrow right key 5x
        }
        String endValue = slider.getAttribute("value");
        assertNotSame(initValue, endValue);

        for (int i = 0; i < 5; i++) {
            slider.sendKeys(Keys.ARROW_LEFT);   // arrow left key 5x
        }

        String lastValue = slider.getAttribute("value");
        assertEquals(lastValue, initValue);   // should be at the starting point again

    }

}  // end class




