package caliva.com;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CheckTime {
	
	public static FileHandler fh;
	public static Logger log;
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static String baseURL = "http://www.smythstoys.com/ie/en-ie/";
	private static String logFile = "log_file.txt";
	
	@BeforeClass
	public static void setupOnce(){
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);
		log = Logger.getLogger(CheckTime.class.getName());
		try{
			fh = new FileHandler(logFile);
		} catch(Exception e){
			log.severe(e.getStackTrace().toString());
		}
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
		log.info("@BeforeClass setupOnce()");
	}
	
	@Before
	public void resetData(){
		driver.navigate().to(baseURL);
		log.info("@Before resetData()");
	}
	
	//Test 17a: check response time for page Store locator - should be below 3 seconds
	@Test(timeout = 3000)
	public void checkTimeStoreLocator17a() {	
		String xpath_storeLocator = "//a[contains(@href,'storelocator')]";
		String xpath_mapIframe = "//div[@id='map-canvas']/div/div/div/iframe";

		log.info("checkTimeStoreLocator17a() - START");
		
		driver.findElement(By.xpath(xpath_storeLocator)).click();
		
		//Wait for iframe containing map
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_mapIframe)));

		log.info("checkTimeStoreLocator17a() - END");
	}

	@After
	public void tearDown() {
		log.info("@After tearDown()");
	}
	
	@AfterClass
	public static void tearDownOnce(){
		log.info("@AfterClass tearDownOnce()");
		driver.close();
		try{
			Thread.sleep(3000);
		} catch(InterruptedException e){
			log.severe(e.getStackTrace().toString());
		}
		driver.quit();		
	}
	
}

