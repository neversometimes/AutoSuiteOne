package autoSuiteOne;

import static org.testng.AssertJUnit.*;             // TestNG
import org.testng.annotations.*;
import static org.assertj.core.api.Assertions.*;   // AssertJ
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.*;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class BrowserAgnosticTest {
    static WebDriver driver;
    static JavascriptExecutor js;
    static TakesScreenshot ts;

    @BeforeSuite
    void setup() {
        System.out.println("Setup BrowserAgnosticTests");
        driver = WebDriverManager.chromedriver().create();

    }
    @AfterSuite
    void teardown() {
        System.out.println("teardown BrowserAgnosticTests");
        driver.quit();
    }

    @Test
    public void testScrollByPixels() {

        System.out.println("testScrollByPixels");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");
        js = (JavascriptExecutor) driver;

        String scriptDown = "window.scrollBy(0, 5000);";  //scrolls down by x,y pixels
        String scriptUp = "window.scrollBy(0, -5000);";   //scrolls up by x,y pixels

        js.executeScript(scriptDown);  // scroll down
        js.executeScript(scriptUp);  // scroll up

    }
    @Test
    public void testScrollIntoView() {
        System.out.println("testScrollIntoView");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");
        js = (JavascriptExecutor) driver;

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement lastElement = driver.findElement(By.cssSelector("p:last-child"));

        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, lastElement);

        WebElement copyrightText = driver.findElement(By.className("text-muted"));
        assertTrue(copyrightText.isDisplayed());
    }
    @Test
    public void testInfiniteScroll() {
        System.out.println("testInfiniteScroll");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/infinite-scroll.html");
        js = (JavascriptExecutor) driver;
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));  //explicit wait for page load

        By pLocator = By.tagName("p");

        List<WebElement> paragraphs = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, 0));
        int initParagraphsNumber = paragraphs.size(); //find the initial number of paragraphs on the page


        // find the last paragraph on the page
        WebElement lastParagraph = driver.findElement(By.xpath(String.format("//p[%d]", initParagraphsNumber)));

        String script = "arguments[0].scrollIntoView();";  //JS to scroll to last virtual paragraph on the page
        js.executeScript(script, lastParagraph); // scroll down

        // wait until there are more paragraphs available on the page
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, initParagraphsNumber));

    }
    @Test
    public void testColorPicker() {
        System.out.println("testColorPicker");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        js = (JavascriptExecutor) driver;

        WebElement colorPicker = driver.findElement(By.name("my-colors"));
        String initColor = colorPicker.getAttribute("value");

        Color red = new Color(255, 0, 0, 1);
        String script = String.format("arguments[0].setAttribute('value', '%s');", red.asHex());
        js.executeScript(script, colorPicker);

        String finalColor = colorPicker.getAttribute("value");

        assertNotSame(finalColor, initColor);  // check color changed
        assertEquals(Color.fromString(finalColor), red);  // check color changed to red

    }
    @Test
    public void testPinnedScripts() throws IOException {
        System.out.println("testPinnedScripts");

        String initPage = "https://bonigarcia.dev/selenium-webdriver-java/";
        driver.get(initPage);
        js = (JavascriptExecutor) driver;

        ScriptKey linkKey = js.pin("return document.getElementsByTagName('a')[2];");  //attach 1st JS fragment
        ScriptKey firstArgkey = js.pin("return arguments[0];");  //attach 2nd JS fragment

        Set<ScriptKey> pinnedScripts = js.getPinnedScripts();
        assertEquals(pinnedScripts.size(), 2);

        WebElement formLink = (WebElement) js.executeScript(linkKey);
        formLink.click();
        assertNotSame(driver.getCurrentUrl(), initPage);

        String message = "Hello Earth!";
        String executeScript = (String) js.executeScript(firstArgkey, message);
        assertEquals(executeScript, message);

        js.unpin(linkKey);
        assertEquals(js.getPinnedScripts().size(), 1);
    }
    @Test
    public void testAsyncScript() {
        System.out.println("testAsyncScript");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        js = (JavascriptExecutor) driver;

        Duration pause = Duration.ofSeconds(5);  // 5 second pause time for the JS to be run
        String script = "const callback = arguments[arguments.length - 1];"
                                            + "window.setTimeout(callback, " + pause.toMillis() + ");";

        long initMills = System.currentTimeMillis();
        js.executeAsyncScript(script);  // ASYNC JS execution call

        Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - initMills);  // measure time elasped

        assertTrue(elapsed.toMillis() >= pause.toMillis());  // assert True if elapsed time >= pause time
    }

   /* @Test
    public void testPageLoadTimeout() {
        System.out.println("testPageLoadTimout");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(1));  // set min possible page load time = 1ms

        //  This is an example of a negative test.
        //  the assert expects an WebDriver timeout exception to be thrown.
        //  Need to use Try/Catch block

        assertThatThrownBy(() -> driver
                .get("https://bonigarcia.dev/selenium-webdriver-java/"))  // load web page
                .isInstanceOf(TimeoutException.class);                  // throws exception b/c page can't load
                                                                        // less than 1ms (pageload vc timeout)
                                                            // NOTE: test passes because an exception is thrown

    }
*/
    @Test
    public void testScriptTimeout() throws IOException {
        System.out.println("testScriptTimout");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        js = (JavascriptExecutor) driver;

        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(3)); // set Script load timeout = 3s

        assertThatThrownBy(() -> {
            long waitMillis = Duration.ofSeconds(5).toMillis(); // set Script wait time = 5s
            String script = "const callback = arguments[arguments.length - 1];"
                    + "window.setTimeout(callback, " + waitMillis + ");"; // script execution time lasts 5s
            js.executeAsyncScript(script);
        }).isInstanceOf(ScriptTimeoutException.class); // throws an exception because script execution time (5s)
                                                        // is greater than script timeout (3s)
    }
    @Test
    public void testPageScreenShotPng() throws IOException {
        System.out.println("testPageScreenShotPng");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        ts = ((TakesScreenshot) driver);

        File screenshotFile = ts.getScreenshotAs(OutputType.FILE);  // take screenshot PNG default file type

        Path destinationPath = Paths.get("testPageScreenshot.png");  //find destination path

        Files.move(screenshotFile.toPath(), destinationPath, REPLACE_EXISTING); //move file to destination

        assertThat(destinationPath).exists(); //assert the screenshot was created
    }
    @Test
    public void testPageScreenShotBase64() throws IOException {
        System.out.println("testPageScreenShotBase64");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        ts = ((TakesScreenshot) driver);

        String screenshot = ts.getScreenshotAs(OutputType.BASE64); // screenshot in Base64

        // System.out.println ("image/png;base64" + screenshot); // paste this result into browser to verify

        assertThat(screenshot).isNotEmpty();  // data capture successful
    }
    @Test
    public void testWebElementScreenShot() throws IOException {
        System.out.println("testWebElementScreenShot");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        WebElement form = driver.findElement(By.tagName("form"));  // grab web element by tagname
        File screenshotFile = form.getScreenshotAs(OutputType.FILE); // take screenshot
        Path destinationPath = Paths.get("testWebElementScreenshot.png");
        Files.move(screenshotFile.toPath(), destinationPath, REPLACE_EXISTING);

        assertThat(destinationPath).exists(); //assert the screenshot was created
    }
    @Test
    public void testBrowserWindow() {
        System.out.println("testBrowserWindow");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        WebDriver.Window window = driver.manage().window();
        Point initialPosition = window.getPosition();     // grab initial position and size of browser window
        Dimension initialSize = window.getSize();

        window.maximize();  // maximize browser window

        Point maximizedPosition = window.getPosition();   // grab max browser window position and size
        Dimension maximizedSize = window.getSize();

        assertThat(initialPosition).isNotEqualTo(maximizedPosition);  // check window position changed
        assertThat(initialSize).isNotEqualTo(maximizedSize);    // check window size changed

        window.minimize();    // minimize browser window
        window.fullscreen();  // return to full screen window (not max/not min)
    }
    @Test
    public void testBrowserHistory() {
        System.out.println("testBrowserHistory");

        String baseURL = "https://bonigarcia.dev/selenium-webdriver-java/";
        String firstPage = baseURL + "navigation1.html";
        String secondPage = baseURL + "navigation2.html";
        String thirdPage = baseURL + "navigation3.html";

        driver.get(firstPage);
        assertThat(driver.getCurrentUrl()).isEqualTo(firstPage);
        driver.navigate().to(secondPage);
        assertThat(driver.getCurrentUrl()).isEqualTo(secondPage);
        driver.navigate().to(thirdPage);
        assertThat(driver.getCurrentUrl()).isEqualTo(thirdPage);
        driver.navigate().back();
        assertThat(driver.getCurrentUrl()).isEqualTo(secondPage);
        driver.navigate().forward();
        assertThat(driver.getCurrentUrl()).isEqualTo(thirdPage);
        driver.navigate().back();
        assertThat(driver.getCurrentUrl()).isEqualTo(secondPage);
        driver.navigate().back();
        assertThat(driver.getCurrentUrl()).isEqualTo(firstPage);
        driver.navigate().forward();
        assertThat(driver.getCurrentUrl()).isEqualTo(secondPage);
        driver.navigate().refresh();
        assertThat(driver.getCurrentUrl()).isEqualTo(secondPage);

    }
    @Test
    public void testShadowDOM() {

        System.out.println("testShadowDOM");

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html");

        WebElement content = driver.findElement(By.id("content"));
        SearchContext shadowRoot = content.getShadowRoot();

        WebElement textElement = shadowRoot.findElement(By.cssSelector("p"));
        assertThat(textElement.getText()).contains("Hello Shadow DOM");

    }
    @Test
    public void testCreateCookies() {  // CREATE COOKIE

        System.out.println("testCreateCookies");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/cookies.html");
        Options options = driver.manage();

        Cookie newCookie = new Cookie("new-cookie-key", "new-cookie-value");  // create new cookie
        options.addCookie(newCookie);  // add cookie to current page

        String readValue = options.getCookieNamed(newCookie.getName()).getValue();  // read new cookie value
        assertThat(newCookie.getValue()).isEqualTo(readValue); // verify new cookie value is correct

        driver.findElement(By.id("refresh-cookies")).click();  // refresh to check cookies in page UI

        Cookie newCookieName = options.getCookieNamed("new-cookie-key");  //read the new added cookie

        options.deleteCookie(newCookieName);   //  delete the username cookie
    }
    @Test
    public void testReadCookies(){     // READ COOKIE

        System.out.println("testReadCookies");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/cookies.html");
        Options options = driver.manage();

        Set<Cookie> cookies = options.getCookies();  // read all cookies on page
        assertThat(cookies).hasSize(2);  // default page has 2 cookies

        Cookie username = options.getCookieNamed("username");  // get username cookie
        assertThat(username.getValue()).isEqualTo("John Doe");  // verify value of cookie
        assertThat(username.getPath()).isEqualTo("/");  // verify path of cookie

        driver.findElement(By.id("refresh-cookies")).click();  // refresh to check cookies in page UI
    }
    @Test
    public void testUpdateCookies() {   //  UPDATE COOKIE

        System.out.println("testUpdateCookies");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/cookies.html");
        Options options = driver.manage();

        Cookie username = options.getCookieNamed("username");  // get username cookie
        Cookie editedCookie = new Cookie(username.getName(), "new-value");  // update cookie name
        options.addCookie(editedCookie);  // add updated cookie to page

        Cookie readCookie = options.getCookieNamed(username.getName()); // read cookie just added
        assertThat(editedCookie.getValue()).isEqualTo(readCookie.getValue());     // verify cookie name updated

        driver.findElement(By.id("refresh-cookies")).click();  // refresh to check cookies in page UI
    }
    @Test
    public void testDeleteCookies() {   //  DELETE COOKIE

        System.out.println("testDeleteCookies");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/cookies.html");
        Options options = driver.manage();

        Set<Cookie> cookies = options.getCookies();   // read all cookies of page
        Cookie username = options.getCookieNamed("username");  //read the username cookie

        options.deleteCookie(username);   //  delete the username cookie

        assertThat(options.getCookies()).hasSize(cookies.size() - 1);  // verifies a cookie got
                                                                                // deleted from array

        driver.findElement(By.id("refresh-cookies")).click();  // refresh to check cookies in page UI
    }
    @Test
    public void testDropDownList() {

        System.out.println("testDropDownList");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        Select select = new Select(driver.findElement(By.name("my-select"))); // find dropdown select
        String optionLabel = "Three";
        select.selectByVisibleText(optionLabel);  // select option labeled "Three"

        assertThat(select.getFirstSelectedOption().getText())
                .isEqualTo(optionLabel); // verify default item displayed == "Three" option in dropdown

    }
    @Test
    public void testDataList() {

        System.out.println("testDataList");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        WebElement dataList = driver.findElement(By.name("my-datalist")); //find datalist web element
        dataList.click();  // mouse click datalist to set focus in web element control

        WebElement option = driver
                .findElement(By.xpath("//datalist/option[2]")); // find 2nd option in list
        String optionValue = option.getAttribute("value"); // get 2nd option value ("New York")
        dataList.sendKeys(optionValue);  // enter "New York" to select that option of dropdown

        assertThat(optionValue).isEqualTo("New York");  // verify text selected and typed is "New York"

    }
    @Test
    public void testOpenNewTab() {

        System.out.println("testOpenNewTab");
        String initURL = "https://bonigarcia.dev/selenium-webdriver-java/";
        driver.get(initURL);
        String initHandle = driver.getWindowHandle();  // get window handle

        driver.switchTo().newWindow(WindowType.TAB);  // open new TAB
        String nextURL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(nextURL);  // open new page in TAB
        assertThat(driver.getWindowHandles().size()).isEqualTo(2); // 2 window handles currently

        String nextHandle = driver.getWindowHandle(); // grab handle of new TAB window

        driver.switchTo().window(initHandle);  // switch back to original window
        driver.close();                         // close original browser window
        assertThat(driver.getWindowHandles().size()).isEqualTo(1);  // verify window handle count == 1

        driver.switchTo().window(nextHandle);   // switch driver to TAB window via handle
        assertEquals(driver.getCurrentUrl(), nextURL); // verify current URL == TAB window URL
    }
    @Test
    public void testOpenNewWindow() {

        System.out.println("testOpenNewWindow");
        String initURL = "https://bonigarcia.dev/selenium-webdriver-java/";
        driver.get(initURL);
        String initHandle = driver.getWindowHandle();  // get window handle

        driver.switchTo().newWindow(WindowType.WINDOW);  // open new WINDOW
        String nextURL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(nextURL);  // open new page in NEW WINDOW
        assertThat(driver.getWindowHandles().size()).isEqualTo(2); // 2 window handles currently

        String nextHandle = driver.getWindowHandle(); // grab handle of new WINDOW

        driver.switchTo().window(initHandle);  // switch back to original window
        driver.close();                         // close original browser window
        assertThat(driver.getWindowHandles().size()).isEqualTo(1);  // verify window handle count == 1

        driver.switchTo().window(nextHandle);   // switch driver to NEW WINDOW via handle
        assertEquals(driver.getCurrentUrl(), nextURL); // verify current URL == NEW WINDOW URL
    }
    @Test
    public void testiFrames() {

        System.out.println("testiFrames");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/iframes.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // wait 10s for iFrame to load
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("my-iframe"));

        By pName = By.tagName("p");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pName, 0)); // waiting for paragraphs
        List<WebElement> paragraphs = driver.findElements(pName); // find all paragraphs in iFrame
        assertThat(paragraphs).hasSize(20); // verify 20 paragraphs found in iFrame
    }
    @Test
    public void testFrames() {

        System.out.println("testFrames");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/frames.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // wait 10s max
        String frameName = "frame-body";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(frameName))); // wait until found & loaded

        driver.switchTo().frame(frameName);  // switch to frame body element

        By pName = By.tagName("p");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pName, 0)); // wait to load paragraphs
        List<WebElement> paragraphs = driver.findElements(pName);  // find all paragraphs in FRAME
        assertThat(paragraphs).hasSize(20);  // verify 20 paragraphs found in FRAME
    }
    @Test
    public void testAlerts() {

        System.out.println("testAlerts");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("my-alert")).click();   // click alert button web element
        wait.until(ExpectedConditions.alertIsPresent()); // wait for alert to appear
        Alert alert = driver.switchTo().alert();         // put focus on alert UI

        assertThat(alert.getText()).isEqualTo("Hello world!"); // verify alert UI text

        alert.accept();   // click OK on alert to dismiss
    }
    @Test
    public void testConfirms() {

        System.out.println("testConfirms");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("my-confirm")).click();  // click confirm web element
        wait.until(ExpectedConditions.alertIsPresent());  // wait for confirm to appear
        Alert confirm = driver.switchTo().alert();        // set focus on confirm UI

        assertThat(confirm.getText()).isEqualTo("Is this correct?"); // verify confirm UI text

        confirm.dismiss();  //click Cancel
        // cancel displays "You chose: false" text below web element on page
        // add assert to verify?
    }
    @Test
    public void testPrompts() {

        System.out.println("testPrompts");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("my-prompt")).click();  // click prompt web element
        wait.until(ExpectedConditions.alertIsPresent()); // wait for prompt to appear
        Alert prompt = driver.switchTo().alert();        // set focus on prompt UI

        assertThat(prompt.getText()).isEqualTo("Please enter your name"); // verify prompt UI text

        prompt.sendKeys("John Doe"); // text input on prompt
        prompt.accept();

        // OK on prompt sets text below web element on the page = "You typed: John Doe"
        // cancel on prompt sets text below web element on the page = "You typed: null"
        // add assert to verify?
    }
    @Test
    public void testModals() {

        System.out.println("testModals");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("my-modal")).click(); // click modal web element
        WebElement close = driver
                .findElement(By.xpath("//button[text() = 'Close']")); // find modal Close button

        assertThat(close.getTagName()).isEqualTo("button"); // verify tag name on Close button

        wait.until(ExpectedConditions.elementToBeClickable(close));  // wait for modal to be clicked
        close.click();  // click modal close button
        // verify other text on modal
        // verify text below modal web element that is returned from modal alert action
    }
    @Test
    public void testWebStorage() {

        System.out.println("testWebStorage");
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-storage.html");
        WebStorage webStorage = (WebStorage) driver;

        SessionStorage sessionStorage = webStorage.getSessionStorage();

        assertThat(sessionStorage.size()).isEqualTo(2);

        sessionStorage.setItem("new element", "new value");
        assertThat(sessionStorage.size()).isEqualTo(3);

        driver.findElement(By.id("display-session")).click();
    }

} //close class
