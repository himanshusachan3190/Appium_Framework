package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import base.BaseTest;
import io.appium.java_client.AppiumBy;


public class LoginTest extends BaseTest {
    
	private static final String menu = "com.saucelabs.mydemoapp.android:id/menuIV";
	private static final String USERNAME = "com.saucelabs.mydemoapp.android:id/nameET";
	private static final String PASSWORD = "com.saucelabs.mydemoapp.android:id/passwordET";
	private static final String LOGIN_BTN = "com.saucelabs.mydemoapp.android:id/loginBtn";
	private static final String PRODUCT_TV = "com.saucelabs.mydemoapp.android:id/productTV";
	
	
	@Test(priority = 1)
	public void testValidLogin() {
		WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
		driver.findElement(By.id(menu)).click();
		driver.findElement(AppiumBy.androidUIAutomator(
			    "new UiSelector().text(\"Log In\")"
			)).click();
		
		WebElement user = wait.until(
			      ExpectedConditions.visibilityOfElementLocated(By.id(USERNAME)));
			    user.clear();
			    user.sendKeys("bod@example.com");
			    
			    driver.findElement(By.id(PASSWORD)).sendKeys("10203040");
			    driver.findElement(By.id(LOGIN_BTN)).click();
			    
		WebElement products = localWait.until(
				ExpectedConditions.visibilityOfElementLocated(By.id(PRODUCT_TV)));
		System.out.println("login successfully");
		Assert.assertTrue(products.isDisplayed(),
				"Products screen should be visible after the app launches");
	}
}
