package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.*;

public class OSNotificationsTest {

    WebDriver driver;

    @BeforeTest
    void setup() {
        //  This setup section includes code to allow notifications in Chrome

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 1);
        options.setExperimentalOption("prefs", prefs);

        driver = WebDriverManager.chromedriver().capabilities(options).create();

    }
    @AfterTest
    void teardown() {
        driver.quit();
    }
    @Test
    public void testNotifications(){

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/notifications.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // This JS returns a String from the notification

        String script = String.join("\n",
                "const callback = arguments[arguments.length - 1];",  // callback function to end the script
                "const OldNotify = window.Notification;",               // store a copy of the original constructor
                "function newNotification(title, options) {",        // create new constructor for notifications
                "     callback(title);" ,                           // pass msg title as arg in callback
                "     return new OldNotify(title, options);",   // use old constructor to create original notificaton object
                "}",
                "newNotification.requestPermission = " +
                        "OldNotify.requestPermission.bind(OldNotify);" ,
                "Object.defineProperty(newNotification, 'permission', {" ,
                "     get: function() {" ,
                "         return OldNotify.permission;" ,
                "     }" ,
                "});" ,
                "window.Notification = newNotification;" ,
                "document.getElementById('notify-me').click();") ;  // btn click triggers notification on web page

        Object notificationTitle = js.executeAsyncScript(script);

        assertEquals(notificationTitle, "This is a notification");  // verfy notification title is as expected

    }

} // end class
