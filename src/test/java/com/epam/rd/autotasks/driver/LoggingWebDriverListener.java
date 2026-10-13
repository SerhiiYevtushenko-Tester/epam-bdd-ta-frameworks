package com.epam.rd.autotasks.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class LoggingWebDriverListener implements WebDriverListener {
    private static final Logger logger = LogManager.getLogger(LoggingWebDriverListener.class);

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        logger.debug("Searching for element with locator: {}", locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        logger.debug("Element found successfully: {}", locator);
    }

    @Override
    public void beforeClick(WebElement element) {
        logger.info("Clicking on element: {}", element.toString());
    }

    @Override
    public void afterQuit(WebDriver driver) {
        logger.info("WebDriver successfully quit.");
    }
}
