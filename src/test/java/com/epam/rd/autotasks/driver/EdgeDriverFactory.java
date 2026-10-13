package com.epam.rd.autotasks.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class EdgeDriverFactory implements DriverFactory {
    @Override
    public WebDriver createDriver() {
        return new EdgeDriver();
    }
}
