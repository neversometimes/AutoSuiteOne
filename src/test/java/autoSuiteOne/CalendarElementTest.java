package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static org.testng.Assert.*;

import java.time.Duration;
import java.util.Properties;

public class CalendarElementTest {
    WebDriver driver;
    SoftAssert sa = new SoftAssert();

    @BeforeMethod
    public void setUp() {

        // get target browser from CLI if given, otherwise use "chrome"
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : "chrome";

        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (browserName.contains("headless")){  // if headless param given on CLI
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));  // basic implicit wait
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testCalendarElement() {
        // Exercise: set a date using a web calendar element
        //  Task: set date to: 15 June 2027
        //  assert date set is correct after using calendar element

        String day = "15";
        String month = "June";
        String year = "2027";

        driver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");  // start at target page!!

        // click on calendar text input to bring up calendar date picker
        driver.findElement(By.cssSelector(".react-date-picker__inputGroup")).click();

        // click TWICE on month YYYY header of calendar picker
        driver.findElement(By.cssSelector("button[class='react-calendar__navigation__label']")).click();
        driver.findElement(By.cssSelector("button[class='react-calendar__navigation__label']")).click();

        // click on "2027" in calendar year picker
        driver.findElement(By.xpath("//button[normalize-space()='" + year + "']")).click();


        // click "June" from calendar month picker
        driver.findElement((By.xpath("//button[contains(.,'" + month + "')]"))).click();


        // click "15" in calendar day picker
        driver.findElement(By.xpath("//button[contains(.,'" + day + "')]")).click();

        // assert that the new date picked is now correctly showing in the element
        String yearTxt = driver.findElement(By.cssSelector("input[name*='year']")).getAttribute("value");
        String monthTxt = driver.findElement(By.cssSelector("input[name*='month']")).getAttribute("value");
        String dayTxt = driver.findElement(By.cssSelector("input[name*='day']")).getAttribute("value");

        assertEquals(monthTxt + "/" + dayTxt + "/" + yearTxt, "6/15/2027");

    }
}
