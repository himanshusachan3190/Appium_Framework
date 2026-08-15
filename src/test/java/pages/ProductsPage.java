package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * ProductsPage.java
 * Represents the Products list screen of My Demo App.
 *
 * RULE: This class only knows about elements and actions on the products
 * screen.
 * Assertions stay in the test class.
 *
 * TODO: Use Appium Inspector on the products screen to verify
 * the resource-ids below before running.
 */
public class ProductsPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────
    private By productTitle = AppiumBy
            .androidUIAutomator("new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/productTV\")");
    private By productName = AppiumBy.androidUIAutomator("new UiSelector().text(\"Sauce Labs Backpack\")");
    private By productPrice = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/priceTV\").instance(0)");
    private By cartIcon = AppiumBy
            .androidUIAutomator("new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/cartIV\")");

    // ── Constructor ───────────────────────────────────────────
    public ProductsPage(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ── State checks ──────────────────────────────────────────

    public boolean isProductsScreenLoaded() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(productTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Actions ───────────────────────────────────────────────

    public List<WebElement> getAllProductNames() {
        return driver.findElements(productName);
    }

    public int getProductCount() {
        return getAllProductNames().size();
    }

    public String getFirstProductName() {
        List<WebElement> products = getAllProductNames();
        if (products.size() > 0) {
            return products.get(0).getText();
        }
        return "";
    }

    public void tapFirstProduct() {
        List<WebElement> products = getAllProductNames();
        if (products.size() > 0) {
            products.get(0).click();
        }
    }

    public void tapCartIcon() {
        driver.findElement(cartIcon).click();
    }
}
