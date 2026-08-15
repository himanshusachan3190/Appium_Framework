package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * ExtentReportManager.java — Manages the Extent Reports lifecycle.
 *
 * Creates a single ExtentReports instance (Singleton) shared across all tests.
 * The report is written to: reports/ExtentReport.html
 */
public class ExtentReportManager {

    private static ExtentReports extent;

    /** Get or create the singleton ExtentReports instance */
    public static ExtentReports getInstance() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }

    /** Create and configure the ExtentReports instance */
    private static ExtentReports createInstance() {
        String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // ── Report appearance ────────────────────────────────
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Appium Framework - Test Report");
        sparkReporter.config().setReportName("Appium Mobile Automation Report");
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy hh:mm:ss a");

        // ── Create ExtentReports and attach reporter ─────────
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // ── System / Environment info shown in the report ────
        extent.setSystemInfo("Framework", "Appium + TestNG");
        extent.setSystemInfo("Platform", "Android");
        extent.setSystemInfo("App", "Sauce Labs My Demo App");
        extent.setSystemInfo("Automation Driver", "UiAutomator2");
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));

        return extent;
    }
}
