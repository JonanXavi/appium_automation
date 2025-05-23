# Appium Automation
This project uses the Appium framework with Java to perform **end-to-end (E2E)** testing. The goal is to provide a scalable, efficient, and easily maintainable solution for validating functionalities.

## 📋 Requirements
To run the project, the following requirements must be met:
- [Java JDK 21](https://www.oracle.com/es/java/technologies/downloads/)
- [Maven](https://maven.apache.org/)
- [Android Studio](https://developer.android.com/studio)
- [Appium Server](https://github.com/appium/appium)

## ⚙️ Installation
To install the project dependencies, run the following command in the root of the repository:
```bash
mvn clean install
```

> [!IMPORTANT]
> When using Appium Server on a non-default port, it is advisable to update the corresponding server URL in the `config.properties` file.
> ```config
> appiumURL = http://127.0.0.1:4800/
> ```

## 🌎 Environment Configuration
To configure the device that will execute the project, it is necessary to modify the `testng.xml` file.

Example of `testng.xml` emulator:
```xml
<test name="Emulator">
    <listeners>
        <listener class-name = "com.qa.listeners.TestListener"/>
        <listener class-name = "com.qa.listeners.RetryTests"/>
    </listeners>
    <parameter name="emulator" value="true"/>
    <parameter name="platformName" value="Android"/>
    <parameter name="udid" value="emulator-5554"/>
    <parameter name="deviceName" value="Pixel_7a"/>
    <parameter name="systemPort" value="10001"/>
    <parameter name="chromeDriverPort" value="11001"/>
    <packages>
        <package name="com.qa.tests"/>
    </packages>
</test>
```

Example of `testng.xml` android device:
```xml
<test name="Galaxy_A24">
    <listeners>
        <listener class-name = "com.qa.listeners.TestListener"/>
    </listeners>
    <parameter name="emulator" value="false"/>
    <parameter name="platformName" value="Android"/>
    <parameter name="udid" value="ANDROID007"/>
    <parameter name="deviceName" value="Samsung_A24"/>
    <parameter name="systemPort" value="10000"/>
    <parameter name="chromeDriverPort" value="11000"/>
    <packages>
        <package name="com.qa.tests"/>
    </packages>
</test>
```

## 🚀 Running Tests
> [!CAUTION]
> Test execution requires either an active emulator or a physical device connected to the computer with debugging mode enabled.

### From an IDE
- Right-click on the testng.xml file and select Run.

### From the command line
```bash
mvn test -DsuiteXmlFile=testng.xml
```

## 📂 Project Structure
```bash
📁 appium_automation
├── 📁 logs
├── 📁 screenshots
├── 📁 serverlogs
├── 📁 src
│   ├── 📁 main
│   │   ├── 📁 java
│   │   │   └── 📦 com.qa
│   │   │       ├── 📁 listeners
│   │   │       ├── 📁 pages
│   │   │       ├── 📁 reports
│   │   │       ├── 📁 utils
│   │   │       └── 📋 BaseTest
│   │   └── 📁 resources
│   │       ├── 📁 app
│   │       ├── 📁 data
│   │       ├── 📁 strings
│   │       ├── 🔧 config.properties
│   │       └── 🔧 log4js.properties
│   └── 📁 test
│       └── 📁 java
│           └── 📦 com.qa.tests
│── 📁 videos
├── 🚫 .gitignore
├── 🔐 pom.xml
└── 🔐 testng.xml
```

## Author
- [@jonanxavi](https://www.github.com/jonanxavi)

## 🔗 Links
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/jonathan-fernandez-/)
