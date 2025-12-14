# CI/CD Setup Guide - Quick Reference

## 📋 Files Required for CI/CD

### Essential Configuration Files

| File Path | Purpose | Required for CI/CD |
|-----------|---------|-------------------|
| `.github/workflows/ci.yml` | GitHub Actions workflow configuration | ✅ **REQUIRED** |
| `gradlew` | Gradle wrapper for Unix/Linux/macOS | ✅ **REQUIRED** |
| `gradlew.bat` | Gradle wrapper for Windows | ✅ **REQUIRED** |
| `build.gradle.kts` | Build and test configuration | ✅ **REQUIRED** |
| `src/test/resources/suites/smoke.xml` | TestNG test suite definition | ✅ **REQUIRED** |
| `src/test/resources/config/staging.properties` | Environment configuration | ✅ **REQUIRED** |
| `src/test/java/core/DriverManager.java` | WebDriver setup with CI detection | ✅ **REQUIRED** |
| `src/test/java/core/BaseTest.java` | Base test class | ✅ **REQUIRED** |

---

## 🔧 Configuration File Details

### 1. `.github/workflows/ci.yml`

**Location**: `.github/workflows/ci.yml`

**Purpose**: Defines the GitHub Actions CI/CD pipeline

**Key Configuration Points**:

```yaml
# When to trigger the workflow
on:
  push:
    branches: [main, master, develop]  # ← Change these branch names if needed
  pull_request:
    branches: [main, master, develop]  # ← Change these branch names if needed

# Which test suites to run
strategy:
  matrix:
    suite: [smoke.xml]  # ← Add more suites here: [smoke.xml, regression.xml]

# Test execution command
run: ./gradlew clean test -Psuite=${{ matrix.suite }} -Penv=staging --no-daemon --stacktrace
                                                    # ↑ Change environment here
```

**What You Can Customize**:
- Branch triggers (lines 4-8)
- Test suites to run (line 17)
- Environment (line 39)
- Java version (line 24)
- Chrome version (line 29)
- Artifact retention days (lines 52, 60)

---

### 2. `build.gradle.kts`

**Location**: `build.gradle.kts`

**Purpose**: Gradle build configuration with test setup

**Key Configuration Points**:

```kotlin
// Test execution configuration
tasks.test {
    useTestNG {
        val suite: String = if (project.hasProperty("suite")) {
            project.property("suite") as String
        } else {
            "smoke.xml"  // ← Default suite when no -Psuite parameter
        }
        println("Run test suite: $suite")
        suiteXmlFiles = listOf(file("src/test/resources/suites/$suite"))
                                    // ↑ Suite files location
        
        if (project.hasProperty("env")) {
            systemProperty("env", project.property("env") as String)
        }
    }
}
```

**What You Can Customize**:
- Default test suite (line 7)
- Test suite path (line 10)
- JVM arguments
- Parallel execution settings
- Test dependencies

---

### 3. `src/test/resources/suites/smoke.xml`

**Location**: `src/test/resources/suites/smoke.xml`

**Purpose**: TestNG suite configuration

**Example Configuration**:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Smoke Test Suite" parallel="false" thread-count="1">
    <!-- ↑ Enable parallel: parallel="methods" or parallel="classes" -->
    
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>  <!-- ↑ Test group to include -->
            </run>
        </groups>
        <packages>
            <package name="saucedemo.*"/>   <!-- ↑ Test packages to run -->
            <package name="nopcommerce"/>
        </packages>
    </test>
</suite>
```

**How to Create New Suite**:
1. Copy `smoke.xml` to new file (e.g., `regression.xml`)
2. Update suite name and included groups
3. Add to ci.yml matrix: `suite: [smoke.xml, regression.xml]`

---

### 4. `src/test/resources/config/staging.properties`

**Location**: `src/test/resources/config/staging.properties`

**Purpose**: Environment-specific configuration

**Current Setup**:

```properties
# Base URL for the application
baseUrl=https://demo.nopcommerce.com/login

# Test credentials
username=bruno@gmail.com
password=admin123

# Test data
firstName=John
lastName=Doe
postalCode=12345
```

**How to Add New Environment**:
1. Create new file: `production.properties`, `qa.properties`
2. Update ci.yml: `-Penv=production`

---

### 5. `src/test/java/core/DriverManager.java`

**Location**: `src/test/java/core/DriverManager.java`

**Purpose**: WebDriver initialization with CI/CD support

**CI Detection Logic**:

```java
// Automatically detects GitHub Actions environment
String githubActions = System.getenv("GITHUB_ACTIONS");
if (githubActions != null && githubActions.equals("true")) {
    // Headless mode configuration
    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");
}

