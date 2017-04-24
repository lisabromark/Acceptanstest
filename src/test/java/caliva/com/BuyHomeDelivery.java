package caliva.com;

import static org.junit.Assert.assertEquals;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BuyHomeDelivery {
	
	public static FileHandler fh;
	public static Logger log;
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static String baseURL = "http://www.smythstoys.com/ie/en-ie/";
	private static String expected = "";
	private static String actual = "";
	//private static String logFile = "C:\\Users\\readsoft\\workspace\\smyths\\SearchTest.log";
	private static String logFile = "C:\\Temp\\BuyHomeDelivery.log";
	
	@BeforeClass
	public static void setupOnce(){
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 20);
		log = Logger.getLogger(SearchTest.class.getName());
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
	
	//Test to initiate buying from Shopping basket
	//Test 7a: buy with Home delivery
	@Test
	public void selectHomeDeliveryTest7a() {	
		String xpath_guestCheckoutButton = "//a[contains(@data-event,'Go to Quick Checkout Button')]";
		expected = "Guest Checkout";
		
		log.info("selectHomeDeliveryTest7a() - START");

		//Perform search: test case 1a
		doSearch();
		//Add item to basket: test case 5
		doAddToBasket();
		
		//Go to shopping basket, click Home Delivery and wait for next page
		doSelectHomeDelivery();	
		WebElement guestCheckoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_guestCheckoutButton)));

		//Assert that text on button is "Guest Checkout"
		actual = guestCheckoutButton.getText();
		
		log.info("Expected: " + expected);
		log.info("Actual: " + actual);

		assertEquals("selectHomeDeliveryTest7a - FAIL", expected, actual);
		log.info("selectHomeDeliveryTest7a() - END");
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

	public void doSearch(){
		String input = "Lego";
		String webid_searchField = "q";
		String webid_searchButton = "main-search-button";
		String webid_product = ".//*[@id='141295']";
		
		log.info("doSearch() - START");

		WebElement searchField = driver.findElement(By.id(webid_searchField));				
		searchField.sendKeys(input);
		driver.findElement(By.id(webid_searchButton)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(webid_product)));
		log.info("doSearch() - END");
	}

	public void doAddToBasket(){
		String webid_product = ".//*[@id='141295']";
		String webid_addButton = "main-add-to-basket";
		String webid_Price = ".//*[@id='inner-mini']/div/span";
		
		log.info("doAddToBasket() - START");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(webid_product)));
		
		//Click on a product
		driver.findElement(By.xpath(webid_product)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(webid_addButton)));

		//Click Add to basket
		driver.findElement(By.id(webid_addButton)).click();
		
		//Wait for basket button to update i.e. the price has changed
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(webid_Price)));
		String origPrice = driver.findElement(By.xpath(webid_Price)).getText();
		boolean updated = false;
		while(!updated){
			try{
				String newPrice = driver.findElement(By.xpath(webid_Price)).getText();
				if (!newPrice.equals(origPrice))
					updated = true;
			}catch(StaleElementReferenceException e){
				updated = true;
				log.warning(e.getMessage().toString());
			}
		}
		
		log.info("doAddToBasket() - END");
	}

	public void doSelectHomeDelivery(){
		log.info("doSelectHomeDelivery() - START");
		String webid_buyonlineButton = "btnBuyOnline";
		String xpath_guestCheckoutButton = "//a[contains(@data-event,'Go to Quick Checkout Button')]";

		//Go to shopping basket page
		driver.navigate().to(baseURL + "basket");
		WebElement homeDeliveryButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(webid_buyonlineButton)));
		
		//Click Home delivery-button
		homeDeliveryButton.click();		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_guestCheckoutButton)));
		
		log.info("doSelectHomeDelivery() - END");	
	}
}

