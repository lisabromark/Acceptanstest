package caliva.com;

import static org.junit.Assert.assertTrue;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SearchTest {
	
	public static FileHandler fh;
	public static Logger log;
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static String baseURL = "http://www.smythstoys.com/ie/en-ie/";
	private static String expected = "";
	private static String actual = "";
	private static String logFile = "C:\\Users\\readsoft\\workspace\\smyths\\SearchTest.log";
	
	@BeforeClass
	public static void setupOnce(){
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);
		log = Logger.getLogger(SearchTest.class.getName());
		try{
		
			fh = new FileHandler("C:\\Users\\readsoft\\workspace\\smyths\\SearchTest.log");

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
		//log.info("@Before resetData()");
	}
	
	//Testing Search function and verifying that results are containing words used in search
	//Test 1a: search by pressing search button
	
	@Test
	public void searchTest1a() {	
		expected = "Star Wars";
		String xpath_item = ".//*[@id='main-content']/div/div[2]/ul/li[1]/div[2]/h2/a";
		
		doSearch();
		String starWarsItemText = driver.findElement(By.xpath(xpath_item)).getText();
		
		log.info("Expected: " + expected);
		log.info("Actual: " + starWarsItemText);

		assertTrue("searchTest1a Fail", starWarsItemText.contains(expected));
	}

	/*Testing Filter function after using search function and verifying that 
	the number of items are the same as the total number of products.
	Test 3a: using one of the filters available
	*/ 
	
	@Test
	public void filterSearchTest3a() {
					
		expected = "";
		String webid_checkbox = "2939";
		String xpath_clearall_button = ".//*[@id='filter-content']/div/div[3]/button";
		String xpath_totalnritems = ".//*[@id='filter-content']/div/div[2]/p/strong";
		String xpath_filterednritems = ".//*[@id='filter-content']/dl/dd[2]/ul/li[3]/a/span";
	
		//perform search
		doSearch();
	
		//continue with unique steps for this test	
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(webid_checkbox)));
		driver.findElement(By.name(webid_checkbox)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_clearall_button)));
		String totalNrOfItems=driver.findElement(By.xpath(xpath_totalnritems)).getText();
		String filteredNrOfItems=driver.findElement(By.xpath(xpath_filterednritems)).getText();
		
		log.info("Total nr of items: " + totalNrOfItems);
		log.info("Filtered nr of items: " + filteredNrOfItems);
		assertTrue(totalNrOfItems.contains(filteredNrOfItems));

			
	}
	
	
	
	@After
	public void tearDown() {
		//log.info("@After tearDown()");
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
		String input = "Star Wars";
		String webid_searchField = "q";
		String webid_searchButton = "main-search-button";
		String xpath_item = ".//*[@id='main-content']/div/div[2]/ul/li[1]/div[2]/h2/a";
		
		WebElement searchField = driver.findElement(By.id(webid_searchField));
				
		searchField.sendKeys(input);
		driver.findElement(By.id(webid_searchButton)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_item)));

	}
}

