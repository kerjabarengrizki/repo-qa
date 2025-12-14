package org.example.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.assertj.core.api.Assertions;
import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorAlert;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        log.info("Logging in with username: {}", username);
        waitForElementToBeVisible(usernameInput);
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    //public void errorMessage() {
     //   Assertions.assertThat(errorAlert.getText())
         //       .as("Login alert should be displayed")
         //       .isEqualTo("Epic sadface: Username and password do not match any user in this service");
   // }

}
