package ecomerce;

import core.BaseTest;
import core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.eccomerce.Login;
import org.example.eccomerce.Checkout;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class E2ETests extends BaseTest {

    private static final Logger log = LogManager.getLogger(E2ETests.class);

    //Optional Runner for Eccomerce Tests, if you want to run with specific browser, you can pass the parameter in testng.xml, otherwise it will default to chrome
    @Override
    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        DriverManager.initDriver(browser);
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().get(config.getProperty("bookstoreUrl"));
    }

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
