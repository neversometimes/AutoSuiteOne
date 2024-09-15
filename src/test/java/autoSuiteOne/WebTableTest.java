package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class WebTableTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {

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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  // basic implicit wait 10s

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    // Exercise:  the following test was an exercise to count the rows and columns of a web table,
    //              then print out the data found in the 2nd row.

    @Test
    public void tableRowCountTest() {

        driver.get("https://rahulshettyacademy.com/AutomationPractice/");

        List<WebElement> webTableRows = driver.findElements(By.cssSelector(".table-display tbody tr"));
            //System.out.println("Total Rows: " + webTableRows.size());
        assertEquals(webTableRows.size(), 11);

        List<WebElement> webTableColumns = driver.findElements(By.cssSelector(".table-display tbody th"));
            //System.out.println("Total Columns: " + webTableColumns.size());
        assertEquals(webTableColumns.size(), 3);

        List<WebElement> webTable2ndRow = webTableRows.get(2).findElements(By.cssSelector("td"));

        System.out.println("\nTable Second Row Data:");
        for (WebElement webElement : webTable2ndRow) {

            System.out.print("| " + webElement.getText() + " |");

        }
        System.out.println();
    }

}
