package org.example.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.assertj.core.api.Assertions;
import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    private static final Logger log = LogManager.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//button[text()='Add to cart']")
    private WebElement addToCartButton;

    @FindBy(xpath = "//a[@class='shopping_cart_link']")
    private WebElement clickCart;

    @FindBy(xpath = "//select[@class='product_sort_container']")
    private WebElement sortProduct;

    @FindBy(xpath = "//*[@id=\"inventory_filter_container\"]/select/option[4]")
    private WebElement sortFromHighToLow;

    @FindBy(xpath = "//*[@id=\"inventory_filter_container\"]/select/option[3]")
    private WebElement sortFromLowToHigh;

    @FindBy(xpath = "//button[@class='btn_secondary btn_inventory']")
    private WebElement removeButton;

    public void addToCart() {
        log.info("Add to cart");
        addToCartButton.click();
        clickCart.click();
    }

    public void verifyOnHomepage() {
        log.info("On Homepage");
        addToCartButton.isDisplayed();
        sortProduct.isDisplayed();
        sortProduct.click();
        sortFromHighToLow.click();
        sortProduct.click();
        sortFromLowToHigh.click();
        addToCartButton.click();
        //removeButton.click();
    }

}
