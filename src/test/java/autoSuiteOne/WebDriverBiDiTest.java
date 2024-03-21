package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.events.CdpEventTypes;
import org.openqa.selenium.devtools.events.DomMutationEvent;
import org.openqa.selenium.logging.HasLogEvents;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class WebDriverBiDiTest {

    WebDriver driver;

    @BeforeSuite
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeTest
    void setup () {
        // **** LOCAL TESTS ONLY ****

        ChromeOptions options = new ChromeOptions();
        driver = WebDriverManager.chromedriver().create();

    }

    @AfterTest
    void teardown() {driver.quit();}

    @Test
    void testDomMutation() throws InterruptedException {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");  // base test page
        HasLogEvents logger = (HasLogEvents) driver;
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Listener for DOM mutation events. Expects to capture
        // only one event, synchronized using a countdown latch.
        AtomicReference<DomMutationEvent> seen = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        logger.onLogEvent(CdpEventTypes.domMutation(mutation -> {
            seen.set(mutation);
            latch.countDown();
        }));

        WebElement img = driver.findElement(By.tagName("img"));
        String newSrc = "img/award.png";
        String script = String.format("arguments[0].src = '%s';", newSrc);
        js.executeScript(script, img);  // DOM mutation by executing JS to change image source.

        assertTrue(latch.await(10, TimeUnit.SECONDS)); // Verify event occurs <= 10s

        //  Check the image source has changed
        assertTrue(seen.get().getElement().getAttribute("src").endsWith(newSrc));
    }

    @Test
    void testConsoleEvents() throws InterruptedException {
        HasLogEvents logger = (HasLogEvents) driver;

        //  Listener for console events.  Expects to capture 4
        //   events synchronized using a countdown latch.
        CountDownLatch latch = new CountDownLatch(4);
        logger.onLogEvent((CdpEventTypes.consoleEvent(consoleEvent -> {
          /*  System.out.println(consoleEvent.getTimestamp()
                    + " " + consoleEvent.getType()
                    + ": " + consoleEvent.getMessages() ); */
            latch.countDown();
        })));

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/console-logs.html");
        assertTrue(latch.await(10, TimeUnit.SECONDS)); // Verify event occurs <= 10s

    }

} // end class
