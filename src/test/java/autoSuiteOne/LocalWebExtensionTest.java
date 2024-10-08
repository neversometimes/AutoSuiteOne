package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import static org.testng.Assert.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class LocalWebExtensionTest {

    WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup () throws URISyntaxException {

        // path to web extension which sets background color of web pages to black
        Path extension = Paths
                .get(ClassLoader.getSystemResource("dark-bg.crx").toURI());
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(extension.toFile()); // add extension as a Java file

        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() {driver.quit();}

    @Test

    void testWebExtension() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        // locate the background color property
        String mainBgColor = driver.findElement(By.tagName("main")).getCssValue("background-color");

        // verify background color is now black (with the web extension having been installed)
        assertEquals(mainBgColor, "rgba(0, 0, 0, 0)");

    }

} // end class
