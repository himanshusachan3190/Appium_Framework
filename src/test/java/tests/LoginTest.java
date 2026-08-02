package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import base.BaseTest;


public class LoginTest extends BaseTest {

	private static final String USERNAME = "com.saucelabs.mydemoapp.android:id/nameET";
	private static final String PASSWORD = "com.saucelabs.mydemoapp.android:id/passwordET";
	private static final String LOGIN_BTN = "com.saucelabs.mydemoapp.android:id/loginBtn";
	private static final String PRODUCT_TV = "com.saucelabs.mydemoapp.android:id/productTV";
	
	
	@Test(priority = 1)
	public void testValidLogin() {
		WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(20));

		WebElement products = localWait.until(
				ExpectedConditions.visibilityOfElementLocated(By.id(PRODUCT_TV)));

		Assert.assertTrue(products.isDisplayed(),
				"Products screen should be visible after the app launches");
	}
}
