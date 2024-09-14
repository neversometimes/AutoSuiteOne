package autoSuiteOne;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import org.openqa.selenium.WebDriver;


import java.net.MalformedURLException;
import java.net.URL;


public class KeyboardActionsTest {
    WebDriver driver;


    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeMethod
    void setup() throws MalformedURLException {


        //URL seleniumServerURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerURL));

        //ChromeOptions options = new ChromeOptions();
        //driver = new RemoteWebDriver(seleniumServerURL, options);

        //driver = new ChromeDriver();
        driver = new FirefoxDriver();


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




