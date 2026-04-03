package org.example.eccomerce;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Checkout extends BasePage {

    private static final Logger log = LogManager.getLogger(Checkout.class);

    @FindBy(xpath = "//*[@id='search-input']")
    private WebElement seacrhInput;

    @FindBy(xpath = "//*[@id='search-btn']")
    private WebElement searchBtn;

    @FindBy(xpath = "//a[normalize-space()='Add To Cart']")
    private WebElement addToCartBtn;

    @FindBy(xpath = "//*[@id='core']/div/div/nav[2]/div/a[2]/img")
    private WebElement cartIcon;

    @FindBy(xpath = "//button[text()='Update']")
    private WebElement updateBtn;

    @FindBy(xpath = "//button[text()='Delete']")
    private WebElement deleteBtn;

    @FindBy(xpath = "//a[@data-testid='checkout']")
    private WebElement procedToCheckoutBtn;

    @FindBy(xpath = "//*[@id='name']")
    private WebElement nameInput;

    @FindBy(xpath = "//*[@id='address']")
    private WebElement addressInput;

    @FindBy(xpath = "//*[@id='card-name']")
    private WebElement cardNameInput;

    @FindBy(xpath = "//*[@id='card-number']")
    private WebElement cardNumberInput;

    @FindBy(xpath = "//*[@id='card-expiry-month']")
    private WebElement expriryMonthInput;

    @FindBy(xpath = "//*[@id='card-expiry-year']")
    private WebElement expiryYearInput;

    @FindBy(xpath = "//*[@id='card-cvc']")
    private WebElement cvcInput;

    @FindBy(xpath = "//button[text()='Purchase']")
    private WebElement purchaseBtn;

    @FindBy(xpath = "//*[@id='flash']")
    private WebElement successMessage;

    @FindBy(xpath = "//*[@id='deleteOrdersBtn']")
    private WebElement deleteOrdersBtn;

    @FindBy(xpath = "//*[@id='core']/div/div/nav[2]/div/a[1]")
    private WebElement allBookHome;



    public Checkout(WebDriver driver) {
        super(driver);
        log.info("Checkout page initialized");
    }

    public void searchProduct(String bookName) {
        log.info("Searching for book: {}", bookName);
        waitForElementToBeVisible(allBookHome);
        allBookHome.click();
        waitForElementToBeVisible(seacrhInput);
        seacrhInput.sendKeys(bookName);
        searchBtn.click();
    }

    public void checkoutProduct(String name, String address, String cardName, String cardNumber, String expiryMonth, String expiryYear, String cvc) {
        log.info("Proceeding to checkout");
        waitForElementToBeVisible(addToCartBtn);
        scrollToElement(addToCartBtn);
        waitForElementToBeClickable(addToCartBtn);
        addToCartBtn.click();
        waitForElementToBeVisible(cartIcon);
        cartIcon.click();
        waitForElementToBeVisible(procedToCheckoutBtn);
        scrollToElement(procedToCheckoutBtn);
        procedToCheckoutBtn.click();
        waitForElementToBeVisible(nameInput);
        nameInput.sendKeys(name);
        addressInput.sendKeys(address);
        cardNameInput.sendKeys(cardName);
        cardNumberInput.sendKeys(cardNumber);
        expriryMonthInput.sendKeys(expiryMonth);
        expiryYearInput.sendKeys(expiryYear);
        cvcInput.sendKeys(cvc);
        scrollToElement(purchaseBtn);
        waitForElementToBeVisible(purchaseBtn);
        purchaseBtn.click();

    }

    public boolean assertSuccessOrder() {
        log.info("Checking if user order in successfully");
        try {
            waitForElementToBeVisible(successMessage);
            String actualText = successMessage.getText();
            String expectedText = "Your purchase was successful! Thank you for your order.";
            log.info("Expected text: '{}' | Actual text: '{}'", expectedText, actualText);
            boolean isLoggedIn = successMessage.isDisplayed() && actualText.contains(expectedText);
            if (isLoggedIn) {
                log.info("User is success order - Success message is displayed");
            } else {
                log.warn("User is failed order - Success message is not displayed");
            }
            return isLoggedIn;
        } catch (Exception e) {
            log.error("Failed to verify orders: {}", e.getMessage());
            return false;
        }
    }
}
