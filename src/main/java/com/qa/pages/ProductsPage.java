package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage extends BaseTest {
    TestUtils utils = new TestUtils();

    public ProductsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    @AndroidFindBy(accessibility = "test-Menu")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement menu;

    @AndroidFindBy(accessibility = "test-Cart")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement cart;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup/android.widget.TextView")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement cartItems;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"PRODUCTS\"]")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement productTitle;

    @AndroidFindBy(accessibility = "test-Drag Handle")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement dragHandle;

    @AndroidFindBy(accessibility = "test-Cart drop zone")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement dropZone;

    private static final String ANDROID_PRODUCT_NAME_XPATH = "(//android.widget.TextView[@content-desc='test-Item title'])[%d]";
    private static final String ANDROID_PRODUCT_PRICE_XPATH = "(//android.widget.TextView[@content-desc='test-Price'])[%d]";
    private static final String ANDROID_PRODUCT_ADD_BTN_XPATH = "(//android.view.ViewGroup[@content-desc='test-ADD TO CART'])[%d]";

    public SettingsPage clickSettingsBtn() {
        click(menu, "Press settings button");
        return new SettingsPage();
    }

    public CartPage clickCartBtn() {
        click(cart, "Press cart button");
        return new CartPage();
    }

    public String getTotalItemsInCart(){
        return getText(cartItems, "Total items in cart is - ");
    }

    public String getTitle() {
        return getText(productTitle, "Products page title is - ");
    }

    public String getProductName(int index) {
        String xpath = utils.buildDynamicXpath(ANDROID_PRODUCT_NAME_XPATH, index);
        WebElement productName = waitForElement(By.xpath(xpath));
        return getText(productName, "Product name for product " + index + " is - ");
    }

    public String getProductPrice(int index) {
        String xpath = utils.buildDynamicXpath(ANDROID_PRODUCT_PRICE_XPATH, index);
        WebElement productPrice = waitForElement(By.xpath(xpath));
        return getText(productPrice, "Product price for product " + index + " is - ");
    }

    public ProductsPage clickAddToCart(int index) {
        String xpath = utils.buildDynamicXpath(ANDROID_PRODUCT_ADD_BTN_XPATH, index);
        WebElement productAddBtn = waitForElement(By.xpath(xpath));
        click(productAddBtn, "Press add to cart button for product");
        return this;
    }

    public ProductDetailPage clickProductTitle(int index) {
        String xpath = utils.buildDynamicXpath(ANDROID_PRODUCT_NAME_XPATH, index);
        WebElement productTitle = waitForElement(By.xpath(xpath));
        click(productTitle, "Press product name for product " + index);
        return new ProductDetailPage();
    }

    public ProductsPage dragProductToCart() {;
        dragAndDropElement(dragHandle, dropZone);
        return this;
    }
}