package saucedemo.login;

import core.BaseTest;
import core.DriverManager;
import org.example.login.LoginPage;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{

    //test aja trigger pipeline
    @Test(groups = {"smoke"})
    public void testLogin() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(config.getProperty("standardUser"), config.getProperty("password"));
    }

    public void testFailedLogin(){
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(config.getProperty("failedUser"), config.getProperty("password"));
    }

}