// Cloudflare bypass (works in both local and CI)
options.addArguments("--disable-blink-features=AutomationControlled");
options.setExperimentalOption("useAutomationExtension", false);
```

**What You Can Customize**:
- Browser options
- Window size for headless mode
- User agent strings
- Timeout values

---

## 🚀 Installation Steps

### Step-by-Step Setup

#### 1. **Clone Repository**
```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
cd project-demo
```

#### 2. **Verify/Create Gradle Wrapper**
```bash
# Check if gradlew exists
ls -la gradlew

# If missing, create it
gradle wrapper --gradle-version=8.5

# Make it executable (macOS/Linux)
chmod +x gradlew

# Verify
./gradlew --version
```

#### 3. **Install Dependencies**
```bash
./gradlew build --refresh-dependencies
```

#### 4. **Test Locally**
```bash
# Run smoke tests
./gradlew clean test -Psuite=smoke.xml

# Test in headless mode (CI simulation)
export GITHUB_ACTIONS=true
./gradlew clean test -Psuite=smoke.xml
```

#### 5. **Commit Required Files**
```bash
# Make sure these files are committed
git add .github/workflows/ci.yml
git add gradlew
git add gradlew.bat
git add gradle/wrapper/
git add build.gradle.kts
git add src/test/resources/
git add src/test/java/core/
git add src/main/java/

# Commit
git commit -m "Add CI/CD configuration"

# Push to GitHub
git push origin main
```

#### 6. **Verify GitHub Actions**
1. Go to GitHub repository
2. Click **"Actions"** tab
3. Wait for workflow to complete
4. Download test reports from artifacts

---

## 🔍 Verification Checklist

Before pushing to GitHub, verify:

### Local Verification
- [ ] `./gradlew --version` works
- [ ] `./gradlew clean compileJava compileTestJava` succeeds
- [ ] `./gradlew clean test -Psuite=smoke.xml` runs tests
- [ ] Test reports generated: `build/reports/tests/test/index.html`
- [ ] Headless mode works: `export GITHUB_ACTIONS=true && ./gradlew test`

### File Verification
- [ ] `.github/workflows/ci.yml` exists
- [ ] `gradlew` exists and is executable (`chmod +x gradlew`)
- [ ] `gradlew.bat` exists
- [ ] `gradle/wrapper/gradle-wrapper.jar` exists
- [ ] `gradle/wrapper/gradle-wrapper.properties` exists
- [ ] `src/test/resources/suites/smoke.xml` exists
- [ ] `src/test/resources/config/staging.properties` exists

### GitHub Actions Verification
- [ ] Workflow appears in Actions tab
- [ ] Workflow runs automatically on push
- [ ] Tests execute successfully
- [ ] Test reports uploaded as artifacts
- [ ] Test results show in PR checks

---

## 🎯 Quick Commands Reference

### Local Development
```bash
# Run default suite (smoke.xml)
./gradlew clean test

# Run specific suite
./gradlew clean test -Psuite=smoke.xml

# Run with different environment
./gradlew clean test -Psuite=smoke.xml -Penv=production

# Run specific test class
./gradlew test --tests "saucedemo.login.LoginTest"

# Run in headless mode
export GITHUB_ACTIONS=true
./gradlew clean test
```

### CI/CD Commands
```bash
# Regenerate Gradle wrapper
gradle wrapper --gradle-version=8.5

# Make gradlew executable
chmod +x gradlew

# Test CI configuration locally
export GITHUB_ACTIONS=true
./gradlew clean test -Psuite=smoke.xml -Penv=staging --no-daemon --stacktrace
```

### View Reports
```bash
# macOS
open build/reports/tests/test/index.html

# Linux
xdg-open build/reports/tests/test/index.html

# Windows
start build\reports\tests\test\index.html
```

---

## 🐛 Common Issues & Solutions

### 1. **gradlew: Permission denied**
```bash
chmod +x gradlew
```

### 2. **gradlew file not found**
```bash
gradle wrapper --gradle-version=8.5
```

### 3. **GitHub Actions fails: "No tests found"**
- Verify suite file path in `build.gradle.kts`
- Check suite file exists: `ls -la src/test/resources/suites/smoke.xml`
- Ensure test classes have `@Test` annotation

### 4. **Chrome not found in CI**
- Workflow already includes Chrome installation step
- Verify `.github/workflows/ci.yml` has `browser-actions/setup-chrome@v1`

### 5. **Tests pass locally but fail in CI**
- Add more wait time for headless mode
- Check window size: `--window-size=1920,1080`
- Review CI test reports in artifacts
- Add explicit waits instead of Thread.sleep

---

## 📚 Additional Resources

- **Main README**: [README.md](README.md)
- **GitHub Actions Workflow**: [.github/workflows/ci.yml](.github/workflows/ci.yml)
- **CI/CD Documentation**: [.github/workflows/README.md](.github/workflows/README.md)

---

## 📞 Need Help?

If you encounter issues:
1. Check this guide for common solutions
2. Review test logs in GitHub Actions
3. Download and inspect test report artifacts
4. Check `build/reports/tests/test/index.html` locally

---

**Last Updated**: December 14, 2024

