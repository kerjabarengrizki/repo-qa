package org.example.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Transaction extends BasePage {
    private static final Logger log = LogManager.getLogger(Transaction.class);

    public Transaction(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//button[@id='checkout']")
    private WebElement checkoutButton;

    @FindBy(xpath = "//input[@id='first-name']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@id='last-name']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@id='postal-code']")
    private WebElement postalCodeInput;

    @FindBy(xpath = "//button[@id='finish']")
    private WebElement finishButton;

    @FindBy(xpath = "//a[@class='btn_action cart_button']")
    private WebElement submitButton;

    @FindBy(xpath = "//h2[@class='complete-header']")
    private WebElement thankyouOrder;

    @FindBy(xpath = "//input[@id='continue']")
    private WebElement continueButton;

    public void submitTransaction() {
        log.info("submit Transaction");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(checkoutButton));
        checkoutButton.click();
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        firstNameInput.sendKeys("John");
        lastNameInput.sendKeys("Doe");
        postalCodeInput.sendKeys("12345");
        continueButton.click();
        finishButton.click();
    }

    public void verifySuccessTransaction() {
        log.info("success Transaction");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(thankyouOrder));
        thankyouOrder.isDisplayed();
        //Assert.assertTrue(thankyouOrder.isDisplayed(), "Success transaction message should be displayed");
        log.info("Transaction completed successfully");
    }
}