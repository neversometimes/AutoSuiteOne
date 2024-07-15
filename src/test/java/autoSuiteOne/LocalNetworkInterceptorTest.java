package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;
import org.testng.annotations.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.*;

public class LocalNetworkInterceptorTest {

    WebDriver driver;

    @BeforeClass
    void setupClass() {
       WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup () {
        driver = WebDriverManager.chromedriver().create();

    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test (description = "Network Interceptor test")
    void testNetworkInterceptor() throws Exception {
        // load local img test resource (only found under src/test/resources) : 128x128 PNG file
        Path img = Paths.get(ClassLoader.getSystemResource("tools.png").toURI());

        byte[] bytes = Files.readAllBytes(img);  //read in resource file as bytes

        try (NetworkInterceptor interceptor = new NetworkInterceptor(driver, //network interceptor created Route
                Route.matching(req -> req.getUri().endsWith(".png"))         //matching all http requests ending
                .to(() -> req -> new HttpResponse()                          // in ".png" and set new response with
                        .setContent(Contents.bytes(bytes))))) {              // test resource 'bytes'

                driver.get("https://bonigarcia.dev/selenium-webdriver-java/");  //open practice site
                int width = Integer.parseInt(driver.findElement(By.tagName("img"))
                             .getAttribute("width"));  //get width value of web element "img"

                assertTrue(width == 128);  // verify previous "img" size of 80x80 is now 128x128 test resource
        }

    }

} // end class
