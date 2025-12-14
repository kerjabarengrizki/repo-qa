package saucedemo.home;

import core.BaseTest;
import core.DriverManager;
import org.example.login.LoginPage;
import org.example.login.HomePage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @Test(groups = {"smoke"})
    public void verifyOnHomepage() {
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        loginPage.login(config.getProperty("standardUser"), config.getProperty("password")); // ini diambil dari login test
        homePage.verifyOnHomepage(); // ini diambil dari home page
    }
}
