package base;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.URL;
import java.time.Duration;


public class BaseTest {

	protected static AndroidDriver driver;
	
	protected static WebDriverWait wait;
	
	@BeforeClass
	  public static void setup() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", "Redmi Note 9 Pro Max");
        caps.setCapability("platformName", "Android");
        caps.setCapability("appPackage", "com.saucelabs.mydemoapp.android");
        caps.setCapability("appActivity", "com.saucelabs.mydemoapp.android.view.activities.SplashActivity");
        caps.setCapability("automationName", "UiAutomator2");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    	wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    	System.out.println("App launched"+ driver.currentActivity());
    }
	
	@AfterClass
	public void tearDown() {
		if (driver != null) {
		driver.quit();
		System.out.println("✓ Driver closed.");
		}
		}
}
