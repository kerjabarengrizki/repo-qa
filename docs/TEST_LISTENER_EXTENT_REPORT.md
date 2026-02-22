# 📊 Test Listener & ExtentReports Implementation Guide

## 📋 Daftar Isi
- [Overview](#-overview)
- [Arsitektur](#-arsitektur)
- [TestNG ITestListener](#-testng-itestlistener)
- [ExtentReports Integration](#-extentreports-integration)
- [Screenshot on Failure](#-screenshot-on-failure)
- [Konfigurasi Test Suite](#-konfigurasi-test-suite)
- [Cara Menjalankan](#-cara-menjalankan)
- [Lokasi Report](#-lokasi-report)
- [Contoh Output](#-contoh-output)

---

## 🎯 Overview

Project ini mengimplementasikan **TestNG ITestListener** yang terintegrasi dengan **ExtentReports** untuk menghasilkan laporan test yang komprehensif dan visual. Fitur utama meliputi:

✅ **Real-time Test Monitoring** - Memantau status test saat berjalan  
✅ **Beautiful HTML Reports** - Laporan visual dengan ExtentReports  
✅ **Automatic Screenshot** - Tangkapan layar otomatis saat test gagal  
✅ **Detailed Logging** - Log lengkap dengan Log4j2  
✅ **Parallel Test Support** - Mendukung eksekusi test paralel dengan ThreadLocal  

---

## 🏗 Arsitektur

```
┌─────────────────────────────────────────────────────────────────┐
│                        TEST EXECUTION                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────┐    ┌─────────────┐    ┌─────────────┐        │
│   │  TestNG     │───▶│ ITestListener│───▶│ ExtentReports│       │
│   │  Framework  │    │ (TestListener)│   │  (Reporter)  │       │
│   └─────────────┘    └──────┬──────┘    └──────┬──────┘        │
│                             │                   │               │
│                             ▼                   ▼               │
│                      ┌─────────────┐    ┌─────────────┐        │
│                      │   Log4j2    │    │   HTML      │        │
│                      │   Logger    │    │   Report    │        │
│                      └─────────────┘    └─────────────┘        │
│                                                                 │
│   On Test Failure:                                              │
│   ┌─────────────┐    ┌─────────────┐    ┌─────────────┐        │
│   │ WebDriver   │───▶│ Screenshot  │───▶│ Attach to   │        │
│   │ Instance    │    │ Capture     │    │ Report      │        │
│   └─────────────┘    └─────────────┘    └─────────────┘        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔧 TestNG ITestListener

### Apa itu ITestListener?

`ITestListener` adalah interface dari TestNG yang memungkinkan kita untuk menangkap event-event selama eksekusi test. Interface ini menyediakan callback methods yang dipanggil pada berbagai tahap test lifecycle.

### Implementasi: `TestListener.java`

```
src/test/java/core/TestListener.java
```

### Methods yang Diimplementasikan

| Method | Deskripsi | Kapan Dipanggil |
|--------|-----------|-----------------|
| `onStart()` | Inisialisasi ExtentReports | Saat test suite dimulai |
| `onTestStart()` | Membuat test entry di report | Saat setiap test method dimulai |
| `onTestSuccess()` | Log status PASS | Saat test berhasil |
| `onTestFailure()` | Log status FAIL + Screenshot | Saat test gagal |
| `onTestSkipped()` | Log status SKIP | Saat test di-skip |
| `onFinish()` | Flush report ke file HTML | Saat test suite selesai |

### Kode Implementasi

```java
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final String REPORT_DIR = System.getProperty("user.dir") + "/reports/";
    private static final String SCREENSHOT_DIR = REPORT_DIR + "screenshots/";

    @Override
    public void onStart(ITestContext context) {
        log.info("========== Test Suite Started: {} ==========", context.getName());
        
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = REPORT_DIR + "extent-report_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setReportName("Automation Test Report");
        sparkReporter.config().setDocumentTitle("Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Environment", System.getProperty("env", "staging"));
        
        new File(SCREENSHOT_DIR).mkdirs();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        
        ExtentTest test = extent.createTest(testName, testDescription);
        test.assignCategory(result.getMethod().getGroups());
        extentTest.set(test);
        
        extentTest.get().log(Status.INFO, "Test execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        
        extentTest.get().log(Status.PASS, "Test executed successfully");
        extentTest.get().log(Status.INFO, "Execution time: " + duration + "ms");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        
        extentTest.get().log(Status.FAIL, "Test failed: " + throwable.getMessage());
        
        String screenshotPath = captureScreenshot(testName);
        if (screenshotPath != null) {
            extentTest.get().addScreenCaptureFromPath(screenshotPath, "Screenshot on failure");
        }
        
        extentTest.get().log(Status.FAIL, throwable);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
        extentTest.get().log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Total: {}, Passed: {}, Failed: {}, Skipped: {}", 
                context.getAllTestMethods().length,
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        
        extent.flush();
    }
}
```

---

## 📈 ExtentReports Integration

### Apa itu ExtentReports?

ExtentReports adalah library open-source untuk membuat laporan test HTML yang interaktif dan visual. Mendukung berbagai framework testing termasuk TestNG, JUnit, dan lainnya.

### Dependency

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.aventstack:extentreports:5.1.2")
    implementation("commons-io:commons-io:2.15.1")
}
```

### Konfigurasi ExtentSparkReporter

```java
ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
sparkReporter.config().setReportName("Automation Test Report");
sparkReporter.config().setDocumentTitle("Test Execution Report");
sparkReporter.config().setTheme(Theme.STANDARD);
sparkReporter.config().setEncoding("utf-8");
sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
```

### System Info yang Dicatat

```java
extent.setSystemInfo("OS", System.getProperty("os.name"));
extent.setSystemInfo("Java Version", System.getProperty("java.version"));
extent.setSystemInfo("User", System.getProperty("user.name"));
extent.setSystemInfo("Environment", System.getProperty("env", "staging"));
```

### Status Test yang Didukung

| Status | Warna | Deskripsi |
|--------|-------|-----------|
| `PASS` | 🟢 Hijau | Test berhasil |
| `FAIL` | 🔴 Merah | Test gagal |
| `SKIP` | 🟡 Kuning | Test di-skip |
| `INFO` | 🔵 Biru | Informasi tambahan |
| `WARNING` | 🟠 Orange | Peringatan |

---

## 📸 Screenshot on Failure

### Cara Kerja

Ketika test gagal, sistem akan:
1. Mendeteksi failure melalui `onTestFailure()` callback
2. Mengambil screenshot menggunakan Selenium's `TakesScreenshot`
3. Menyimpan file PNG ke folder `reports/screenshots/`
4. Melampirkan screenshot ke ExtentReport

### Implementasi Screenshot Capture

```java
private String captureScreenshot(String testName) {
    WebDriver driver = DriverManager.getDriver();
    if (driver == null) {
        log.warn("WebDriver is null, cannot capture screenshot");
        return null;
    }
    
    try {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;
        String relativePath = "screenshots/" + fileName;
        
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(filePath);
        FileUtils.copyFile(srcFile, destFile);
        
        log.info("Screenshot saved: {}", filePath);
        return relativePath;
    } catch (IOException e) {
        log.error("Failed to capture screenshot: {}", e.getMessage());
        return null;
    }
}
```

### Format Nama File Screenshot

```
{testMethodName}_{timestamp}.png

Contoh: testLogin_20260222_041530.png
```

### Lokasi Screenshot

```
project-root/
└── reports/
    └── screenshots/
        ├── testLogin_20260222_041530.png
        ├── testFailedLogin_20260222_041535.png
        └── ...
```

---

## ⚙️ Konfigurasi Test Suite

### Menambahkan Listener ke TestNG XML

Listener harus didaftarkan di file test suite XML:

**smoke.xml**
```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="SmokeTestSuite" verbose="1">
    <listeners>
        <listener class-name="core.TestListener"/>
    </listeners>
    <parameter name="browser" value="chrome"/>
    <test name="LoginTests">
        <classes>
            <class name="saucedemo.login.LoginTest"/>
        </classes>
    </test>
</suite>
```

**login-parallel.xml**
```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Parallel Login Test Suite" parallel="methods" thread-count="3" verbose="1">
    <listeners>
        <listener class-name="core.TestListener"/>
    </listeners>
    <parameter name="browser" value="chrome"/>
    <test name="Login Tests - Running in Parallel">
        <classes>
            <class name="saucedemo.login.LoginTest"/>
        </classes>
    </test>
</suite>
```

**regression.xml**
```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Regression Test Suite" parallel="classes" thread-count="2" verbose="1">
    <listeners>
        <listener class-name="core.TestListener"/>
    </listeners>
    <parameter name="browser" value="chrome"/>
    <test name="Login Tests">
        <classes>
            <class name="saucedemo.login.LoginTest"/>
        </classes>
    </test>
    <test name="Home Page Tests">
        <classes>
            <class name="saucedemo.home.HomePageTest"/>
        </classes>
    </test>
    <test name="Transaction Tests">
        <classes>
            <class name="saucedemo.transaction.TransactionTest"/>
        </classes>
    </test>
</suite>
```

---

## 🚀 Cara Menjalankan

### Run Smoke Test
```bash
./gradlew clean test -Psuite=smoke.xml
```

### Run Parallel Test
```bash
./gradlew clean test -Psuite=login-parallel.xml
```

### Run Regression Test
```bash
./gradlew clean test -Psuite=regression.xml
```

### Run dengan Environment Tertentu
```bash
./gradlew clean test -Psuite=smoke.xml -Penv=staging
```

---

## 📁 Lokasi Report

Setelah test selesai dijalankan, report akan tersedia di:

```
project-root/
├── reports/
│   ├── extent-report_2026-02-22_04-12-25.html    # HTML Report
│   └── screenshots/                               # Screenshot folder
│       ├── testFailedLogin_20260222_041530.png
│       └── ...
└── build/
    └── reports/
        └── tests/
            └── test/
                └── index.html                     # TestNG Default Report
```

### Membuka Report

```bash
# macOS
open reports/extent-report_*.html

# Linux
xdg-open reports/extent-report_*.html

# Windows
start reports\extent-report_*.html
```

---

## 📊 Contoh Output

### Console Log Output

```
========== Test Suite Started: SmokeTestSuite ==========
ExtentReports initialized. Report will be saved to: /reports/extent-report_2026-02-22_04-12-25.html

---------- Test Started: testLogin ----------
Test PASSED: testLogin (Duration: 5234ms)

---------- Test Started: testFailedLogin ----------
Test FAILED: testFailedLogin
Failure reason: User should see an error message when login fails
Screenshot captured: screenshots/testFailedLogin_20260222_041530.png

========== Test Suite Finished: SmokeTestSuite ==========
Total tests: 3, Passed: 2, Failed: 1, Skipped: 0
ExtentReports flushed successfully
```

### ExtentReport HTML Features

Report HTML yang dihasilkan mencakup:

1. **Dashboard** - Ringkasan eksekusi test
2. **Test Details** - Detail setiap test case
3. **Categories** - Pengelompokan berdasarkan groups
4. **Timeline** - Urutan eksekusi test
5. **Exception Logs** - Stack trace untuk test yang gagal
6. **Screenshots** - Tangkapan layar untuk test yang gagal
7. **System Info** - Informasi environment

---

## 🔧 Troubleshooting

### Report Tidak Terbuat

1. Pastikan listener sudah ditambahkan di XML:
```xml
<listeners>
    <listener class-name="core.TestListener"/>
</listeners>
```

2. Pastikan folder `reports/` memiliki write permission

### Screenshot Tidak Muncul di Report

1. Pastikan path relatif benar: `screenshots/filename.png`
2. Pastikan WebDriver tidak null saat capture
3. Cek folder `reports/screenshots/` ada dan writable

### Parallel Test Issue

Implementasi menggunakan `ThreadLocal<ExtentTest>` untuk mendukung parallel execution:
```java
private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
```

---

## 📚 Dependencies

```kotlin
// build.gradle.kts
dependencies {
    testImplementation("org.testng:testng:7.10.2")
    implementation("com.aventstack:extentreports:5.1.2")
    implementation("commons-io:commons-io:2.15.1")
    implementation("org.apache.logging.log4j:log4j-core:2.25.1")
    implementation("org.apache.logging.log4j:log4j-api:2.25.1")
    implementation("org.seleniumhq.selenium:selenium-java:4.35.0")
}
```

---

## 📝 Best Practices

1. **Gunakan deskripsi test yang jelas**
   ```java
   @Test(description = "Verify user can login with valid credentials")
   ```

2. **Kelompokkan test dengan groups**
   ```java
   @Test(groups = {"smoke", "login"})
   ```

3. **Bersihkan report lama secara berkala**
   ```bash
   rm -rf reports/*.html reports/screenshots/*
   ```

4. **Gunakan timestamp pada nama report** untuk history tracking

5. **Jangan hardcode path** - gunakan `System.getProperty("user.dir")`

---

**Last Updated**: February 22, 2026  
**Version**: 1.0.0

