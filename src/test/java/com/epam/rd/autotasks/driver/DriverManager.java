package com.epam.rd.autotasks.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver(String browser) {
        if (DRIVER_THREAD_LOCAL.get() == null) {
            WebDriver driver = WebDriverFactory.createDriver(browser);
            DRIVER_THREAD_LOCAL.set(driver);
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialised - call initDriver() first");
        }
        return driver;
    }

    public static void quitDriver() {
        if (DRIVER_THREAD_LOCAL.get() != null) {
            DRIVER_THREAD_LOCAL.get().quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }
}
