# 📱 Appium Mobile Automation Framework

<div align="center">

![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Appium](https://img.shields.io/badge/Appium-8.6.0-662d91?style=for-the-badge&logo=appium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.8.0-DC382D?style=for-the-badge&logo=testng&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Android](https://img.shields.io/badge/Android-UiAutomator2-3DDC84?style=for-the-badge&logo=android&logoColor=white)

**A robust, scalable Appium test automation framework built with the Page Object Model (POM) design pattern for Android mobile application testing.**

[Getting Started](#-getting-started) •
[Project Structure](#-project-structure) •
[How to Run](#-how-to-run) •
[Actions Utility](#-actions-utility)

</div>

---

## 🏗️ Architecture

This framework follows the **Page Object Model (POM)** design pattern, separating test logic from UI interaction logic for better maintainability and reusability.

```
┌─────────────────────────────────────────────────────────┐
│                     TestNG Suite                        │
│                    (testng.xml)                         │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   ┌─────────────┐    ┌──────────────┐                  │
│   │  Test Class  │───▶│  Page Object │                  │
│   │ (LoginTest)  │    │ (LoginPage)  │                  │
│   └──────┬───────┘    └──────┬───────┘                  │
│          │                   │                          │
│          ▼                   ▼                          │
│   ┌─────────────┐    ┌──────────────┐                  │
│   │  Base Test   │    │   Actions    │                  │
│   │ (BaseTest)   │    │  (Utility)   │                  │
│   └──────┬───────┘    └──────┬───────┘                  │
│          │                   │                          │
│          └───────┬───────────┘                          │
│                  ▼                                      │
│          ┌──────────────┐                               │
│          │ Appium Server│                               │
│          │  + Android   │                               │
│          │    Device     │                               │
│          └──────────────┘                               │
└─────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
AppiumFramework/
│
├── pom.xml                          # Maven config & dependencies
├── testng.xml                       # TestNG suite configuration
├── README.md                        # You are here!
│
├── src/
│   └── test/
│       └── java/
│           ├── base/
│           │   └── BaseTest.java        # Driver setup & teardown
│           │
│           ├── pages/
│           │   ├── LoginPage.java       # Login screen interactions
│           │   └── ProductsPage.java    # Products screen interactions
│           │
│           ├── tests/
│           │   └── LoginTest.java       # Login test scenarios
│           │
│           └── utils/
│               └── Actions.java         # Reusable mobile action helpers
│
└── target/                          # Build output (auto-generated)
```

| Layer | Package | Purpose |
|-------|---------|---------|
| **Base** | `base/` | Driver initialization, capabilities setup, session teardown |
| **Pages** | `pages/` | Page Object classes — element locators & screen actions |
| **Tests** | `tests/` | Test classes — test scenarios with assertions |
| **Utils** | `utils/` | Reusable utilities — common mobile gestures & helpers |

---

## ⚙️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 11 | Programming language |
| **Appium Java Client** | 8.6.0 | Mobile automation library |
| **TestNG** | 7.8.0 | Test execution & reporting |
| **Maven** | 3.x | Build & dependency management |
| **Maven Surefire** | 3.1.2 | Test runner plugin |
| **UiAutomator2** | Latest | Android automation driver |

---

## 🚀 Getting Started

### Prerequisites

Ensure the following are installed and configured:

| # | Requirement | Verify Command |
|---|-------------|----------------|
| 1 | **Java JDK 11+** | `java -version` |
| 2 | **Maven 3.x** | `mvn -version` |
| 3 | **Node.js & npm** | `node -v` |
| 4 | **Appium Server 2.x** | `appium -v` |
| 5 | **Android SDK** | `adb devices` |
| 6 | **UiAutomator2 Driver** | `appium driver list` |

### Environment Variables

```bash
JAVA_HOME    → Path to your JDK installation
ANDROID_HOME → Path to your Android SDK
PATH         → Include platform-tools & tools directories
```

### Target App

This framework tests the **[Sauce Labs My Demo App](https://github.com/saucelabs/my-demo-app-android)** for Android.

- **Package:** `com.saucelabs.mydemoapp.android`
- **Activity:** `com.saucelabs.mydemoapp.android.view.activities.SplashActivity`

> Install the APK on your device/emulator before running tests.

---

## 🏃 How to Run

### 1. Start Appium Server

```bash
appium
```

> Server starts at `http://127.0.0.1:4723` by default.

### 2. Connect Android Device

```bash
# Verify device is connected
adb devices
```

### 3. Run Tests

```bash
# Run all tests (uses testng.xml configured in pom.xml)
mvn test

# Run with specific suite file (PowerShell — use quotes around -D)
mvn test "-DsuiteXmlFile=testng.xml"

# Run with verbose output
mvn test -e
```

### 4. Run from Eclipse IDE

> Right-click `testng.xml` → **Run As** → **TestNG Suite**

---

## 🧪 Test Scenarios

### LoginTest

| # | Test Method | Description | Priority |
|---|-------------|-------------|----------|
| 1 | `testValidLogin()` | Login with valid credentials → verify Products screen loads | 1 |
| 2 | `testInvalidLogin()` | Login with wrong password → verify error message appears | 2 |
| 3 | `testProductsListNotEmpty()` | Login → verify product list is populated | 3 |

---

## 📄 Key Classes

### 🔧 BaseTest (`base/BaseTest.java`)

Handles Appium driver setup and teardown for all tests.

```java
// Capabilities configured:
- deviceName:       "Redmi Note 9 Pro Max"
- platformName:     "Android"
- automationName:   "UiAutomator2"
- appPackage:       "com.saucelabs.mydemoapp.android"
- appActivity:      "...SplashActivity"
- implicitWait:     10 seconds
- explicitWait:     15 seconds
```

### 📑 Page Objects (`pages/`)

| Class | Screen | Key Methods |
|-------|--------|-------------|
| **LoginPage** | Login Screen | `navigateToLogin()`, `enterUsername()`, `enterPassword()`, `tapLogin()`, `login()`, `isErrorDisplayed()`, `getErrorText()` |
| **ProductsPage** | Products Screen | `isProductsScreenLoaded()`, `getAllProductNames()`, `getProductCount()`, `getFirstProductName()`, `tapFirstProduct()`, `tapCartIcon()` |

### 🛠️ Actions Utility (`utils/Actions.java`)

A comprehensive utility class with **40+ reusable methods** for common mobile interactions:

| Category | Methods |
|----------|---------|
| **Tap / Click** | `tap()`, `tapByCoordinates()`, `doubleTap()`, `longPress()` |
| **Text Input** | `type()`, `appendText()`, `clearField()`, `getText()` |
| **Waits** | `waitForVisible()`, `waitForClickable()`, `waitForPresence()`, `waitForInvisible()`, `waitForTextPresent()` |
| **Element State** | `isDisplayed()`, `isDisplayedWithWait()`, `isEnabled()`, `isSelected()`, `getAttribute()`, `getElementCount()` |
| **Swipe** | `swipeUp()`, `swipeDown()`, `swipeLeft()`, `swipeRight()`, `swipeMultiple()` |
| **Scroll** | `scrollToText()`, `scrollToResourceId()`, `scrollToContentDesc()`, `scrollDownToElement()`, `scrollUpToElement()` |
| **Gestures** | `dragAndDrop()`, `pinch()`, `zoom()` |
| **Keyboard** | `hideKeyboard()`, `isKeyboardShown()`, `pressBack()` |
| **App Lifecycle** | `closeApp()`, `launchApp()`, `resetApp()`, `runAppInBackground()` |
| **Screenshot** | `takeScreenshot()`, `takeElementScreenshot()` |

**Usage Example:**
```java
Actions actions = new Actions(driver, wait);

actions.tap(loginButton);
actions.type(usernameField, "user@example.com");
actions.swipeUp();
actions.scrollToText("Sauce Labs Backpack");
actions.takeScreenshot("screenshots", "login_success");
```

---

## 📊 Test Reports

After running tests, reports are generated at:

```
target/surefire-reports/          # Maven Surefire reports
test-output/                      # TestNG HTML reports
```

Open `test-output/index.html` in a browser to view the detailed TestNG report.

---

## 🔄 Device Configuration

To run on a different device, update the capabilities in `BaseTest.java`:

```java
caps.setCapability("deviceName", "Your Device Name");    // adb devices
caps.setCapability("platformName", "Android");
caps.setCapability("automationName", "UiAutomator2");
```

> **Tip:** Use `adb devices -l` to find your device name and model.

---

## 📌 Best Practices Followed

- ✅ **Page Object Model (POM)** — Clean separation of concerns
- ✅ **W3C Actions API** — Modern gesture implementation (no deprecated TouchAction)
- ✅ **Explicit Waits** — Reliable element synchronization
- ✅ **Reusable Actions Utility** — DRY principle for common operations
- ✅ **TestNG Annotations** — Structured test lifecycle management
- ✅ **Maven Integration** — Consistent builds and CI/CD ready

---

## 🗺️ Roadmap

- [ ] Add data-driven testing with `@DataProvider`
- [ ] Integrate Extent Reports for rich HTML reporting
- [ ] Add parallel execution support
- [ ] Add iOS support with XCUITest
- [ ] CI/CD integration with GitHub Actions
- [ ] Add screenshot-on-failure listener
- [ ] Add logging with Log4j2

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m "Add new feature"`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

---

## 📝 License

This project is for educational and training purposes.

---

<div align="center">

**Built with ❤️ using Appium + TestNG + Java**

</div>
