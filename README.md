# Selenium Automation Testing Project

![CI](https://github.com/YOUR_USERNAME/YOUR_REPO_NAME/actions/workflows/ci.yml/badge.svg)

## 📋 Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Configuration Files](#configuration-files)
- [Running Tests Locally](#running-tests-locally)
- [CI/CD Setup](#cicd-setup)
- [Test Reports](#test-reports)
- [Troubleshooting](#troubleshooting)

## 🎯 Overview
This is a Selenium WebDriver automation testing framework built with Java, Gradle, and TestNG. It supports multiple test environments, parallel execution, and includes CI/CD integration with GitHub Actions.

## 💻 Tech Stack
- **Java**: 17
- **Selenium WebDriver**: 4.35.0
- **TestNG**: 7.10.2
- **Gradle**: 8.5
- **WebDriverManager**: 6.3.2 (Automatic driver management)
- **Log4j2**: 2.25.1 (Logging framework)
- **AssertJ**: 3.27.3 (Fluent assertions)

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required Software
1. **Java JDK 17 or higher**
   ```bash
   # Check Java version
   java -version
   ```
   Download from: https://adoptium.net/

2. **Google Chrome Browser** (latest stable version)
   ```bash
   # Check Chrome version (macOS)
   /Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --version
   ```

3. **Git**
   ```bash
   # Check Git version
   git --version
   ```

### Optional (Gradle wrapper included in project)
- **Gradle 8.5+** (Optional - the project includes Gradle wrapper)

---

## 🚀 Installation

### Step 1: Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
cd project-demo
```

### Step 2: Verify Gradle Wrapper
Ensure the `gradlew` file has execute permissions:
```bash
# macOS/Linux
chmod +x gradlew
ls -la gradlew

# If gradlew is missing, regenerate it:
gradle wrapper --gradle-version=8.5
```

### Step 3: Download Dependencies
```bash
./gradlew build --refresh-dependencies
```

### Step 4: Verify Installation
```bash
# Check Gradle version
./gradlew --version

# Compile the project
./gradlew clean compileJava compileTestJava
```

---

## 📁 Project Structure

```
project-demo/
├── .github/
│   └── workflows/
│       ├── ci.yml                    # ⚙️ GitHub Actions CI/CD configuration
│       └── README.md                  # CI/CD documentation
├── src/
│   ├── main/java/org/example/
│   │   ├── core/
│   │   │   └── BasePage.java         # Base page object class
│   │   ├── login/
│   │   │   ├── LoginPage.java
│   │   │   ├── HomePage.java
│   │   │   └── Transaction.java
│   │   └── nopcommerce/
│   │       ├── LoginPage.java
│   │       ├── HomePage.java
│   │       ├── Cart.java
│   │       └── CheckoutPage.java
│   └── test/
│       ├── java/
│       │   └── core/
│       │       ├── BaseTest.java      # 🔧 Base test configuration
│       │       ├── DriverManager.java # 🔧 WebDriver setup & Cloudflare bypass
│       │       ├── ConfigReader.java  # 🔧 Reads environment configs
│       │       ├── TestListener.java
│       │       └── TestUtils.java
│       │   ├── nopcommerce/
│       │   │   └── TransactionTest.java
│       │   └── saucedemo/
│       │       ├── login/LoginTest.java
│       │       ├── home/HomePageTest.java
│       │       └── transaction/TransactionTest.java
│       └── resources/
│           ├── config/
│           │   └── staging.properties  # 🔧 Environment configuration
│           └── suites/
│               └── smoke.xml           # 🔧 TestNG suite configuration
├── build.gradle.kts                    # 🔧 Gradle build configuration
├── settings.gradle.kts
├── gradlew                             # ⚙️ Gradle wrapper (Unix/Linux/macOS)
├── gradlew.bat                         # ⚙️ Gradle wrapper (Windows)
└── README.md                           # This file
```

**Legend:**
- 🔧 = Configuration files you need to modify
- ⚙️ = Setup/system files

---

## ⚙️ Configuration Files

### 1. **CI/CD Configuration** (Required for GitHub Actions)

#### `.github/workflows/ci.yml`
**Purpose**: Defines the GitHub Actions workflow for automated testing

**Key Configurations:**
```yaml
# Trigger events
on:
  push:
    branches: [main, master, develop]
  pull_request:
    branches: [main, master, develop]

# Test suite to run
strategy:
  matrix:
    suite: [smoke.xml]

# Environment variable for headless mode
env:
  GITHUB_ACTIONS: true
```

**What it does:**
- ✅ Checks out code from repository
- ✅ Sets up Java 17 with Gradle caching
- ✅ Installs Chrome browser automatically
- ✅ Runs tests in headless mode
- ✅ Generates and uploads test reports
- ✅ Publishes test results as GitHub annotations

---

### 2. **Build Configuration**

#### `build.gradle.kts`
**Purpose**: Gradle build script with dependencies and test configuration

**Key Sections:**
```kotlin
dependencies {
    testImplementation("org.testng:testng:7.10.2")
    implementation("org.seleniumhq.selenium:selenium-java:4.35.0")
    implementation("io.github.bonigarcia:webdrivermanager:6.3.2")
    // ... other dependencies
}

tasks.test {
    useTestNG {
        val suite: String = if (project.hasProperty("suite")) {
            project.property("suite") as String
        } else {
            "smoke.xml"
        }
        suiteXmlFiles = listOf(file("src/test/resources/suites/$suite"))
        if (project.hasProperty("env")) {
            systemProperty("env", project.property("env") as String)
        }
    }
}
```

**How to modify:**
- Add new dependencies in the `dependencies` block
- Change default test suite in `tasks.test` block

---

### 3. **Environment Configuration**

#### `src/test/resources/config/staging.properties`
**Purpose**: Contains environment-specific settings (URLs, credentials, test data)

**Current Configuration:**
```properties
baseUrl=https://demo.nopcommerce.com/login
username=bruno@gmail.com
password=admin123
failedUser=locked_out_user
firstName=John
lastName=Doe
postalCode=12345
```

**How to add new environments:**
1. Create new file: `production.properties`, `qa.properties`, etc.
2. Use `-Penv=production` to switch environments

---

### 4. **Test Suite Configuration**

#### `src/test/resources/suites/smoke.xml`
**Purpose**: TestNG suite file that defines which tests to run

**Example Configuration:**
```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Smoke Test Suite" parallel="false" thread-count="1">
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <packages>
            <package name="saucedemo.*"/>
            <package name="nopcommerce"/>
        </packages>
    </test>
</suite>
```

**How to create new suites:**
1. Create new XML file in `src/test/resources/suites/`
2. Run with: `./gradlew test -Psuite=your-suite.xml`

---

### 5. **WebDriver Configuration**

#### `src/test/java/core/DriverManager.java`
**Purpose**: Manages WebDriver instances with Cloudflare bypass and CI/CD support

**Key Features:**
```java
// Cloudflare bypass options
options.addArguments("--disable-blink-features=AutomationControlled");
options.addArguments("--user-agent=Mozilla/5.0...");
options.setExperimentalOption("useAutomationExtension", false);

// GitHub Actions detection (headless mode)
String githubActions = System.getenv("GITHUB_ACTIONS");
if (githubActions != null && githubActions.equals("true")) {
    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
}
```

**What you can modify:**
- Browser options
- Headless mode settings
- User agent strings
- Timeouts and window size

---

### 6. **Base Test Configuration**

#### `src/test/java/core/BaseTest.java`
**Purpose**: Base class for all test classes with setup/teardown

**Key Methods:**
```java
@BeforeSuite
public void loadConfig() {
    // Loads environment config (staging, production, etc.)
}

@BeforeTest
public void setUp(@Optional("chrome") String browser) {
    // Initializes WebDriver, maximizes window, navigates to baseUrl
}

@AfterTest
public void tearDown() {
    // Closes browser and cleans up
}
```

---

## 🏃 Running Tests Locally

### Run Default Test Suite (smoke.xml)
```bash
./gradlew clean test
```

### Run Specific Test Suite
```bash
./gradlew clean test -Psuite=smoke.xml
```

### Run with Different Environment
```bash
./gradlew clean test -Psuite=smoke.xml -Penv=staging
```

### Run in Headless Mode (Simulating CI)
```bash
# macOS/Linux
export GITHUB_ACTIONS=true
./gradlew clean test -Psuite=smoke.xml

# Windows
set GITHUB_ACTIONS=true
gradlew clean test -Psuite=smoke.xml
```

### Run Specific Test Class
```bash
./gradlew test --tests "saucedemo.login.LoginTest"
```

### Run with Detailed Logging
```bash
./gradlew clean test -Psuite=smoke.xml --info
```

---

## 🔄 CI/CD Setup

### Setting Up GitHub Actions

#### Step 1: Ensure Required Files Exist
Verify these files are committed to your repository:
```bash
# Must have execute permissions
✅ gradlew
✅ gradlew.bat
✅ .github/workflows/ci.yml
✅ src/test/resources/suites/smoke.xml
✅ src/test/resources/config/staging.properties
```

#### Step 2: Commit and Push to GitHub
```bash
# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Add CI/CD configuration"

# Add remote repository
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# Push to main branch
git push -u origin main
```

#### Step 3: Verify Workflow
1. Go to your GitHub repository
2. Click on **"Actions"** tab
3. You should see the workflow "CI - Automation Tests" running
4. Click on the workflow run to see real-time logs

#### Step 4: Download Test Reports
After the workflow completes:
1. Click on the completed workflow run
2. Scroll down to **"Artifacts"** section
3. Download `test-reports-smoke.xml` and `test-results-smoke.xml`
4. Extract and open `index.html` for detailed test reports

---

## 📊 Test Reports

### Local Test Reports

After running tests locally, view the HTML report:

```bash
# macOS
open build/reports/tests/test/index.html

# Linux
xdg-open build/reports/tests/test/index.html

# Windows
start build/reports/tests/test/index.html
```

### CI Test Reports

Test reports are automatically uploaded as artifacts in GitHub Actions:
- **HTML Reports**: `build/reports/tests/test/`
- **XML Results**: `build/test-results/test/*.xml`
- **Retention**: 30 days

---

## 🛠️ Troubleshooting

### 1. **Permission Denied: gradlew**
```bash
# Fix: Make gradlew executable
chmod +x gradlew

# Or regenerate wrapper
gradle wrapper --gradle-version=8.5
```

### 2. **Chrome Driver Not Found**
The project uses WebDriverManager which automatically downloads ChromeDriver. If issues persist:
```bash
# Clear cache and redownload
rm -rf ~/.cache/selenium
./gradlew clean test
```

### 3. **Tests Fail in CI but Pass Locally**
- Check if elements need more wait time in headless mode
- Verify window size is sufficient: `--window-size=1920,1080`
- Add explicit waits in page objects
- Check test reports in GitHub Actions artifacts

### 4. **Cloudflare Blocking Tests**
The `DriverManager` includes Cloudflare bypass options. If still blocked:
- Increase Thread.sleep delays
- Add more realistic user interactions
- Use explicit waits instead of Thread.sleep
- Check if the website has additional bot protection

### 5. **Out of Memory in CI**
```kotlin
// Add to build.gradle.kts
tasks.test {
    maxHeapSize = "1g"
    jvmArgs = listOf("-Xmx1g")
}
```

### 6. **TestNG Suite Not Found**
Verify the suite file path:
```bash
# Check file exists
ls -la src/test/resources/suites/smoke.xml

# Verify path in build.gradle.kts matches
suiteXmlFiles = listOf(file("src/test/resources/suites/$suite"))
```

---

## 📝 Additional Resources

- [GitHub Actions Workflow Documentation](.github/workflows/README.md)
- [Selenium WebDriver Docs](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make your changes
4. Run tests locally: `./gradlew clean test`
5. Commit changes: `git commit -m "Add your feature"`
6. Push to branch: `git push origin feature/your-feature`
7. Submit a pull request

---

## 📄 License
MIT License

---

## 📧 Contact

For questions or issues, please open an issue on GitHub.

---

**Note**: 
- Replace `YOUR_USERNAME` and `YOUR_REPO_NAME` in the badge URL and git commands with your actual GitHub username and repository name.
- Update the contact section with your information.

