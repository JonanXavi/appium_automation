package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ProductDetailPage extends BaseTest {
    public ProductDetailPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement productName;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement productDetail;

    @AndroidFindBy(accessibility = "test-Price")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement productPrice;

    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement backToProductsBtn;

    @AndroidFindBy(accessibility = "test-ADD TO CART")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement addToCartBtn;

    public String getProductName() {
        return getText(productName, "Product name is - ");
    }

    public String getProductDetail() {
        return  getText(productDetail, "Product detail is - ");
    }

    public String getProductPrice() {
        return getText(productPrice, "Product price is - ");
    }

    public ProductsPage clickBackToProductsBtn() {
        click(backToProductsBtn, "Navigate back to products page");
        return new ProductsPage();
    }

    public ProductDetailPage clickAddToCartBtn() {
        click(addToCartBtn, "Add product to the cart");
        return this;
    }
}
