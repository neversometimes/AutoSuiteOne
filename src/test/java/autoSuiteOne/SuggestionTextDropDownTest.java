package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.time.Duration;
import java.util.List;

public class SuggestionTextDropDownTest {

    WebDriver driver;

    @BeforeClass
    public void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  // basic implicit wait 10s
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void autoSuggestTest() throws Exception {

        driver.get("https://rahulshettyacademy.com/AutomationPractice/");

        //type "uni" into suggestion textbox
        WebElement suggestTxtBox = driver.findElement(By.id("autocomplete"));
        suggestTxtBox.sendKeys("uni");

        // add all dropdown suggestion options to suggestList List
        List<WebElement> suggestList = driver.findElements(By.cssSelector("li[class='ui-menu-item']"));

        // using stream(), filter the suggestlist to only "United States (USA)" option
        List<WebElement> filteredSuggestList = suggestList.stream().filter(s->s.getText().contains("United States (USA)")).toList();

        // get the first (only) filtered item and click it
        filteredSuggestList.getFirst().click();

        //verify United States (USA) appears in the textbox
            // note: must use .getAttribute to grab text value present in textbox; .getText() won't work
        assertEquals(suggestTxtBox.getAttribute("value"), "United States (USA)");
    }

}
