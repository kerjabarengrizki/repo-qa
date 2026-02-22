package saucedemo.login;

import core.BaseTest;
import core.DriverManager;
import core.TestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.saucedemo.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(LoginTest.class);

    @Test(priority = 3, groups = {"smoke"}, description = "Test successful login")
    public void testLogin() {
        log.info("========== Starting testLogin ==========");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(config.getProperty("standardUser"), config.getProperty("password"));

        log.info("Verifying user can see the Products page after login");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");
        log.info("PASSED: User can see the Products page");

        log.info("Verifying user is redirected to inventory page");
        Assert.assertTrue(loginPage.getCurrentUrl().contains("inventory"),
                "User should be redirected to the inventory page after successful login");
        log.info("PASSED: User is redirected to inventory page");

        log.info("Verifying no error message is displayed");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "User should not see any error message after successful login");
        log.info("PASSED: No error message is displayed");

        log.info("========== testLogin completed successfully ==========");
    }

    @Test(priority = 2, description = "Test failed login scenario")
    public void testFailedLogin() {
        log.info("========== Starting testFailedLogin ==========");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(config.getProperty("failedUser"), config.getProperty("password"));

        SoftAssert softAssert = new SoftAssert();

        log.info("Verifying error message is displayed for failed login");
        softAssert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "User should see an error message when login fails");
        log.info("Checked: Error message visibility");

        log.info("Verifying user cannot access Products page with invalid credentials");
        softAssert.assertFalse(loginPage.isUserLoggedInSuccessfully(),
                "User should not be able to access the Products page with invalid credentials");
        log.info("Checked: Products page access denied");

        log.info("Verifying error message contains 'locked out' text");
        softAssert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "User should be informed that the account has been locked");
        log.info("Checked: Error message content");

        log.info("Running all soft assertions");
        softAssert.assertAll();
        log.info("========== testFailedLogin completed ==========");
    }

    @DataProvider(name = "loginCredentials", parallel = true)
    public Object[][] loginCredentials() {
        log.info("Loading test data from Excel file");
        return TestUtils.getTestData("src/test/resources/data/login-data-test.xlsx", "login-tests");
    }

    @Test(priority = 1, dataProvider = "loginCredentials", description = "Data-driven login test")
    public void testDataDriven(String username, String password, String expectedResult) {
        log.info("========== Starting testDataDriven ==========");
        log.info("Test data - Username: {}, Expected Result: {}", username, expectedResult);

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(username, password);

        SoftAssert softAssert = new SoftAssert();

        if (expectedResult.equalsIgnoreCase("success")) {
            log.info("Verifying successful login for user: {}", username);

            log.info("Checking if user is logged in successfully");
            softAssert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                    "User with username '" + username + "' should be able to login successfully");
            log.info("Checked: Login success status");

            log.info("Checking if user is redirected to inventory page");
            softAssert.assertTrue(loginPage.getCurrentUrl().contains("inventory"),
                    "User should be redirected to the inventory page after successful login");
            log.info("Checked: Inventory page redirection");
        } else {
            log.info("Verifying failed login for user: {}", username);

            log.info("Checking if error message is displayed");
            softAssert.assertTrue(loginPage.isErrorMessageDisplayed(),
                    "User with username '" + username + "' should see an error message");
            log.info("Checked: Error message visibility");

            log.info("Checking if user cannot access Products page");
            softAssert.assertFalse(loginPage.isUserLoggedInSuccessfully(),
                    "User should not be able to access the Products page with invalid credentials");
            log.info("Checked: Products page access denied");
        }

        log.info("Running all soft assertions for user: {}", username);
        softAssert.assertAll();
        log.info("========== testDataDriven completed for user: {} ==========", username);
    }

}
