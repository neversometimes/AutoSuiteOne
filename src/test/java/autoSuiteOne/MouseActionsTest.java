package autoSuiteOne;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.locators.RelativeLocator;

import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class MouseActionsTest {

    WebDriver driver;


    @BeforeMethod
    void setup() throws MalformedURLException {

        //URL seleniumServerURL = new URL("http://localhost:4444");
        //assertTrue(isOnline(seleniumServerURL));

        // get target browser from CLI if given, otherwise use "chrome"
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : "chrome";

        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (browserName.contains("headless")) {
                options.addArguments("headless");
            }
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        } else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }
        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testAWebNav(){

        // load target page this test only
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        //very basic web page navigation using xpath link text and CLICK()
        driver.findElement(By.xpath("//a[text()='Navigation']")).click();
        driver.findElement(By.xpath("//a[text()='3']")).click();

        driver.findElement(By.xpath("//a[text()='2']")).click();
        driver.findElement(By.xpath("//a[text()='Next']")).click();
        driver.findElement(By.xpath("//a[text()='Previous']")).click();
        driver.findElement(By.xpath("//a[text()='1']")).click();

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains("Lorem ipsum"));
    }

    @Test
    void testCheckboxAndRadioBtns () {

        // load test web site page
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        WebElement checkbox2 = driver.findElement(By.id("my-check-2"));  //Default checkbox
        assertFalse(checkbox2.isSelected());  // verify default state is not selected

        // Actions doesn't seem to require the control to be visible on the screen
        new Actions(driver)
                .click(checkbox2)
                .pause(Duration.ofSeconds(3))
                .perform();

        assertTrue(checkbox2.isSelected());  // verify Default checkbox is now selected

        WebElement radio1 = driver.findElement(By.id("my-radio-1"));  // Checked Radio btn
        assertTrue(radio1.isSelected());        // verify default state is selected

        WebElement radio2 = driver.findElement(By.id("my-radio-2"));  //Default Radio btn
        assertFalse(radio2.isSelected());                   // verfiy default state is not selected

        //radio2.click();  // .click() seems to require the control to be visible - this was intermittent
        new Actions(driver)
                .click(radio2)
                .pause(Duration.ofSeconds(3))
                .perform();

        assertFalse(radio1.isSelected()); // verify new state Checked Radio is NOT selected
        assertTrue(radio2.isSelected());  // verfiy new state Default Radio is Selected

    }

    @Test
    void testContextAndDoubleClick(){

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html");
        Actions actions = new Actions(driver);

        WebElement dropdown2 = driver.findElement(By.id("my-dropdown-2"));
        actions.contextClick(dropdown2).build().perform();
        WebElement contextMenu2 = driver.findElement(By.id("context-menu-2"));
        assertTrue(contextMenu2.isDisplayed());

        WebElement dropdown3 = driver.findElement(By.id("my-dropdown-3"));
        actions.doubleClick(dropdown3).build().perform();
        WebElement contextMenu3 = driver.findElement(By.id("context-menu-3"));
        assertTrue(contextMenu3.isDisplayed());

    }

    @Test
    void testMouseOver() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/mouse-over.html");
        Actions actions = new Actions(driver);

        List<String> imageList = Arrays.asList("compass", "calendar", "award", "landscape");

        for (String imageName : imageList) {
            String xpath = String.format("//img[@src='img/%s.png']", imageName);
            WebElement image = driver.findElement(By.xpath(xpath));
            actions.moveToElement(image).build().perform();

            WebElement caption = driver.findElement(RelativeLocator.with(By.tagName("div")).near(image));

            assertTrue(imageName.equalsIgnoreCase(caption.getText()));

        }
    }
    @Test
    void testDragAndDrop() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html");
        Actions actions = new Actions(driver);

        WebElement draggable = driver.findElement(By.id("draggable"));
        int offset = 100;
        Point initLocation = draggable.getLocation();
        actions.dragAndDropBy(draggable, offset, 0)
                .dragAndDropBy(draggable, 0, offset)
                .dragAndDropBy(draggable, -offset, 0)
                .dragAndDropBy(draggable, 0, -offset).build().perform();
        assertEquals(initLocation, draggable.getLocation());

        WebElement target = driver.findElement(By.id("target"));
        actions.dragAndDrop(draggable, target).build().perform();
        assertEquals(target.getLocation(), draggable.getLocation());

    }

    @Test
    void testClickAndHold() {

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/draw-in-canvas.html");
        Actions actions = new Actions(driver);

        WebElement canvas = driver.findElement(By.tagName("canvas"));
        actions.moveToElement(canvas).clickAndHold();

        int numPoints = 10;
        int radius = 30;
        for (int i = 0; i <= numPoints; i++) {
            double angle = Math.toRadians((double) (360 * i) / numPoints);
            double x = Math.sin(angle) * radius;
            double y = Math.cos(angle) * radius;
            actions.moveByOffset((int) x, (int) y);
        }

        actions.release(canvas).build().perform();

    }

    @Test
    void testCopyAndPaste() {

        // NOTE: SystemUtils doesn't work with RemoteWebDriver. Gets hub properties and not node properties
        Keys modifier = SystemUtils.IS_OS_MAC ? Keys.COMMAND : Keys.CONTROL;

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        WebElement inputText = driver.findElement(By.name("my-text"));
        WebElement textArea = driver.findElement(By.name("my-textarea"));

        Actions actions = new Actions(driver);

        actions.sendKeys(inputText, "Good-ay Mate!")
                .pause(Duration.ofSeconds(3))
                .keyDown(modifier)
                .sendKeys(inputText, "a")
                .sendKeys(inputText, "c")
                .pause(Duration.ofSeconds(3))
                .sendKeys(textArea, "v")
                .pause(Duration.ofSeconds(3))
                .perform();

        // verify the copy-paste action; source and target strings match
        assertEquals(inputText.getAttribute("value"), textArea.getAttribute("value"));

    }

} // end class
