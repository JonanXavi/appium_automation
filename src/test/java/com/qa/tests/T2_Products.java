package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

public class T2_Products extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    ProductDetailPage productDetailPage;
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
    public void validateProductsOnProductListPage() {
        productsPage = loginPage.login(userData.getJSONObject("validUser").getString("username"),
                userData.getJSONObject("validUser").getString("password"));

        SoftAssert softAssert = new SoftAssert();

        for (int i = 1; i <= 2; i++) {
            String productName = productsPage.getProductName(i);
            softAssert.assertEquals(productName, productData.getJSONObject("product_" + i).getString("name"));

            String productPrice = productsPage.getProductPrice(i);
            softAssert.assertEquals(productPrice, productData.getJSONObject("product_" + i).getString("price"));
        }
        softAssert.assertAll();
    }

    @Test
    public void validateProductOnProductDetailPage() {
        productsPage = loginPage.login(userData.getJSONObject("validUser").getString("username"),
                userData.getJSONObject("validUser").getString("password"));

        SoftAssert softAssert = new SoftAssert();

        int index = 2;
        productDetailPage = productsPage.clickProductTitle(index);
        String productName = productDetailPage.getProductName();
        softAssert.assertEquals(productName, productData.getJSONObject("product_" + index).getString("name"));
        String productDetail = productDetailPage.getProductDetail();
        softAssert.assertEquals(productDetail, productData.getJSONObject("product_" + index).getString("description"));
        String productPrice = productDetailPage.getProductPrice();
        softAssert.assertEquals(productPrice, productData.getJSONObject("product_" + index).getString("price"));

        softAssert.assertAll();
    }
}
