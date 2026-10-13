package com.epam.rd.autotasks.tests;

import com.epam.rd.autotasks.driver.DriverManager;
import com.epam.rd.autotasks.utils.ConfigProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected String email;
    protected String password;
    protected String recipient;

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @BeforeClass
    public void setUp() {
        email = ConfigProvider.getInstance().getRequiredProperty("mail.email");
        password = ConfigProvider.getInstance().getRequiredProperty("mail.password");
        recipient = ConfigProvider.getInstance().getProperty("test.recipient");

        if (recipient == null || recipient.trim().isEmpty()) {
            recipient = ConfigProvider.getInstance().getRequiredProperty("mail.recipient");
        }

        String browser = System.getProperty("browser", "chrome").toLowerCase();
        String env = System.getProperty("env", "qa").toLowerCase();
        String url = ConfigProvider.getInstance().getRequiredProperty("base.url");

        logger.info("Initializing Test Run");
        logger.info("Environment: {}", env.toUpperCase());
        logger.info("Browser: {}", browser.toUpperCase());
        logger.info("Target URL: {}", url);

        DriverManager.initDriver(browser);
        getDriver().get(url);
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
