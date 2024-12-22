package com.qa.pages;

import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class CartPage extends BaseTest {
    TestUtils utils = new TestUtils();

    public CartPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    @AndroidFindBy(xpath = "(//android.view.ViewGroup[@content-desc=\"test-Item\"])[1]")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement product;

    @AndroidFindBy(accessibility = "test-Delete")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement removeProductBtn;

    @AndroidFindBy(accessibility = "test-Cart Content")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement cartContent;

    private static final String ANDROID_PRODUCT_NAME_XPATH = "(//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1])[%d]";

    public String getProductName(int index) throws InterruptedException {
        Thread.sleep(1000);
        String xpath = utils.buildDynamicXpath(ANDROID_PRODUCT_NAME_XPATH, index);
        WebElement productName = waitForElement(By.xpath(xpath));
        return getText(productName, "Product name for product " + index + " is - ");
    }

    public CartPage swipeProduct() {
        swipeElement(product, "left");
        return this;
    }

    public CartPage removeProduct() {
        click(removeProductBtn, "Removing product from cart");
        return this;
    }

    public boolean isCartEmpty() {
        ExtentReport.getTest().log(Status.INFO, "Getting cart information");
        return cartContent.findElements(By.xpath(".//*[@content-desc='test-Item']")).isEmpty();
    }
}