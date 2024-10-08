package autoSuiteOne;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.testng.AssertJUnit.*;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LocatingElementsTest {
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

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().setSize(new Dimension(1440, 900));

        // ** load test web site page for ALL LocatingElementsTest class methods **
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    public void testTagName(){

        WebElement textarea = driver.findElement(By.tagName("textarea"));  // only one element has this name
        assertEquals((textarea.getDomAttribute("rows")), "3");
    }

    @Test
    void testLinkText(){
        WebElement linkByText = driver.findElement(By.linkText("Return to index"));
        assertEquals(linkByText.getTagName(), "a");
        assertEquals(linkByText.getCssValue("cursor"), "pointer");

        WebElement linkByPartialText = driver.findElement(By.partialLinkText("index"));
        assertEquals(linkByPartialText.getLocation(), linkByText.getLocation());
        assertEquals(linkByPartialText.getRect(), linkByText.getRect());
    }

    @Test
    void testHtmlAttributes(){
       //locate by HTML attributes name, id and class

        //by name
        WebElement textByName = driver.findElement(By.name("my-text"));
        assertTrue(textByName.isEnabled());

        //by id
        WebElement textById = driver.findElement(By.id("my-text-id"));
        assertEquals(textById.getAttribute("type"), "text");
        assertEquals(textById.getDomAttribute("type"), "text");
        assertEquals(textById.getDomProperty("type"), "text");

        assertEquals(textById.getAttribute("myprop"), "myvalue");
        assertEquals(textById.getDomAttribute("myprop"), "myvalue");
        assertNull(textById.getDomProperty("myprop"), null);


        //by class name
        List<WebElement> byClassName = driver.findElements((By.className("form-control")));
        assertFalse(byClassName.isEmpty());  //list has more than one element
        assertEquals(byClassName.get(0).getAttribute("name"), "my-text");
    }
    @Test
    void testCssSelector(){

        // basic locator with CSS selector
        WebElement hidden = driver.findElement(By.cssSelector("input[type=hidden]"));  //simple example
        assertFalse(hidden.isDisplayed());

        // advanced locators with CSS selectors
        WebElement checkbox1 = driver.findElement(By.cssSelector("[type=checkbox]:checked"));
        assertEquals(checkbox1.getAttribute("id"), "my-check-1");
        assertTrue(checkbox1.isSelected());

        WebElement checkbox2 = driver.findElement(By.cssSelector("[type=checkbox]:not(:checked)"));
        assertEquals(checkbox2.getAttribute("id"), "my-check-2");
        assertFalse(checkbox2.isSelected());
    }
    @Test
    void testXpath(){

        // basic locator with XPath
        WebElement hidden = driver.findElement(By.xpath("//input[@type='hidden']"));
        assertFalse(hidden.isDisplayed());

        // advanced locators with XPath
        WebElement radio1 = driver.findElement(By.xpath("//*[@type='radio' and @checked]"));
        assertEquals(radio1.getAttribute("id"), "my-radio-1");
        assertTrue(radio1.isSelected());

        WebElement radio2 = driver.findElement(By.xpath("//*[@type='radio' and not(@checked)]"));
        assertEquals(radio2.getAttribute("id"), "my-radio-2");
        assertFalse(radio2.isSelected());
    }
    @Test
    void testIdOrName(){

        WebElement fileElement = driver.findElement(new ByIdOrName("my-file"));
        assertTrue(fileElement.getAttribute("id").isBlank());
        assertFalse(fileElement.getAttribute("name").isBlank());
    }
    @Test
    void testComplexChained(){

        List<WebElement> rowsInForm = driver.findElements(new ByChained(By.tagName("form"), By.className("row")));
        assertEquals(rowsInForm.size(), 1);
    }
    @Test
    void testByAll(){

        List<WebElement> formRows = driver.findElements(new ByAll(By.tagName("form"), By.className("row")));
        assertEquals(formRows.size(), 5);
    }

    @Test
    void testRelativeLocators(){

        WebElement link = driver.findElement(By.linkText("Return to index"));
        RelativeLocator.RelativeBy relativeBy = RelativeLocator.with(By.tagName("input"));
        WebElement readOnly = driver.findElement(relativeBy.above(link));
        assertEquals(readOnly.getAttribute("name"), "my-readonly");
    }

    @Test
    void testDatePicker(){

        // get current date from the system clock
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentDay = today.getDayOfMonth();

        // click on the date picker to open calendar
        WebElement datePicker = driver.findElement(By.name("my-date"));
        datePicker.click();

        // click on the current month by searching html tag attributes
        WebElement monthElement = driver.findElement(By.xpath(String.format("//th[contains(text(),'%d')]", currentYear)));
        monthElement.click();

        // click on the left arrow using relative locators
        WebElement arrowLeft = driver.findElement(RelativeLocator.with(By.tagName("th")).toRightOf(monthElement));
        arrowLeft.click();

        // click on the current month of that year
        WebElement monthPastYear = driver.findElement(RelativeLocator.with(By.cssSelector("span[class$=focused]")).below(arrowLeft));
        monthPastYear.click();

        // click on the present day in that month
        WebElement dayElement = driver.findElement(By.xpath(String.format("//td[@class='day' and contains(text(), '%d')]", currentDay)));
        dayElement.click();

        // get final date on the input text
        String oneYearBack = datePicker.getAttribute("value");
        //log.debug("Final date in date picker: {}", oneYearBack);

        // verify date is equal to the one selected in date picker
        LocalDate previousYear = today.minusYears(1);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String expectedDate = previousYear.format(dateFormat);
        assertEquals(oneYearBack, expectedDate);
    }

} // end class
