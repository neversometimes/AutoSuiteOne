package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.*;

import static org.testng.Assert.*;

public class ScopeTest {
    WebDriver driver;
    SoftAssert sa = new SoftAssert();

    @BeforeClass
    public void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void testScope() throws Exception{
        // 1) get total links count on the page
        // 2) get links count only within the footer section
        // 3) get links count for footer 1st column only
        // 4) follow each link in #3, verify nav to correct page

        driver.get("https://qaclickacademy.com/practice.php");
        // #1 - full page scope
        List<WebElement> allAnchors = driver.findElements(By.tagName("a"));
        assertEquals(allAnchors.size(), 27);

        //#2a - isolating scope using CSS (my solution)
        List<WebElement> footerAnchors = driver.findElements(By.cssSelector(".gf-t a"));
        assertEquals(footerAnchors.size(), 20 );

        //#2b - isolating scope using WebElement driver
        WebElement footerDriver = driver.findElement(By.id("gf-BIG"));
        assertEquals(footerDriver.findElements(By.tagName("a")).size(), 20);

        //#3
        WebElement firstColDriver = footerDriver.findElement(By.cssSelector("table tbody tr td:first-child ul"));
        assertEquals(firstColDriver.findElements(By.tagName("a")).size(), 5);

        //#4

        List<WebElement> columnAnchors = firstColDriver.findElements(By.tagName("a"));

        int aTagCount = firstColDriver.findElements(By.tagName("a")).size();

        String os = System.getProperty("os.name");
        Actions action = new Actions(driver);

        for (int i=1; i<aTagCount; i++) {
            // control click each link to open new tabs

            if (os.contains("Windows")) {
                action.keyDown(Keys.CONTROL)
                        .pause(Duration.ofSeconds(1))
                        .click(columnAnchors.get(i))
                        .keyUp(Keys.CONTROL)
                        .build()
                        .perform();
            } else {  //  use MacOS COMMAND key
                action.keyDown(Keys.COMMAND)
                        .pause(Duration.ofSeconds(1))
                        .click(columnAnchors.get(i))
                        .keyUp(Keys.COMMAND)
                        .build()
                        .perform();

            }
            Thread.sleep(1000);
        }
       // cycle through new tab windows
        // NOTE: the windows are not kept in order, so verifying is not trivial

        //fetch handles of all windows
        Set<String> abc = driver.getWindowHandles();  // expect 5 : includes home test page

        for (String s : abc) { // starts at [0]
            driver.switchTo().window(s);
            Thread.sleep(500);
            System.out.println(driver.getTitle());
            System.out.println(driver.getCurrentUrl());

        }

    }
}
