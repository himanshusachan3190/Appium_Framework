package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * LoginTest.java — updated for Day 10
 *
 * Changes from Day 9:
 * - No longer finds elements directly
 * - Uses LoginPage and ProductsPage objects instead
 * - Test methods are now clean — just actions and assertions
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    // Valid credentials
    private static final String VALID_USER = "bod@example.com";
    private static final String VALID_PASS = "10203040";
    private static final String WRONG_PASS = "wrongpass";

    // ── Runs before each @Test — resets app and page objects ──
    @BeforeMethod
    public void initPages() {
        // Reset app to a fresh state so each test starts from the home screen
        String appPackage = "com.saucelabs.mydemoapp.android";
        driver.terminateApp(appPackage);
        driver.activateApp(appPackage);

        loginPage = new LoginPage(driver, wait);
        productsPage = new ProductsPage(driver, wait);
    }

    // ── Test 1: Valid login ───────────────────────────────────
    @Test(priority = 1)
    public void testValidLogin() {
        loginPage.login(VALID_USER, VALID_PASS);

        Assert.assertTrue(
                productsPage.isProductsScreenLoaded(),
                "Products screen should load after valid login");

        System.out.println("✓ Products screen loaded.");
        System.out.println("✓ Product count: " + productsPage.getProductCount());
    }

    // ── Test 2: Invalid login ─────────────────────────────────
    @Test(priority = 2)
    public void testInvalidLogin() {
        loginPage.login(VALID_USER, WRONG_PASS);

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should appear for wrong password");

        System.out.println("✓ Error shown: " + loginPage.getErrorText());
    }

    // ── Test 3: Products list not empty ───────────────────────
    @Test(priority = 3)
    public void testProductsListNotEmpty() {
        loginPage.login(VALID_USER, VALID_PASS);

        int count = productsPage.getProductCount();

        Assert.assertTrue(count > 0,
                "Products list should not be empty after login");

        System.out.println("✓ Products found: " + count);
        System.out.println("✓ First product: " + productsPage.getFirstProductName());
    }
}
