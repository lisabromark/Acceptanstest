package caliva.com;

import static org.junit.Assert.assertTrue;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TermsConditionsTest {

	public static FileHandler fh;
	public static Logger log;
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static String baseURL = "http://www.smythstoys.com/ie/en-ie/";
	private static String expected = "";
	private static String actual = "";
	private static String logFile = "log_file.txt";

	@BeforeClass
	public static void setupOnce() {
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);
		log = Logger.getLogger(SearchTest.class.getName());
		try {
			fh = new FileHandler(logFile);
		} catch (Exception e) {
			log.severe(e.getStackTrace().toString());
		}
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
		log.info("@BeforeClass setupOnce()");
	}

	@Before
	public void resetData() {
		driver.navigate().to(baseURL);
		log.info("@Before resetData()");
	}

	// Testing availability of Terms and Conditions

	@Test
	public void termsConditionsTest() {

		expected = "Terms & Conditions";

		String xpath_termsconditionsPage1 = "html/body/div[2]/div[3]/div/div/div/div[3]/div/ul/li[2]/a";
		String xpath_termsconditionsPage2 = "html/body/div[2]/div[2]/div[2]/div[1]/h4";

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_termsconditionsPage1)));
		driver.findElement(By.xpath(xpath_termsconditionsPage1)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_termsconditionsPage2)));

		String resultText = driver.findElement(By.xpath(xpath_termsconditionsPage2)).getText();

		log.info("Expected: " + expected);
		log.info("Actual: " + resultText);

		assertTrue("TermsConditionsTest Fail", resultText.contains(expected));

	}

	@After
	public void tearDown() {
		log.info("@After tearDown()");
	}

	@AfterClass
	public static void tearDownOnce() {
		log.info("@AfterClass tearDownOnce()");
		driver.close();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			log.severe(e.getStackTrace().toString());
		}
		driver.quit();
	}

}