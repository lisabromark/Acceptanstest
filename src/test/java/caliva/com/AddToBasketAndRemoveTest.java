package caliva.com;

import static org.junit.Assert.*;

import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddToBasketAndRemoveTest {

	public static FileHandler fh;
	public static Logger log;
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static String baseURL = "http://www.smythstoys.com/ie/en-ie/";
	private static String searchWord = "lego";
	private static By fldSearch = By.id("q");
	private static By btnSearch = By.id("main-search-button");
	private static By listOfProducts = By.className("item");
	private static By basketPrice = By.className("basket-price");
	private static By btnAddToBasket = By.id("main-add-to-basket");
	private static String expected = "€0.00";
	private static By removeItemLink = By.partialLinkText("remove");
	private static By basketIcon = By.id("inner-mini");
    private static By btnCollect = By.id("btnCollect");
    private static By emptyBasketMsg = By.className("basket-noitem-warning");

	@BeforeClass
	public static void beforeClass(){
		driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 20);
        driver.get(baseURL); 
  		doSearch();
        log = Logger.getLogger(SearchTest.class.getName());
		try{
			fh = new FileHandler("log_file.txt");
		} catch(Exception e){
			log.severe(e.getStackTrace().toString());
		}
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
		log.info("@BeforeClass beforeClass()");
	}
	
	//Testing add to basket buttons visibility and functionality and remove link
	//Test 5: add by pressing the button
	//Test 6: remove by clicking on remove link
	
	@Test
	public void addToBasketAndRemove() {	
		//find first element in list and click on it		
		findElements(listOfProducts).get(0).click();
		//check that basket is empty (price=0)
		assertTrue(findElement(basketPrice).getText().contentEquals(expected));
		//add item to basket
		findElement(btnAddToBasket).click();
		//find remove link in pop-up window
		findElement(removeItemLink);
		//get price after adding item to basket
		String actual= driver.findElement(basketPrice).getText();
		log.info("Expected: " + expected);
		log.info("Actual: " + actual);
		assertFalse(actual.contains(expected));
		//removing item from basket in next window
		findElement(basketIcon).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(btnCollect));
		//find all remove on the page and click on the last one
		findElements1(removeItemLink).get(findElements1(removeItemLink).size()-1).click();
		//message shown when basket is empty
		expected="Your shopping bag is now empty!";
		actual= findElement(emptyBasketMsg).getText(); 
		log.info("Expected: " + expected);
		log.info("Actual: " + actual);
		assertTrue(actual.contains(expected));	
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
	
	public static void doSearch(){
		findElement(fldSearch).sendKeys(searchWord);
		findElement(btnSearch).click();
	}
	
	
	public static WebElement findElement(By by){
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		return driver.findElement(by);
	}
	
	public List<WebElement> findElements(By by){
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return driver.findElements(by);		 		
	}
	
	public List<WebElement> findElements1(By by){
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		return driver.findElements(by);		 		
	}
}
