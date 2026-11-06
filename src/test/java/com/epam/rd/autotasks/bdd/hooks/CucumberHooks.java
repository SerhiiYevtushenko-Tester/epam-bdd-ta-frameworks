package com.epam.rd.autotasks.bdd.hooks;

import com.epam.rd.autotasks.driver.DriverManager;
import com.epam.rd.autotasks.utils.ConfigProvider;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public class CucumberHooks {

    private static final Logger logger = LogManager.getLogger(CucumberHooks.class);

    @Before
    public void setUp(Scenario scenario) {
        String browser = resolveBrowser(scenario);
        String url = ConfigProvider.getInstance().getRequiredProperty("base.url");

        logger.info("Initializing browser for scenario: {}", scenario.getName());
        logger.info("Browser: {}", browser.toUpperCase());
        logger.info("Target URL: {}", url);

        DriverManager.initDriver(browser);
        DriverManager.getDriver().get(url);
    }

    @After
    public void tearDown() {
        logger.info("Closing browser after Cucumber scenario");
        DriverManager.quitDriver();
    }

    private String resolveBrowser(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();

        if (tags.contains("@browser_firefox")) {
            return "firefox";
        }

        if (tags.contains("@browser_chrome")) {
            return "chrome";
        }

        return System.getProperty("browser", "chrome").toLowerCase();
    }
}
