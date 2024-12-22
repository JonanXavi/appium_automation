package com.qa;

import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableMap;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {
    protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    protected static ThreadLocal <Properties> properties = new ThreadLocal<Properties>();
    protected static ThreadLocal <HashMap<String, String>> strings = new ThreadLocal <HashMap<String, String>>();
    protected static ThreadLocal <String> platform = new ThreadLocal<String>();
    protected static ThreadLocal <String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal <String> deviceName = new ThreadLocal<String>();
    private static AppiumDriverLocalService server;
    TestUtils utils = new TestUtils();

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    public Properties getProperties() {
        return properties.get();
    }

    public void setProperties(Properties properties2) {
        properties.set(properties2);
    }

    public HashMap<String, String> getStrings() {
        return strings.get();
    }

    public void setStrings(HashMap<String, String> strings2) {
        strings.set(strings2);
    }

    public String getPlatform() {
        return platform.get();
    }

    public void setPlatform(String platform2) {
        platform.set(platform2);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public void setDateTime(String dateTime2) {
        dateTime.set(dateTime2);
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }

    @BeforeMethod
    public void beforeMethod() {
        ((CanRecordScreen) getDriver()).startRecordingScreen();
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result) {
        String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();

        if (result.getStatus() == ITestResult.FAILURE) {
            Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();

            String dir = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName")
                    + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();

            File videoDir = new File(dir);

            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }

            try (FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4")) {
                stream.write(Base64.getDecoder().decode(media));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeSuite
    public void beforeSuite() throws Exception {
        ThreadContext.put("ROUTINGKEY", "serverlogs");
        try {
            if (server == null || !checkIfAppiumServerIsRunnning(4723)) {
                server = getAppiumServerDefault();
                //server = getAppiumService(); // Se usa cuando se hace la configuración manual de appium server
                server.start();
                utils.log().info("Appium server started");
            } else {
                utils.log().info("Appium server already running");
            }
        } catch (Exception e) {
            utils.log().error("Error while starting appium server: " + e.getMessage(), e);
            throw new RuntimeException("Error while starting appium server", e);
        }
    }

    @AfterSuite
    public void afterSuite() {
        driver.remove();
        properties.remove();
        strings.remove();
        platform.remove();
        dateTime.remove();
        deviceName.remove();

        try {
            if (server != null && server.isRunning()) {
                server.stop();
                utils.log().info("Appium server stopped");
            }
        } catch (Exception e) {
            utils.log().error("Error while stopping Appium server: " + e.getMessage(), e);
        }
    }

    @Parameters({"emulator", "platformName", "udid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeTest
    public void beforeTest(@Optional("androidOnly")String emulator, String platformName, String udid, String deviceName,
                           @Optional("androidOnly")String systemPort, @Optional("androidOnly")String chromeDriverPort,
                           @Optional("iOSOnly")String wdaLocalPort, @Optional("iOSOnly")String webkitDebugProxyPort) throws Exception {
        setDateTime(utils.dataTime());
        setPlatform(platformName);
        setDeviceName(deviceName);
        URL url;
        Properties properties;
        AppiumDriver driver;

        String strFile = "logs" + File.separator + platformName + "_" + deviceName;
        File logFile = new File(strFile);
        if(!logFile.exists()) {
            logFile.mkdirs();
        }
        ThreadContext.put("ROUTINGKEY", strFile);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
             InputStream stringsis = getClass().getClassLoader().getResourceAsStream("strings/strings.xml")) {

            properties = new Properties();
            utils.log().info("Load config.properties file");
            properties.load(inputStream);
            setProperties(properties);
            utils.log().info("Load strings.xml file");
            setStrings(utils.parseStringXML(stringsis));

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("platformName", platformName);
            desiredCapabilities.setCapability("deviceName", deviceName);
            desiredCapabilities.setCapability("udid", udid);

            url = new URL(properties.getProperty("appiumURL"));

            switch (platformName) {
                case "Android":
                    String androidApp = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                            + File.separator + "resources" + File.separator + "app" + File.separator + properties.getProperty("androidApp");

                    desiredCapabilities.setCapability("automationName", properties.getProperty("androidAutomationName"));
                    desiredCapabilities.setCapability("appPackage", properties.getProperty("androidAppPackage"));
                    desiredCapabilities.setCapability("appActivity", properties.getProperty("androidAppActivity"));
                    if(emulator.equalsIgnoreCase("true")) {
                        desiredCapabilities.setCapability("avd", deviceName);
                        desiredCapabilities.setCapability("avdLaunchTimeout", 120000);
                    }
                    desiredCapabilities.setCapability("systemPort", systemPort);
                    desiredCapabilities.setCapability("chromeDriverPort", chromeDriverPort);
                    desiredCapabilities.setCapability("app", androidApp);

                    driver = new AndroidDriver(url, desiredCapabilities);
                    break;

                case "iOS":
                    String iOSApp = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                            + File.separator + "resources" + File.separator + "app" + File.separator + properties.getProperty("iOSApp");

                    desiredCapabilities.setCapability("automationName", properties.getProperty("iOSAutomationName"));
                    desiredCapabilities.setCapability("bundleId", properties.getProperty("iOSBundleId"));
                    desiredCapabilities.setCapability("wdaLocalPort", properties.getProperty("wdaLocalPort"));
                    desiredCapabilities.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);
                    desiredCapabilities.setCapability("app", iOSApp);

                    driver = new IOSDriver(url, desiredCapabilities);
                    break;

                default:
                    throw new Exception("Invalid platform! - " + platformName);
            }
            setDriver(driver);
            utils.log().info("Driver initialized: " + driver);

        } catch (Exception e) {
            utils.log().fatal("Driver initialization failure. ABORT!!!\n" + e.toString());
            throw e;
        }
    }

    @AfterTest
    public void afterTest() {
        getDriver().quit();
    }

    public boolean checkIfAppiumServerIsRunnning(int port) {
        boolean isAppiumServerRunning = false;
        try (ServerSocket socket = new ServerSocket(port)) {
        } catch (IOException e) {
            isAppiumServerRunning = true;
        }
        return isAppiumServerRunning;
    }

    public AppiumDriverLocalService getAppiumServerDefault(){
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<String, String>();
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe")) // Obtener ruta node con CMD en WIN -> where node
                .withAppiumJS(new File("C:\\Users\\Jonathan Fernandez\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js")) // Obtener ruta appium con CMD en WIN -> where appium
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withLogFile(new File("ServerLogs/server.log")));
    }

    public void closeApp() {
        switch(getPlatform()){
            case "Android":
                ((InteractsWithApps) getDriver()).terminateApp(getProperties().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).terminateApp(getProperties().getProperty("iOSBundleId"));
                break;
        }
    }

    public void launchApp() {
        switch(getPlatform()){
            case "Android":
                ((InteractsWithApps) getDriver()).activateApp(getProperties().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).activateApp(getProperties().getProperty("iOSBundleId"));
                break;
        }
    }

    public void waitForVisibility (WebElement e) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public WebElement waitForElement(By locator) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click(WebElement e) {
        waitForVisibility(e);
        e.click();
    }

    public void click(WebElement e, String msg) {
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.click();
    }

    public void sendKeys(WebElement e, String text) {
        waitForVisibility(e);
        e.sendKeys(text);
    }

    public void sendKeys(WebElement e, String text, String msg) {
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.sendKeys(text);
    }

    public void clear(WebElement e) {
        waitForVisibility(e);
        e.clear();
    }

    public void clear(WebElement e, String msg) {
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.clear();
    }

    public String getAttribute(WebElement e, String attribute) {
        waitForVisibility(e);

        return e.getDomAttribute(attribute);
    }

    public String getText(WebElement e, String msg) {
        String text = null;
        switch(getPlatform()) {
            case "Android":
                text = getAttribute(e, "text");
                break;
            case "iOS":
                text = getAttribute(e, "label");
                break;
        }
        utils.log().info(msg + text);
        ExtentReport.getTest().log(Status.INFO, msg + text);
        return text;
    }

    public void scrollToElement(String attribute, String value) {
        String scrollCommand = String.format(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                        "new UiSelector().%s(\"%s\"))", attribute, value);
        waitForElement(AppiumBy.androidUIAutomator(scrollCommand));
    }

    public void swipeElement(WebElement e, String direction) {
        waitForVisibility(e);
        getDriver().executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) e).getId(),
                "direction", direction,
                "percent", 0.75
        ));
    }

    public void dragAndDropElement(WebElement source, WebElement target) {
        waitForVisibility(source);
        waitForVisibility(target);
        Point targetLocation = target.getLocation();
        Dimension targetSize = target.getSize();
        int endX = targetLocation.getX() + (targetSize.getWidth() / 2);
        int endY = targetLocation.getY() + (targetSize.getHeight() / 2);

        getDriver().executeScript("mobile: dragGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) source).getId(),
                "endX", endX,
                "endY", endY,
                "speed", 3500
        ));
    }
}