
package com.epam.rd.autotasks.bdd.hooks;

import com.epam.rd.autotasks.driver.DriverManager;
import io.cucumber.java.After;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberHooks {

    private static final Logger logger = LogManager.getLogger(CucumberHooks.class);

    @After
    public void tearDown() {
        logger.info("Closing browser after Cucumber scenario");
        DriverManager.quitDriver();
    }
}
