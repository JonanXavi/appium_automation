package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SettingsPage extends BaseTest {
    public SettingsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    @AndroidFindBy(accessibility = "test-LOGOUT")
    @iOSXCUITFindBy(id = "ios-locator")
    private WebElement logoutOpt;

    public LoginPage clickLogoutBtn() {
        click(logoutOpt, "Press logout option");
        return new LoginPage();
    }
}
