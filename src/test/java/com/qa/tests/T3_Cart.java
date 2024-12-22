package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.CartPage;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

public class T3_Cart extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    ProductDetailPage productDetailPage;
    CartPage cartPage;
    JSONObject userData;
    JSONObject productData;
    TestUtils utils = new TestUtils();

    @BeforeClass
    public void beforeClass() {
        userData = TestUtils.loadJsonData("data/userData.json");
        productData = TestUtils.loadJsonData("data/productData.json");
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
    public void addProductsFromPDPToCart() throws InterruptedException {
        productsPage = loginPage.login(userData.getJSONObject("validUser").getString("username"),
                userData.getJSONObject("validUser").getString("password"));

        SoftAssert softAssert = new SoftAssert();

        for (int i = 1; i <= 2; i++) {
            productDetailPage = productsPage.clickProductTitle(i);
            scrollToElement("text", "ADD TO CART");
            productDetailPage.clickAddToCartBtn();
            switch (i) {
                case 1:
                    productsPage = productDetailPage.clickBackToProductsBtn();
                    break;
                case 2:
                    cartPage = productsPage.clickCartBtn();
                    break;
            }
        }
        for (int i = 1; i <= 2; i++) {
            String productName = cartPage.getProductName(i);
            softAssert.assertEquals(productName, productData.getJSONObject("product_" + i).getString("name"));
        }

        softAssert.assertAll();
    }

    @Test
    public void addProductsWithDragToCart() {
        productsPage = loginPage.login(userData.getJSONObject("validUser").getString("username"),
                userData.getJSONObject("validUser").getString("password"));

        for (int i = 1; i <= 2; i++) {
            productsPage.dragProductToCart();
            if (i == 2) {
                String actualItemCount = productsPage.getTotalItemsInCart();
                String expectedItemCount = String.valueOf(i);
                Assert.assertEquals(actualItemCount, expectedItemCount);
            }
        }
    }

    @Test
    public void clearShoppingCart() {
        productsPage = loginPage.login(userData.getJSONObject("validUser").getString("username"),
                userData.getJSONObject("validUser").getString("password"));

        for (int i = 1; i <= 2; i++) {
            productsPage.clickAddToCart(1);
        }
        cartPage = productsPage.clickCartBtn();
        for (int i = 1; i <= 2; i++) {
            cartPage.swipeProduct();
            cartPage.removeProduct();
        }
        boolean cartIsEmpty = cartPage.isCartEmpty();
        Assert.assertTrue(cartIsEmpty);
    }
}
