package ecomerce;

import core.BaseTest;
import core.DriverManager;
import core.TestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.eccomerce.Login;
import org.example.eccomerce.Checkout;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import saucedemo.login.LoginTest;

public class E2ETests extends BaseTest {


    private static final Logger log = LogManager.getLogger(LoginTest.class);

    @Test(priority = 1, groups = {"e2e"}, description = "Test successful login")
    public void loginUsers() {
        log.info("User Login to The Websites");
        Login loginPage = new Login(DriverManager.getDriver());
        loginPage.loginUser(config.getProperty("emailUser"), config.getProperty("passwordUser"));

        log.info("Verifying user can see the Welcome Message after login");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the WelcomeMessage after logging in with valid credentials");
        log.info("PASSED: User can see the Welcome Message page");

        log.info("=== Test Login Successfully Completed ===");
    }

    @Test(priority = 1, groups = {"e2e"}, description = "Test success transaction")
    public void E2ETransactionSuccess() {
        log.info("User start to do transaction");
        Login loginPage = new Login(DriverManager.getDriver());
        Checkout checkoutPage = new Checkout(DriverManager.getDriver());
        loginPage.loginUser(config.getProperty("emailUser"), config.getProperty("passwordUser"));
        log.info("User search product");
        checkoutPage.searchProduct(config.getProperty("bookName"));
        log.info("User add product to cart");
        checkoutPage.checkoutProduct(config.getProperty("name"), config.getProperty("address"), config.getProperty("cardName"), config.getProperty("cardNumber"), config.getProperty("expiryMonth"), config.getProperty("expiryYear"), config.getProperty("cvv"));
        log.info("Verifying user can see the Transaction Success Message after checkout");
        Assert.assertTrue(checkoutPage.assertSuccessOrder(),
                "User should be able to see the Transaction Success Message after checkout with valid credentials");

    }

}
