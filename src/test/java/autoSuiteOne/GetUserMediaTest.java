package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.time.Duration;
import java.util.regex.Pattern;

public class GetUserMediaTest {

    WebDriver driver;
    @BeforeTest
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");

        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterTest()
    void teardown()  throws InterruptedException {
        // PAUSE FOR MANUAL INSPECTION OF TEST PAGE
        Thread.sleep(Duration.ofSeconds(3).toMillis());

        driver.quit();
    }

    @Test
    void testGetUserMedia() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/get-user-media.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // WAIT!!

        driver.findElement(By.id("start")).click();  //play video
        By videoDevice = By.id("video-device");     // locate video player
        Pattern nonEmptyString = Pattern.compile(".+"); // need to research this
        wait.until(ExpectedConditions.textMatches(videoDevice, nonEmptyString)); //WAIT!!

        // Verify the video text is not null ("Using video device: fake_device_0")
        assertNotEquals(driver.findElement(videoDevice).getText(), null);

    }
}
