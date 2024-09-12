package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static org.testng.Assert.*;

import java.time.Duration;

public class MultiElementTest {
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
    public void testMultipleElements() {
        // practice exercise - don't hardcode any label strings - use only variables
        // 1. at practice website, select (check) one of the checkboxes
        // 2. label from that checkbox select from dropdown control
        // 3. enter text from dropdown into textbox
        // 4. click 'Alert' button
        // 5. verify alert button text contains text from checkbox in step 2

        driver.get("https://rahulshettyacademy.com/AutomationPractice/");

        WebElement chkBox3 = driver.findElement(By.cssSelector("input[id='checkBoxOption3']"));
        chkBox3.click();

        WebElement chkbox3Label = driver.findElement(By.cssSelector("label[for='honda']"));
        String chkBoxLabelTxt = chkbox3Label.getText();

        Select select = new Select(driver.findElement(By.id("dropdown-class-example")));
        select.selectByVisibleText(chkBoxLabelTxt);
        String dropDownLabelTxt = select.getFirstSelectedOption().getText();
        //    System.out.println(dropDownLabelTxt);

        WebElement txtBox = driver.findElement(By.id("name"));
        txtBox.sendKeys(dropDownLabelTxt);
        WebElement alertBtn = driver.findElement(By.id("alertbtn"));
        alertBtn.click();
        //    System.out.println(driver.switchTo().alert().getText());
        assertEquals(driver.switchTo().alert().getText(), "Hello " + dropDownLabelTxt
                                            + ", share this practice page and share your knowledge");
    }

}
