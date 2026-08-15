package utils;

import base.BaseTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * ExtentTestListener.java — TestNG Listener for Extent Reports
 *
 * Automatically logs every test's result (pass / fail / skip) into the
 * Extent Report. On failure, a screenshot is captured and embedded in
 * the report for easy debugging.
 *
 * Registered in testng.xml as a listener — no code changes needed in tests.
 */
public class ExtentTestListener implements ITestListener {

    private static ExtentReports extent = ExtentReportManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // ── Called once before any test in the suite ──────────────
    @Override
    public void onStart(ITestContext context) {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  Extent Report: Suite started — " + context.getName());
        System.out.println("═══════════════════════════════════════════");
    }

    // ── Called before each @Test method ───────────────────────
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        // Create a new entry in the report for this test
        ExtentTest extentTest = extent.createTest(testName,
                description != null ? description : "");

        // Tag with class name for filtering
        extentTest.assignCategory(result.getTestClass().getName());

        test.set(extentTest);
        System.out.println("▶ Starting: " + testName);
    }

    // ── Called when a @Test passes ────────────────────────────
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        test.get().log(Status.PASS, "✅ Test PASSED: " + testName);
        System.out.println("✓ Passed: " + testName);
    }

    // ── Called when a @Test fails ─────────────────────────────
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        // Log the failure with exception details
        test.get().log(Status.FAIL, "❌ Test FAILED: " + testName);
        test.get().fail(result.getThrowable());

        // Capture and attach screenshot on failure
        try {
            AndroidDriver driver = BaseTest.getDriver();
            if (driver != null) {
                String base64Screenshot = driver.getScreenshotAs(OutputType.BASE64);
                test.get().fail("Screenshot at failure:",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                System.out.println("📸 Screenshot captured for: " + testName);
            }
        } catch (Exception e) {
            test.get().warning("⚠ Could not capture screenshot: " + e.getMessage());
        }

        System.out.println("✗ Failed: " + testName);
    }

    // ── Called when a @Test is skipped ────────────────────────
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        test.get().log(Status.SKIP, "⏭ Test SKIPPED: " + testName);

        if (result.getThrowable() != null) {
            test.get().skip(result.getThrowable());
        }

        System.out.println("⏭ Skipped: " + testName);
    }

    // ── Called once after all tests in the suite ──────────────
    @Override
    public void onFinish(ITestContext context) {
        // Flush writes the report to disk
        extent.flush();
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  Extent Report: Suite finished — " + context.getName());
        System.out.println("  Report saved to: reports/ExtentReport.html");
        System.out.println("═══════════════════════════════════════════");
    }

    /** Get the current thread's ExtentTest — use this in tests to add custom logs */
    public static ExtentTest getTest() {
        return test.get();
    }
}
