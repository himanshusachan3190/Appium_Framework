package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Actions;

/**
 * LoginPage.java
 * Represents the Login screen of My Demo App.
 *
 * RULE: This class only knows about elements and actions on the login screen.
 * It does NOT contain any assertions — that is LoginTest's job.
 */
public class LoginPage {

    private AndroidDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    // ── Locators ──────────────────────────────────────────────
    // Verify these with Appium Inspector on the login screen

    private By MenuBar = By.id("com.saucelabs.mydemoapp.android:id/menuIV");
    private By MenuBar_LoginLink = By.xpath("//android.widget.TextView[@content-desc=\"Login Menu Item\"]");
    private By usernameField = By.id("com.saucelabs.mydemoapp.android:id/nameET");
    private By passwordField = By.id("com.saucelabs.mydemoapp.android:id/passwordET");
    private By loginButton = By.id("com.saucelabs.mydemoapp.android:id/loginBtn");
    private By errorMessage = By.id("com.saucelabs.mydemoapp.android:id/errorTV");

    // ── Constructor ───────────────────────────────────────────
    public LoginPage(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.actions = new Actions(driver, wait);
    }

    // ── Actions ───────────────────────────────────────────────

    /** Navigate to the Login screen via the hamburger menu */
    public void navigateToLogin() {
        actions.tap(MenuBar);
        actions.tap(MenuBar_LoginLink);
    }

    public void enterUsername(String username) {
        actions.type(usernameField, username);

    }

    public void enterPassword(String password) {
        actions.type(passwordField, password);
    }

    public void tapLogin() {
        actions.tap(loginButton);
    }

    // ── Reusable full login action ────────────────────────────
    public void login(String username, String password) {
        navigateToLogin();
        enterUsername(username);
        enterPassword(password);
        tapLogin();
    }

    // ── State checks — used by test for assertions ────────────
    public boolean isErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorText() {
        return driver.findElement(errorMessage).getText();
    }
}
