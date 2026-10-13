package com.epam.rd.autotasks.pages;

import com.epam.rd.autotasks.models.User;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    @FindBy(css = "input[name='login']")
    private WebElement loginInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//input[@name='login' or @name='username'] | //form//input[1]")
    private WebElement loginScreenIndicator;

    @FindBy(xpath = "//button[contains(text(), 'Пропустити') or contains(text(), 'Пізніше')] | //a[contains(text(), 'Пропустити')]")
    private WebElement skipPasskeyButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public InboxPage login(User user) {
        logAction("Attempting to log in with user: {}", user.getEmail());

        wait.until(ExpectedConditions.visibilityOf(loginInput));
        loginInput.clear();
        loginInput.sendKeys(user.getEmail());
        logger.debug("Login input filled");

        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(user.getPassword());
        logger.debug("Password input filled");

        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        logAction("Submit button clicked, checking for optional Passkey overlay");

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            shortWait.until(ExpectedConditions.elementToBeClickable(skipPasskeyButton));
            skipPasskeyButton.click();
            logger.info("Dismissed optional 'Passkey/Setup' overlay by clicking Skip");
        } catch (TimeoutException e) {
            logger.debug("No 'Passkey/Setup' overlay appeared. Proceeding to Inbox normally.");
        }

        return new InboxPage(driver);
    }

    public boolean isLoginScreenDisplayed() {
        logger.debug("Checking if login screen is displayed");
        try {
            boolean displayed = wait.until(ExpectedConditions.visibilityOf(loginScreenIndicator)).isDisplayed();
            logger.debug("Login screen display status: {}", displayed);
            return displayed;
        } catch (Exception e) {
            logger.debug("Login screen is not displayed", e);
            return false;
        }
    }
}
