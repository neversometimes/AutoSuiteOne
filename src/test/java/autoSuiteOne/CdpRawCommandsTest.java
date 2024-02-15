package autoSuiteOne;

import com.google.common.collect.ImmutableList;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v121.network.model.BlockedReason;
import org.openqa.selenium.devtools.v121.network.model.Headers;
import org.openqa.selenium.devtools.v121.performance.Performance;
import org.openqa.selenium.devtools.v121.dom.model.Rect;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.ConnectionType;
import org.openqa.selenium.devtools.v121.page.Page;
import org.openqa.selenium.devtools.v121.page.model.Viewport;
import org.openqa.selenium.devtools.v121.performance.model.Metric;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

public class CdpRawCommandsTest {

    private WebDriver driver;

    private DevTools devTools;

    @BeforeTest
    void setup() {
        driver = WebDriverManager.chromedriver().create();
        devTools = ((ChromeDriver) driver).getDevTools();  // capture devtools instance from driver obj
        devTools.createSession();                         // create CDP session
    }

    @AfterTest
    void teardown() {
        devTools.close();  // terminate CDP session
        driver.quit();
    }

    @Test
    void testEmulateNetworkConditions() {
        // activate network tracking without tuning any network params
        devTools.send(Network.enable(Optional.empty(),
                Optional.empty(), Optional.empty()));

        // emulate a mobile 3G network
        devTools.send(Network.emulateNetworkConditions(false, 100, 50 * 1024,
                50 * 1024, Optional.of(ConnectionType.CELLULAR3G)));

        long initMillis = System.currentTimeMillis();  // get start of page load
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - initMillis);  // get page load time(ms) delta

        //System.out.println("The page took " + elapsed.toMillis() + " ms to be loaded");
        // the 3G emulation slows the page load time from about 500 ms to 2300ms

        assertTrue(driver.getTitle().contains("Selenium WebDriver"));
    }

    @Test
    void testNetworkMonitoring() {
        // activate network tracking without tuning any network params
        devTools.send(Network.enable(Optional.empty(),
                Optional.empty(), Optional.empty()));

        devTools.addListener(Network.requestWillBeSent(), request -> {  // set network request listener
        //    System.out.println("Request: " + request.getRequestId());
        //    System.out.println("Method: " + request.getRequest().getMethod());
        //    System.out.println("URL: " + request.getRequest().getUrl());
            //print headers?
        });

        devTools.addListener(Network.responseReceived(), response -> {  // set network response listener
        //    System.out.println("Response: " + response.getRequestId());
        //    System.out.println("URL: " + response.getResponse().getUrl());
        //    System.out.println("Status: " + response.getResponse().getStatus());
            assertEquals(response.getResponse().getStatus(), 200);

        //    System.out.print("Headers: ");
        //    response.getResponse().getHeaders().toJson().forEach((k, v) -> System.out.println(k + ":" +v));
        });

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");  // request: Get ; response: 200
    }

    @Test
    void testFullPageScreenshotChrome() throws IOException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(
                By.className("container"), By.tagName("p")));

        Page.GetLayoutMetricsResponse metrics = devTools
                .send(Page.getLayoutMetrics());

        Rect contentSize = metrics.getContentSize();
        String screenshotBase64;
        screenshotBase64 = devTools.send
                (Page.captureScreenshot
                        (Optional.empty(), //format
                                Optional.empty(), //quality
                                    Optional.of // clip
                                 (new Viewport(0, 0, contentSize.getWidth(), contentSize.getHeight(), 1)),
                                                Optional.empty(), //from Surface
                                                        Optional.of(true), //capture beyond viewport
                                                        Optional.of(true))); // optimize for speed


        Path destination = Paths.get("fullpage-screenshot-chrome.png");
        Files.write(destination, Base64.getDecoder().decode(screenshotBase64));

        assertTrue(Files.exists(destination));

    }

    @Test
    void testPerformanceMetrics() {
        devTools.send(Performance.enable(Optional.empty()));  // enable collecting metrics
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        List<Metric> metrics = devTools.send(Performance.getMetrics()); // gather all metrics
        assertFalse(metrics.isEmpty());

        // Print out the Name and Value for each of the CDP Perf metrics...
        //metrics.forEach(metric -> System.out.println(metric.getName() + ": " + metric.getValue()));
    }

    @Test
    void testExtraHeaders() {

        // activate network tracking without tuning any network params
        devTools.send(Network.enable(Optional.empty(),
                Optional.empty(), Optional.empty()));

        String userName = "guest";
        String password = "guest";

        Map<String, Object> headerz = new HashMap<>();                  // set new headers hashmap
        String basicAuth = "Basic " + new String(Base64.getEncoder()
                .encode(String.format("%s:%s", userName, password).getBytes())); // encode strings in base64

        headerz.put("Authorization", basicAuth); //put encoded strings into headers hashmap

        devTools.send(Network.setExtraHTTPHeaders(new Headers(headerz))); // create auth header

        driver.get("https://jigsaw.w3.org/HTTP/Basic/"); // navigate to the Basic auth page

        String bodyText = driver.findElement(By.tagName("body")).getText();  // find body text element

        assertTrue(bodyText.contains("Your browser made it!")); // verify expected text in body
    }

    @Test
    void testBlockURL() {
        // activate network tracking without tuning any network params
        devTools.send(Network.enable(Optional.empty(),
                Optional.empty(), Optional.empty()));

        String urlToBlock =
                "https://bonigarcia.dev/selenium-webdriver-java/img/hands-on-icon.png";

        devTools.send(Network.setBlockedURLs(ImmutableList.of(urlToBlock))); //block URL to img file

        devTools.addListener(Network.loadingFailed(), loadingFailed -> {  // listener to trace failed events
            BlockedReason reason = loadingFailed.getBlockedReason().get();
            System.out.println("Blocking reason: " + reason);
            assertEquals(reason, BlockedReason.INSPECTOR);
        });

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        assertTrue(driver.getTitle().contains("Selenium WebDriver"));

    }

} // end class
