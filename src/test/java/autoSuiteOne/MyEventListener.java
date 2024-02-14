package autoSuiteOne;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.events.WebDriverListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEventListener implements WebDriverListener  {

    @Override
    public void afterGet(WebDriver driver, String url) {
        WebDriverListener.super.afterGet(driver, url);
        takeScreenshot(driver);
    }

    @Override
    public void beforeQuit(WebDriver driver) {
        takeScreenshot(driver);
    }

    private void takeScreenshot(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);  //screenshot is a FILE
        SessionId sessionID = ((RemoteWebDriver) driver).getSessionId();
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy.MM.dd_HH.mm.ss.SSS");
        String screenshotFileName = String.format("%s-%s.png", dateFormat.format(today), sessionID.toString());
        Path destination = Paths.get(screenshotFileName);

        try{
            Files.move(screenshot.toPath(), destination);  // move screenshot into project path
        } catch (IOException e) {
            System.out.println("IOException moving screenshot file. These things happen.");
        }
    }

} // close class
