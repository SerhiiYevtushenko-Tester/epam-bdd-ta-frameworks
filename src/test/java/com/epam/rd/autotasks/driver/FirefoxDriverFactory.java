package com.epam.rd.autotasks.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxDriverFactory implements DriverFactory {
    @Override
    public WebDriver createDriver() {
        return new FirefoxDriver();
    }
}
