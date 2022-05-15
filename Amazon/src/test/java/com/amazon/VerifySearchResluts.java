package com.amazon;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.amazon.Utils;

public class VerifySearchResluts extends Utils{
    String searchedBook = "the Lost World by Arthur Conan Doyle";
	
	@Before	
	public void navigateToApp() throws InterruptedException {
	  openAmazon();	
	}
	
	@Test
	public void validateSearchedResults() throws InterruptedException {					
		selectProductCategory("Books");
		search(searchedBook);
		verifySearchedResultsCount(searchedBook);
		//applyFilter("englishFilter");
	}
	
	@After	
	public void exitTest() {
		//closeBrowser();
	}
	
}
