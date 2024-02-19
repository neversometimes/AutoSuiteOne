package autoSuiteOne;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.print.PrintOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class PrintPageTest {

    WebDriver driver;

    @BeforeSuite
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeTest
    void setup () {driver = WebDriverManager.chromedriver().create();}

    @AfterTest
    void teardown() {driver.quit();}

    @Test
    void testPrintPage() throws IOException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        PrintsPage pg = (PrintsPage) driver;                // cast driver object to PrintsPage
        PrintOptions printOptions = new PrintOptions();
        Pdf pdf = pg.print(printOptions);               // print current web page to PDF

        String pdfBase64 = pdf.getContent();            // put PDF in Base64 format
        assertTrue(pdfBase64.contains("JVBER"));        // verify Base64 PDF content

        byte[] decodedImg = Base64.getDecoder()
                .decode(pdfBase64.getBytes(StandardCharsets.UTF_8)); // convert Base64 PDF to byte array

        Path destinationFile = Paths.get("my-pdf.pdf");
        Files.write(destinationFile, decodedImg);               // write out byte array as PDF file
        
    }

} // end class
