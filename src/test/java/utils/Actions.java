package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Actions.java — Reusable Appium Mobile Actions Utility
 *
 * A comprehensive helper class that wraps common Appium mobile interactions
 * using the W3C Actions API. Designed to be shared across all Page Objects
 * and Test classes in the framework.
 *
 * Categories:
 *   1. Tap / Click actions
 *   2. Text input actions
 *   3. Wait utilities
 *   4. Element state checks
 *   5. Swipe actions
 *   6. Scroll actions
 *   7. Gesture actions (long press, double tap, drag & drop, pinch, zoom)
 *   8. Keyboard utilities
 *   9. App lifecycle management
 *  10. Screenshot utilities
 *
 * Usage:
 *   Actions actions = new Actions(driver, wait);
 *   actions.tap(myLocator);
 *   actions.swipeUp();
 */
public class Actions {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // ── Default durations ────────────────────────────────────
    private static final Duration DEFAULT_WAIT    = Duration.ofSeconds(15);
    private static final Duration SWIPE_DURATION  = Duration.ofMillis(800);
    private static final Duration LONG_PRESS_HOLD = Duration.ofSeconds(2);

    // ══════════════════════════════════════════════════════════
    //  Constructor
    // ══════════════════════════════════════════════════════════

    public Actions(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    // ══════════════════════════════════════════════════════════
    //  1. TAP / CLICK ACTIONS
    // ══════════════════════════════════════════════════════════

    /** Wait for element to be clickable, then tap it */
    public void tap(By locator) {
        waitForClickable(locator).click();
    }

    /** Tap an already-found WebElement */
    public void tap(WebElement element) {
        element.click();
    }

    /** Tap at exact screen coordinates */
    public void tapByCoordinates(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tapSeq = new Sequence(finger, 1);
        tapSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tapSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tapSeq.addAction(new Pause(finger, Duration.ofMillis(100)));
        tapSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(tapSeq));
    }

