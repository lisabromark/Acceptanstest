package caliva.com;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
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

	// Testing Search function and verifying that results are containing words
	// used in search
	// Test 1a: search by pressing search button

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

	/*
	 * Testing Filter function after using search function and verifying that
	 * the number of items are the same as the total number of products. Test
	 * 3a: using one of the filters available
	 */


	@Test
	public void filterSearchTest3a() {

		String name_checkbox = "2939";
		String xpath_clearall_button = ".//*[@id='filter-content']/div/div[3]/button";
		String xpath_totalnritems = ".//*[@id='filter-content']/div/div[2]/p/strong";
		String xpath_filterednritems = ".//*[@id='filter-content']/dl/dd[2]/ul/li[3]/a/span";

		// perform search
		doSearch();

		// continue with unique steps for this test
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name_checkbox)));
		driver.findElement(By.name(name_checkbox)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_clearall_button)));
		String totalNrOfItems = driver.findElement(By.xpath(xpath_totalnritems)).getText();
		String filteredNrOfItems = driver.findElement(By.xpath(xpath_filterednritems)).getText();

		log.info("Total nr of items: " + totalNrOfItems);
		log.info("Filtered nr of items: " + filteredNrOfItems);
		assertTrue(totalNrOfItems.contains(filteredNrOfItems));

	}

	@Test
	public void sortSearchTest4a() {

		expected = "";
		String xpath_image = ".//*[@id='147498']";
		String webid_sort_name = "cuit_sort_Name";
		
		// perform search
		doSearch();

		// continue with unique steps for this test
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_image)));
		driver.findElement(By.id(webid_sort_name)).click();

		// using LinkedList as we do not know how many items will be in the
		// list, it is dynamic (list will have unpredictable insertions and deletions)
		List<WebElement> products_Webelement = new LinkedList<WebElement>();

		// using the xpath with class "product-name" as this is in common for
		// all the elements in the list
		products_Webelement = driver.findElements(By.xpath("//h2[@class='product-name']/a"));

		// creating a new list for the product names as we want to compare the
		// title for each element
		LinkedList<String> product_names = new LinkedList<String>();

		// loop through all the elements of the products_Webelement list to get the
		// title and store it into the product_names list
		for (int i = 0; i < products_Webelement.size(); i++) {

			String name = products_Webelement.get(i).getAttribute("title");
			product_names.add(name);

		}
		
		// send the list to method doAlphabeticalOrder
		boolean result = doAlphabeticalOrder(product_names);
		log.info("Product names sorted by alphabetical order: " + result);
		assertTrue(result == true);

		
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

	public void doSearch() {
		String input = "Star Wars";
		String webid_searchField = "q";
		String webid_searchButton = "main-search-button";
		String xpath_item = ".//*[@id='main-content']/div/div[2]/ul/li[1]/div[2]/h2/a";

		WebElement searchField = driver.findElement(By.id(webid_searchField));

		searchField.sendKeys(input);
		driver.findElement(By.id(webid_searchButton)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_item)));

	}

	public static boolean doAlphabeticalOrder(LinkedList<String> product_names) {
		String previous = "";

		// using compareTo method which compares two strings lexicographically,
		// the comparison is based on the Unicode value of each character in the
		// strings

		for (final String current : product_names) {
			if (current.compareTo(previous) < 0)
				return false;
			previous = current;
		}

		return true;

	}

}
