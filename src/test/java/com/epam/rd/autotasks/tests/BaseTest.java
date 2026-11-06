package com.epam.rd.autotasks.tests;

import com.epam.rd.autotasks.driver.DriverManager;
import com.epam.rd.autotasks.models.User;
import com.epam.rd.autotasks.utils.ConfigProvider;
import com.epam.rd.autotasks.utils.TestDataFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected final TestDataFactory testDataFactory = new TestDataFactory();
    protected User testUser;

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @BeforeClass
    public void setUp() {
        testUser = testDataFactory.createUser();

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