    /** Double-tap on an element */
    public void doubleTap(By locator) {
        WebElement element = waitForVisible(locator);
        Point center = getCenter(element);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence doubleTapSeq = new Sequence(finger, 1);

        // First tap
        doubleTapSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
        doubleTapSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTapSeq.addAction(new Pause(finger, Duration.ofMillis(50)));
        doubleTapSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Short pause between taps
        doubleTapSeq.addAction(new Pause(finger, Duration.ofMillis(100)));

        // Second tap
        doubleTapSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTapSeq.addAction(new Pause(finger, Duration.ofMillis(50)));
        doubleTapSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(doubleTapSeq));
    }

    /** Long-press on an element (default 2 seconds) */
    public void longPress(By locator) {
        longPress(locator, LONG_PRESS_HOLD);
    }

    /** Long-press on an element for a custom duration */
    public void longPress(By locator, Duration holdDuration) {
        WebElement element = waitForVisible(locator);
        Point center = getCenter(element);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPressSeq = new Sequence(finger, 1);
        longPressSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
        longPressSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPressSeq.addAction(new Pause(finger, holdDuration));
        longPressSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(longPressSeq));
    }

    // ══════════════════════════════════════════════════════════
    //  2. TEXT INPUT ACTIONS
    // ══════════════════════════════════════════════════════════

    /** Clear field and type text */
    public void type(By locator, String text) {
        WebElement field = waitForVisible(locator);
        field.clear();
        field.sendKeys(text);
    }

    /** Type text without clearing first */
    public void appendText(By locator, String text) {
        waitForVisible(locator).sendKeys(text);
    }

    /** Clear a text field */
    public void clearField(By locator) {
        waitForVisible(locator).clear();
    }

    /** Get text from an element */
    public String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    /** Get text from an already-found WebElement */
    public String getText(WebElement element) {
        return element.getText();
    }

    // ══════════════════════════════════════════════════════════
    //  3. WAIT UTILITIES
    // ══════════════════════════════════════════════════════════

    /** Wait until element is visible and return it */
    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is visible with custom timeout */
    public WebElement waitForVisible(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is clickable and return it */
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Wait until element is clickable with custom timeout */
    public WebElement waitForClickable(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Wait until element is present in DOM (may not be visible) */
    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Wait until element disappears from screen */
    public boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Wait until element disappears with custom timeout */
    public boolean waitForInvisible(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Wait until element contains specific text */
    public boolean waitForTextPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /** Static sleep — use sparingly, prefer explicit waits */
    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ══════════════════════════════════════════════════════════
    //  4. ELEMENT STATE CHECKS
    // ══════════════════════════════════════════════════════════

    /** Check if element is displayed (no wait, no exception) */
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Check if element is displayed with explicit wait */
    public boolean isDisplayedWithWait(By locator) {
        try {
            waitForVisible(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Check if element is displayed with custom timeout */
    public boolean isDisplayedWithWait(By locator, int timeoutSeconds) {
        try {
            waitForVisible(locator, timeoutSeconds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Check if element is enabled */
    public boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /** Check if element is selected (checkbox, radio) */
    public boolean isSelected(By locator) {
        try {
            return driver.findElement(locator).isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /** Get an attribute value from an element */
    public String getAttribute(By locator, String attribute) {
        return waitForPresence(locator).getAttribute(attribute);
    }

    /** Count the number of elements matching a locator */
    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    /** Get all elements matching a locator */
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    // ══════════════════════════════════════════════════════════
    //  5. SWIPE ACTIONS
    // ══════════════════════════════════════════════════════════

    /** Swipe up (scroll content down) — 60% of screen height */
    public void swipeUp() {
        swipe(Direction.UP, 0.6);
    }

    /** Swipe down (scroll content up) — 60% of screen height */
    public void swipeDown() {
        swipe(Direction.DOWN, 0.6);
    }

    /** Swipe left — 60% of screen width */
    public void swipeLeft() {
        swipe(Direction.LEFT, 0.6);
    }

    /** Swipe right — 60% of screen width */
    public void swipeRight() {
        swipe(Direction.RIGHT, 0.6);
    }

    /** Swipe in a direction with a custom swipe percentage (0.0 to 1.0) */
    public void swipe(Direction direction, double swipePercent) {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.width / 2;
        int centerY = size.height / 2;
        int startX, startY, endX, endY;

        switch (direction) {
            case UP:
                startX = centerX;
                startY = (int) (size.height * (0.5 + swipePercent / 2));
                endX   = centerX;
                endY   = (int) (size.height * (0.5 - swipePercent / 2));
                break;
            case DOWN:
                startX = centerX;
                startY = (int) (size.height * (0.5 - swipePercent / 2));
                endX   = centerX;
                endY   = (int) (size.height * (0.5 + swipePercent / 2));
                break;
            case LEFT:
                startX = (int) (size.width * (0.5 + swipePercent / 2));
                startY = centerY;
                endX   = (int) (size.width * (0.5 - swipePercent / 2));
                endY   = centerY;
                break;
            case RIGHT:
                startX = (int) (size.width * (0.5 - swipePercent / 2));
                startY = centerY;
                endX   = (int) (size.width * (0.5 + swipePercent / 2));
                endY   = centerY;
                break;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }

        performSwipe(startX, startY, endX, endY, SWIPE_DURATION);
    }

    /** Swipe from one point to another with custom duration */
    public void swipeFromTo(int startX, int startY, int endX, int endY) {
        performSwipe(startX, startY, endX, endY, SWIPE_DURATION);
    }

    /** Swipe multiple times in a direction */
    public void swipeMultiple(Direction direction, int times) {
        for (int i = 0; i < times; i++) {
            swipe(direction, 0.6);
            pause(500);
        }
    }

    /** Core swipe implementation using W3C PointerInput */
    private void performSwipe(int startX, int startY, int endX, int endY, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipeSeq = new Sequence(finger, 1);
        swipeSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipeSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipeSeq.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), endX, endY));
        swipeSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipeSeq));
    }

    /** Direction enum for swipe/scroll operations */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    // ══════════════════════════════════════════════════════════
    //  6. SCROLL ACTIONS
    // ══════════════════════════════════════════════════════════

    /** Scroll to an element using UiScrollable (Android) */
    public WebElement scrollToText(String visibleText) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
              + ".scrollIntoView(new UiSelector().textContains(\"" + visibleText + "\"))"));
    }

    /** Scroll to an element by resource-id using UiScrollable */
    public WebElement scrollToResourceId(String resourceId) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
              + ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"))"));
    }

    /** Scroll to an element by content-desc using UiScrollable */
    public WebElement scrollToContentDesc(String contentDesc) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
              + ".scrollIntoView(new UiSelector().description(\"" + contentDesc + "\"))"));
    }

    /** Scroll down until an element is found or max attempts reached */
    public boolean scrollDownToElement(By locator, int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            if (isDisplayed(locator)) {
                return true;
            }
            swipeUp();
            pause(500);
        }
        return isDisplayed(locator);
    }

    /** Scroll up until an element is found or max attempts reached */
    public boolean scrollUpToElement(By locator, int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            if (isDisplayed(locator)) {
                return true;
            }
            swipeDown();
            pause(500);
        }
        return isDisplayed(locator);
    }

    /** Scroll to the top of the page */
    public void scrollToTop(int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            swipeDown();
            pause(300);
        }
    }

    /** Scroll to the bottom of the page */
    public void scrollToBottom(int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            swipeUp();
            pause(300);
        }
    }

    // ══════════════════════════════════════════════════════════
    //  7. GESTURE ACTIONS
    // ══════════════════════════════════════════════════════════

    /** Drag an element and drop it onto another element */
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = waitForVisible(sourceLocator);
        WebElement target = waitForVisible(targetLocator);
        Point sourceCenter = getCenter(source);
        Point targetCenter = getCenter(target);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence dragDrop = new Sequence(finger, 1);
        dragDrop.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), sourceCenter.x, sourceCenter.y));
        dragDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        dragDrop.addAction(new Pause(finger, Duration.ofMillis(500)));
        dragDrop.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), targetCenter.x, targetCenter.y));
        dragDrop.addAction(new Pause(finger, Duration.ofMillis(300)));
        dragDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(dragDrop));
    }

    /** Drag from coordinates to coordinates */
    public void dragAndDrop(int fromX, int fromY, int toX, int toY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence dragDrop = new Sequence(finger, 1);
        dragDrop.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), fromX, fromY));
        dragDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        dragDrop.addAction(new Pause(finger, Duration.ofMillis(500)));
        dragDrop.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), toX, toY));
        dragDrop.addAction(new Pause(finger, Duration.ofMillis(300)));
        dragDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(dragDrop));
    }

    /** Pinch (zoom out) gesture on an element */
    public void pinch(By locator) {
        WebElement element = waitForVisible(locator);
        Point center = getCenter(element);
        int offset = 100;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        // Finger 1: from above center → to center
        Sequence pinch1 = new Sequence(finger1, 0);
        pinch1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y - offset));
        pinch1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        pinch1.addAction(finger1.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), center.x, center.y));
        pinch1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Finger 2: from below center → to center
        Sequence pinch2 = new Sequence(finger2, 0);
        pinch2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y + offset));
        pinch2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        pinch2.addAction(finger2.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), center.x, center.y));
        pinch2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(pinch1, pinch2));
    }

    /** Zoom (zoom in) gesture on an element */
    public void zoom(By locator) {
        WebElement element = waitForVisible(locator);
        Point center = getCenter(element);
        int offset = 100;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        // Finger 1: from center → move up
        Sequence zoom1 = new Sequence(finger1, 0);
        zoom1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
        zoom1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        zoom1.addAction(finger1.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), center.x, center.y - offset));
        zoom1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Finger 2: from center → move down
        Sequence zoom2 = new Sequence(finger2, 0);
        zoom2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
        zoom2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        zoom2.addAction(finger2.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), center.x, center.y + offset));
        zoom2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(zoom1, zoom2));
    }

    // ══════════════════════════════════════════════════════════
    //  8. KEYBOARD UTILITIES
    // ══════════════════════════════════════════════════════════

    /** Hide the on-screen keyboard if visible */
    public void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (Exception e) {
            // Keyboard was not visible — safe to ignore
        }
    }

    /** Check if the keyboard is currently shown */
    public boolean isKeyboardShown() {
        try {
            return driver.isKeyboardShown();
        } catch (Exception e) {
            return false;
        }
    }

    /** Press the Android back button */
    public void pressBack() {
        driver.navigate().back();
    }

    // ══════════════════════════════════════════════════════════
    //  9. APP LIFECYCLE MANAGEMENT
    // ══════════════════════════════════════════════════════════

    /** Close the app (keep session alive) */
    public void closeApp(String appPackage) {
        driver.terminateApp(appPackage);
    }

    /** Relaunch the app */
    public void launchApp(String appPackage) {
        driver.activateApp(appPackage);
    }

    /** Reset app by terminating and relaunching */
    public void resetApp(String appPackage) {
        driver.terminateApp(appPackage);
        driver.activateApp(appPackage);
    }

    /** Send app to background for a given number of seconds, then bring back */
    public void runAppInBackground(int seconds) {
        driver.runAppInBackground(Duration.ofSeconds(seconds));
    }

    /** Check if app is installed */
    public boolean isAppInstalled(String appPackage) {
        return driver.isAppInstalled(appPackage);
    }

    /** Get current activity name */
    public String getCurrentActivity() {
        return driver.currentActivity();
    }

    // ══════════════════════════════════════════════════════════
    //  10. SCREENSHOT UTILITIES
    // ══════════════════════════════════════════════════════════

    /** Take a screenshot and save to the specified path */
    public String takeScreenshot(String directory, String fileName) {
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        Path destDir  = Paths.get(directory);
        Path destFile = destDir.resolve(fileName + ".png");
        try {
            Files.createDirectories(destDir);
            Files.copy(screenshot.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + destFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
        return destFile.toAbsolutePath().toString();
    }

    /** Take a screenshot of a specific element */
    public String takeElementScreenshot(By locator, String directory, String fileName) {
        WebElement element = waitForVisible(locator);
        File screenshot = element.getScreenshotAs(OutputType.FILE);
        Path destDir  = Paths.get(directory);
        Path destFile = destDir.resolve(fileName + ".png");
        try {
            Files.createDirectories(destDir);
            Files.copy(screenshot.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Element screenshot saved: " + destFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save element screenshot: " + e.getMessage());
        }
        return destFile.toAbsolutePath().toString();
    }

    // ══════════════════════════════════════════════════════════
    //  INTERNAL HELPERS
    // ══════════════════════════════════════════════════════════

    /** Get the centre point of an element */
    private Point getCenter(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        return new Point(
                location.getX() + size.getWidth()  / 2,
                location.getY() + size.getHeight() / 2);
    }
}
