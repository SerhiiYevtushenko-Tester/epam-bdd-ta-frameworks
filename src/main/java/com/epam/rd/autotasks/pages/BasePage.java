package com.epam.rd.autotasks.pages;

import com.epam.rd.autotasks.utils.ConfigProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;

        String timeoutStr = ConfigProvider.getInstance().getProperty("timeout.seconds");
        long timeout = timeoutStr != null ? Long.parseLong(timeoutStr) : 10;

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(driver, this);
    }

    protected void logAction(String message, Object... params) {
        logger.log(Level.getLevel("ACTION"), message, params);
    }

    protected void clickWithJs(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Standard click intercepted, falling back to JS click for element: {}", element, e);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
    }
}
