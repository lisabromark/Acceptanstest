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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BuyGuestCheckout {
	
	public static FileHandler fh;
	public static Logger log;
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static String baseURL = "http://www.smythstoys.com/ie/en-ie/";
	//private static String logFile = "C:\\Users\\readsoft\\workspace\\smyths\\SearchTest.log";
	private static String logFile = "log_file.txt";
	private static String totalCost = "";
	private static String firstName = "Mary";
	private static String lastName = "O'Reilly";
	private static String street1 = "Wood Lane";
	private static String street2 = "3";
	private static String city = "Dublin";
	private static String county = "Dublin";
	private static String email = "mary@home.ie";
	private static String phone = "(01)2953052";
	private static String deliveryInfo = "Quick delivery, please!";

	
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
	
	//Test to buy item as guest without logging in
	@Test
	public void selectGuestCheckoutTest8() {	
		String xpath_street1_PayPage = "//input[(@id='AddressLine1') and (@type='hidden')]";
		String xpath_street2_PayPage = "//input[(@id='AddressLine2') and (@type='hidden')]";
		String xpath_city_PayPage = "//input[(@id='CityTown') and (@type='hidden')]";
		String webid_county_PayPage = "Region";
		String xpath_finalTotalCost = ".//*[@id='checkoutBasket']/ul/div/li[2]/div/span[2]";

		log.info("selectGuestCheckoutTest8() - START");

		//Perform search: test case 1a
		doSearch();
		//Add item to basket: test case 5
		doAddToBasket();
		//Go to shopping basket and select Home Delivery: test case 7a
		doSelectHomeDelivery();

		//Go to delivery details page and enter name/address information
		doEnterDeliveryAddressInfo();
		
		//Go to payment details page and use same name/address info as on previous page
		doEnterPaymentAddressInfo();
		
		String finalTotalCost = driver.findElement(By.xpath(xpath_finalTotalCost)).getText().substring(1);

		//Assert that name and price are the same as on previous page		
		String actualStreet1 = driver.findElement(By.xpath(xpath_street1_PayPage)).getAttribute("value");
		String actualStreet2 = driver.findElement(By.xpath(xpath_street2_PayPage)).getAttribute("value");
		String actualCity = driver.findElement(By.xpath(xpath_city_PayPage)).getAttribute("value");
		Select countyDropdown = new Select(driver.findElement(By.id(webid_county_PayPage)));
		String actualCounty = countyDropdown.getFirstSelectedOption().getText();

		log.info("Expected: " + street1 + " " + street2 + " " + city + " " + county + " " + totalCost);
		log.info("Actual: " + actualStreet1 + " " + actualStreet2 + " " + actualCity + " " + actualCounty + " " + finalTotalCost);

		//Firstname, lastname and phone can't be asserted since they aren't saved in value-attributes on html-page
		//Not sure if it's a bug or if they are not interested in using these values
		assertEquals("selectGuestCheckoutTest8 - STREET1 FAIL", street1, actualStreet1);
		assertEquals("selectGuestCheckoutTest8 - STREET2 FAIL", street2, actualStreet2);
		assertEquals("selectGuestCheckoutTest8 - CITY FAIL", city, actualCity);
		assertEquals("selectGuestCheckoutTest8 - COUNTY FAIL", county, actualCounty);		
		assertEquals("selectGuestCheckoutTest8 - TOTAL COST FAIL", totalCost, finalTotalCost);		
		
		log.info("selectGuestCheckoutTest8() - END");
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
		String xpath_totalCost = ".//*[@id='total-and-vouchers']/div/div[2]/div[2]";

		//Go to shopping basket page
		driver.navigate().to(baseURL + "basket");
		WebElement homeDeliveryButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(webid_buyonlineButton)));
		
		//Save total cost in global variable to assert in last step
		totalCost = driver.findElement(By.xpath(xpath_totalCost)).getText().substring(1);
			
		//Click Home delivery-button
		homeDeliveryButton.click();		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_guestCheckoutButton)));
		
		log.info("doSelectHomeDelivery() - END");	
	}
	
	public void doEnterDeliveryAddressInfo(){
		log.info("doEnterDeliveryAddressInfo() - START");
		String xpath_guestCheckoutButton = "//a[contains(@data-event,'Go to Quick Checkout Button')]";
		String webid_freeShippingRadioButton = "19";
		String webid_firstName = "FirstName";
		String webid_lastName = "LastName";
		String webid_street1 = "AddressLine1";
		String webid_street2 = "AddressLine2";
		String webid_city = "CityTown";
		String webid_county = "Region";
		String webid_email = "Email";
		String webid_confirmEmail = "ConfirmEmail";
		String webid_phone = "Mobile";
		String webid_deliveryInfo = "checkout-payment-delivery";

		//Click GuestCheckout-button and wait for next page
		driver.findElement(By.xpath(xpath_guestCheckoutButton)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(webid_freeShippingRadioButton)));

		//Fill in all fields in form
		driver.findElement(By.id(webid_firstName)).sendKeys(firstName);
		driver.findElement(By.id(webid_lastName)).sendKeys(lastName);
		driver.findElement(By.id(webid_street1)).sendKeys(street1);
		driver.findElement(By.id(webid_street2)).sendKeys(street2);
		driver.findElement(By.id(webid_city)).sendKeys(city);
		driver.findElement(By.id(webid_email)).sendKeys(email);
		driver.findElement(By.id(webid_confirmEmail)).sendKeys(email);
		driver.findElement(By.id(webid_phone)).sendKeys(phone);
		driver.findElement(By.id(webid_deliveryInfo)).sendKeys(deliveryInfo);		
		Select countyDropdown = new Select(driver.findElement(By.id(webid_county)));
		countyDropdown.selectByValue(county);
		
		log.info("doEnterDeliveryAddressInfo() - END");
	}
	
	public void doEnterPaymentAddressInfo(){
		log.info("doEnterPaymentAddressInfo() - START");
		String webid_sameAsDeliveryButton = "check_billingAddresSameDeliveryAddress";
		String webid_proceedToPaymentButton = "CreditCard";
		String webid_buyNowButton = "btn-proceed-pay";

		//Click button Proceed to Payment and wait for next page
		driver.findElement(By.id(webid_proceedToPaymentButton)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(webid_buyNowButton)));		

		//Click checkbox "Same as delivery" to use same name/address values as filled in on previous page
		driver.findElement(By.id(webid_sameAsDeliveryButton)).click();		
		
		log.info("doEnterPaymentAddressInfo() - END");
	}
}

