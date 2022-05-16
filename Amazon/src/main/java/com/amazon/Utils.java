package com.amazon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Utils {
  static WebDriver driver;
  static WebDriverWait wait;
  final static String path = System.getProperty("user.dir");
  static ArrayList<String> arr;
  static ArrayList<String> bks;
  
	public static void openAmazon() {
		        WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				driver.get("https://www.amazon.com/");
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				wait= new WebDriverWait(driver, 350);
				click(getProp().getProperty("dismissPopup"));
				try{
					click(getProp().getProperty("logo"));
					}
				catch(Exception e) {
					click(getProp().getProperty("alternateHomepage"));
				}
	}
	
	public static void click(String locator) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath((locator)))).click();
	}
	
	public static void inputValue(String locator, String value) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((locator)))).sendKeys(value);
	}
	
    public static void selectProductCategory(String productCategory) throws InterruptedException {
		Select category = new Select(driver.findElement(By.id("searchDropdownBox")));
		category.selectByVisibleText(productCategory);
	}
    
    public static void applyFilter(String filter) throws InterruptedException {
		click(getProp().getProperty("englishFilter"));
		System.out.println("Filter Applied");
	}
    
    public static void search(String searchText) throws InterruptedException {
    	inputValue(getProp().getProperty("searchBox"), searchText);
		click(getProp().getProperty("searchButton"));
		}
    
	public static void verifySearchedResultsCount(String searchText) throws InterruptedException {
		ArrayList<String> bookNames = new ArrayList<>();
		Actions action = new Actions(driver);
		try {
		while(driver.findElement(By.xpath(getProp().getProperty("nextButton"))).isDisplayed()) {
			List<WebElement> booksListEle = driver.findElements(By.xpath(getProp().getProperty("books")));
			for(WebElement bookName : booksListEle) {
			bookNames.add(bookName.getText());
			//System.out.println(bookName.getText());
			}
			action.moveToElement(driver.findElement(By.xpath(getProp().getProperty("navButtons"))));
			click(getProp().getProperty("nextButton"));
		}
		}
		catch(Exception e) {
			List<WebElement> booksListEle = driver.findElements(By.xpath(getProp().getProperty("books")));
			for(WebElement bookName : booksListEle) {
			bookNames.add(bookName.getText());
			//System.out.println(bookName.getText());
			}
		}
		arr = bookNames;
		int resultCount = getResultCount();
		System.out.println(resultCount);
		assertEquals(resultCount, bookNames.size());
	}
	
	public static void checkResultContainSearchedText(String searchedText) throws InterruptedException {
		ArrayList<String> booksContainingText = new ArrayList<>(); 
		for(String bookname : arr) {
			 if (bookname.toLowerCase().contains(searchedText.toLowerCase())) {
				 booksContainingText.add(bookname);				 
				 assertTrue(bookname.toLowerCase().contains(searchedText.toLowerCase()));
			 }
		 }
		bks = booksContainingText;
	}
	
	public static void checkLargestBookNameWhichContainSearchedTextAndCharactersLessThan70(String searchedText) throws InterruptedException {
		HashMap<String, Integer> map = new HashMap<>();
		String largestName ="";
		System.out.println("***Books containing searched text in the results are below***");
		for (String st : bks) {
			System.out.println(st + ", length is " +st.length());
			map.put(st, st.length());
		}
		ArrayList<Integer> val= new ArrayList<>();
		for(Entry<String, Integer> entry: map.entrySet()) {
			val.add(entry.getValue());
		}
		int maxCharStr= Collections.max(val);
		for(Entry<String, Integer> entrym: map.entrySet()) {
			if(entrym.getValue()==maxCharStr) {
				largestName = entrym.getKey();
				break;
			}
		}
		System.out.println("***Largest Name in the List which contain searched text is*** "+largestName+" -length is "+largestName.length());
		assertTrue(largestName.length() == Collections.max(val));
		assertTrue(largestName.length()<70);		
	}
	
	public static Integer getResultCount() throws InterruptedException {
		String resultCountStr = driver.findElement(By.xpath(getProp().getProperty("resultsCount"))).getText();
		System.out.println(resultCountStr);
		String[] str = resultCountStr.split("of ");
		String[] str1 = str[1].split(" results");
		int count = Integer.parseInt(str1[0]);  
		return count;
	}
		
	public static Properties getProp() {
	    File file = new File(path+"\\src\\Resources\\data.properties");	  
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();
		
		//load properties file
		try {
			prop.load(fileInput);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	return prop;
	}
	
	public static void closeBrowser() {
		driver.quit();
	}

}
