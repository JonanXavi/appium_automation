package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class T1_Login extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    JSONObject userData;
    TestUtils utils = new TestUtils();

    @BeforeClass
    public void beforeClass() {
        userData = TestUtils.loadJsonData("data/userData.json");
    }

    @BeforeMethod
    public void beforeMethod(Method m) {
        utils.log().info("\n" + "****** starting test: " + m.getName() + " ******" + "\n");
        loginPage = new LoginPage();
    }

    @AfterMethod
    public void afterMethod() {
        closeApp();
        launchApp();
    }

    @Test
    public void mandatoryPassword() {
        loginPage.enterUserName(userData.getJSONObject("validUser").getString("username"));
        loginPage.clickLoginBtn();

        String actualError = loginPage.getErrorMessage();
        String expectedError = getStrings().get("err_password");
        Assert.assertEquals(actualError, expectedError);
    }

    @Test
    public void mandatoryUsername() {
        loginPage.enterPassword(userData.getJSONObject("validUser").getString("password"));
        loginPage.clickLoginBtn();

        String actualError = loginPage.getErrorMessage();
        String expectedError = getStrings().get("err_username");
        Assert.assertEquals(actualError, expectedError);
    }

    @Test
    public void invalidCredentials() {
        loginPage.enterUserName(userData.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPassword(userData.getJSONObject("invalidUser").getString("password"));
        loginPage.clickLoginBtn();

        String actualError = loginPage.getErrorMessage();
        String expectedError = getStrings().get("err_credentials");
        Assert.assertEquals(actualError, expectedError);
    }

    @Test
    public void lockedAccount() {
        loginPage.enterUserName(userData.getJSONObject("lockedUser").getString("username"));
        loginPage.enterPassword(userData.getJSONObject("lockedUser").getString("password"));
        loginPage.clickLoginBtn();

        String actualError = loginPage.getErrorMessage();
        String expectedError = getStrings().get("err_locked");
        Assert.assertEquals(actualError, expectedError);
    }

    @Test
    public void successfulLogin() {
        loginPage.enterUserName(userData.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(userData.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.clickLoginBtn();

        String actualProductTitle = productsPage.getTitle();
        String expectedProductTitle = getStrings().get("product_title");
        Assert.assertEquals(actualProductTitle, expectedProductTitle);
    }

    @Test
    public void successfulLogout() {
        productsPage = loginPage.login(userData.getJSONObject("validUser").getString("username"),
                userData.getJSONObject("validUser").getString("password"));
        settingsPage = productsPage.clickSettingsBtn();
        loginPage = settingsPage.clickLogoutBtn();

        boolean logoVisible = loginPage.isLogoVisible();
        Assert.assertTrue(logoVisible);
    }
}
