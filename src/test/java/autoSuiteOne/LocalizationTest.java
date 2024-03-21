package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import static org.testng.Assert.*;

public class LocalizationTest {

    WebDriver driver;
    String lang;
    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup(){

        lang = "es-ES";
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", lang);
        options.setExperimentalOption("prefs", prefs);

        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    public void testAcceptLang() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/multilanguage.html");

        // **** LOCAL TEST ONLY ****
        ResourceBundle strings = ResourceBundle.getBundle("strings", Locale.forLanguageTag(lang));

        String home = strings.getString("home");
        String content = strings.getString("content");
        String about = strings.getString("about");
        String contact = strings.getString("contact");

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains(home));
        assertTrue(bodyText.contains(content));
        assertTrue(bodyText.contains(about));
        assertTrue(bodyText.contains(contact));

    }

   @Test
    void testUpLoadFile() throws IOException {
        //  **** LOCAL TEST ONLY ****

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        String initURL = driver.getCurrentUrl();

        WebElement inputFile = driver.findElement(By.name("my-file"));  //select file input element

        Path tempFile = Files.createTempFile("tempfiles", ".tmp"); // create temp file
        String filename = tempFile.toAbsolutePath().toString();
        inputFile.sendKeys(filename);

        driver.findElement(By.tagName("form")).submit();
        assertNotSame(driver.getCurrentUrl(), initURL);

    }


} // end class
