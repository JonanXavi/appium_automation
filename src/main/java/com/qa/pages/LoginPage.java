package com.qa.pages;

import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BaseTest {
    public LoginPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement userInput;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement passwordInput;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement errorMsg;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement loginBtn;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(0)")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement logoApp;

    public LoginPage enterUserName(String username) {
        sendKeys(userInput, username, "Login with " + username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendKeys(passwordInput, password, "Password is " + password);
        return this;
    }

    public ProductsPage clickLoginBtn() {
        click(loginBtn, "Press login button");
        return new ProductsPage();
    }

    public ProductsPage login(String username, String password) {
        enterUserName(username);
        enterPassword(password);
        return clickLoginBtn();
    }

    public String getErrorMessage() {
        return getText(errorMsg, "Get error text - ");
    }

    public boolean isLogoVisible() {
        ExtentReport.getTest().log(Status.INFO, "Get logo visibility");
        return logoApp.isDisplayed();
    }
}