package com.epam.rd.autotasks.listeners;

import com.epam.rd.autotasks.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());

        WebDriver driver = DriverManager.getDriver();

        if (driver != null) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String destination = "target/screenshots/" + result.getName() + "_" + System.currentTimeMillis() + ".png";

            try {
                Path destPath = Paths.get(destination);
                Files.createDirectories(destPath.getParent());
                Files.copy(source.toPath(), destPath);
                logger.error("Screenshot saved to: {}", destination);
            } catch (IOException e) {
                logger.error("Failed to save screenshot", e);
            }
        }
    }
}
