# CI Troubleshooting Guide

## Quick Diagnosis Steps

### 1. Check CI Logs
Go to: https://github.com/kerjabarengrizki/repo-qa/actions

Look for these key sections:

#### A. Chrome Installation (Should show)
```
Chrome version: 131
Chrome installed at: /usr/bin/google-chrome
```

#### B. ChromeDriver Installation (Should show)
```
ChromeDriver version: 131.0.6778.87
ChromeDriver installed at: /usr/local/bin/chromedriver
```

#### C. Driver Initialization (Should show)
```
=== Driver Initialization ===
Running in CI: true
ChromeDriver created successfully!
```

### 2. Common Errors and Solutions

#### Error: "SessionNotCreatedException"
**Cause:** Version mismatch or missing binaries

**Solution:**
- Check if Chrome and ChromeDriver versions match (same major version)
- Verify paths are correct in environment variables
- Check if ChromeDriver has execute permissions

#### Error: "Chrome failed to start"
**Cause:** Missing dependencies or wrong binary path

**Solution:**
Add to CI workflow before Chrome installation:
```yaml
- name: Install Chrome Dependencies
  run: |
    sudo apt-get update
    sudo apt-get install -y libnss3 libxss1 libasound2 libatk-bridge2.0-0 libgtk-3-0
```

#### Error: "Cannot find Chrome binary"
**Cause:** CHROME_BIN path is incorrect

**Solution:**
Update CHROME_BIN in workflow:
```yaml
env:
  CHROME_BIN: /opt/google/chrome/chrome  # or /usr/bin/google-chrome
```

### 3. Debug Mode

To get even more verbose output, add to `build.gradle.kts`:

```kotlin
tasks.test {
    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showStackTraces = true
    }
}
```

### 4. Manual Local Testing (Simulate CI)

Test locally with same CI settings:

```bash
export GITHUB_ACTIONS=true
export CHROME_BIN=/Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome
export CHROMEDRIVER_PATH=/usr/local/bin/chromedriver

./gradlew clean test -Psuite=smoke.xml -Penv=staging
```

### 5. Check Test Reports

After CI run, download test reports artifact:
1. Go to failed workflow run
2. Scroll to "Artifacts" section
3. Download `test-reports-smoke.xml`
4. Open `index.html` in browser

### 6. Alternative Solutions

#### Option A: Use Selenium Docker Container
```yaml
services:
  selenium:
    image: selenium/standalone-chrome:latest
    ports:
      - 4444:4444
```

Then update `DriverManager`:
```java
ChromeOptions options = new ChromeOptions();
webDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
```

#### Option B: Use GitHub's Pre-installed Browsers
```yaml
- name: Use Pre-installed Chrome
  run: |
    echo "Chrome: $(which google-chrome-stable || which google-chrome)"
    google-chrome-stable --version || google-chrome --version
```

#### Option C: Disable Headless for Debugging
Temporarily disable headless mode to see if it's a headless-specific issue:
```java
// Comment out in DriverManager:
// options.addArguments("--headless=new");
```

## Getting Help

If still failing, collect these details:

1. Full CI log output (from Actions tab)
2. Test report HTML (from artifacts)
3. Chrome version shown in logs
4. ChromeDriver version shown in logs
5. Error stack trace

Then check:
- GitHub Issues: https://github.com/SeleniumHQ/selenium/issues
- Stack Overflow: Tag `selenium` + `github-actions`
- ChromeDriver docs: https://chromedriver.chromium.org/

## Quick Commands Reference

```bash
# Check local setup
chromedriver --version
google-chrome --version

# Clean and rebuild
./gradlew clean build

# Run specific test
./gradlew test --tests saucedemo.login.LoginTest

# Run with debug
./gradlew test --debug > test-debug.log 2>&1

# Check Gradle version
./gradlew --version
```

