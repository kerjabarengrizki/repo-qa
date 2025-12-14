package saucedemo.transaction;

import core.BaseTest;
import core.DriverManager;
import org.example.login.LoginPage;
import org.example.login.HomePage;
import org.example.login.Transaction;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class TransactionTest extends BaseTest {

    @Test(groups = {"smoke"})
    public void transactionSuccess() {
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver); // memanggil login page
        HomePage homePage = new HomePage(driver); // memanggil home page
        Transaction transaction = new Transaction(driver); //memanggil transaction page
        loginPage.login(config.getProperty("standardUser"), config.getProperty("password")); // ini diambil dari login test
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        homePage.addToCart(); // ini diambil dari home page
        transaction.submitTransaction(); // ini diambil dari transaction
        transaction.verifySuccessTransaction(); //ini assertionsnya
        //coba untuk trigger pipeline
    }
}
