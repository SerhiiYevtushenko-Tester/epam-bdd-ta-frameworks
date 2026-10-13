package com.epam.rd.autotasks.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WebDriverFactory {

    private static final Map<String, Supplier<DriverFactory>> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("chrome", ChromeDriverFactory::new);
        FACTORIES.put("firefox", FirefoxDriverFactory::new);
        FACTORIES.put("edge", EdgeDriverFactory::new);
    }

    private WebDriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        Supplier<DriverFactory> factorySupplier = FACTORIES.get(browser.toLowerCase());

        if (factorySupplier == null) {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        WebDriver originalDriver = factorySupplier.get().createDriver();
        originalDriver.manage().window().maximize();

        LoggingWebDriverListener listener = new LoggingWebDriverListener();
        return new EventFiringDecorator<>(listener).decorate(originalDriver);
    }
}
