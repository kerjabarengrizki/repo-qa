package org.example.eccomerce;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Login extends BasePage {

    private static final Logger log = LogManager.getLogger(Login.class);


    @FindBy(xpath = "//*[@id='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//*[@id='core']/div/div/nav[2]/div/div/a")
    private WebElement signInCta;

    @FindBy(xpath = "//*[@id='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//*[@id='submit']")
    private WebElement signInButton;

    @FindBy(xpath = "//*[@id='welcome-message']")
    private WebElement welcomeMessage;

    public Login(WebDriver driver) {
        super(driver);
        log.info("LoginPage initialized");
    }

    public void loginUser(String email, String password) {
        log.info("Attempting to login with username: {}", email);
        waitForElementToBeVisible(signInCta);
        signInCta.click();
        waitForElementToBeVisible(emailInput);
        scrollToElement(emailInput);
        emailInput.sendKeys(email);
        log.info("Email entered");
        passwordInput.sendKeys(password);
        log.info("Password entered");
        signInButton.click();
        log.info("Login button clicked");
    }

    public boolean isUserLoggedInSuccessfully() {
        log.info("Checking if user is logged in successfully");
        try {
            waitForElementToBeVisible(welcomeMessage);
            String actualText = welcomeMessage.getText();
            String expectedText = "Hello rizki";
            log.info("Expected text: '{}' | Actual text: '{}'", expectedText, actualText);
            boolean isLoggedIn = welcomeMessage.isDisplayed() && actualText.equals(expectedText);
            if (isLoggedIn) {
                log.info("User is logged in successfully - Welcome message is displayed");
            } else {
                log.warn("User is not logged in - Welcome message is not displayed");
            }
            return isLoggedIn;
        } catch (Exception e) {
            log.error("Failed to verify login status: {}", e.getMessage());
            return false;
        }
    }


}
