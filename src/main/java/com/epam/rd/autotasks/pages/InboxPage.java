package com.epam.rd.autotasks.pages;

import com.epam.rd.autotasks.utils.ConfigProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InboxPage extends BasePage {

    @FindBy(xpath = "//a[contains(@href, 'inbox')] | //span[contains(text(), 'Вхідні')] | //button[normalize-space()='Написати листа']")
    private WebElement loginSuccessIndicator;

    @FindBy(xpath = "//button[normalize-space()='Написати листа'] | //a[normalize-space()='Написати листа']")
    private WebElement composeBtn;

    @FindBy(css = "a[href*='draft']")
    private WebElement draftsFolder;

    @FindBy(css = "a[href*='sent']")
    private WebElement sentFolder;

    public InboxPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginSuccessful() {
        logger.debug("Checking if login was successful");
        try {
            boolean displayed = wait.until(ExpectedConditions.visibilityOf(loginSuccessIndicator)).isDisplayed();
            logger.debug("Login success status check: {}", displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Login success elements not found", e);
            return false;
        }
    }

    public ComposeEmailComponent clickCompose() {
        logAction("Clicking compose button");
        wait.until(ExpectedConditions.elementToBeClickable(composeBtn));
        clickWithJs(composeBtn);
        return new ComposeEmailComponent(driver);
    }

    public DraftsPage goToDrafts() {
        logAction("Navigating to Drafts folder");
        wait.until(ExpectedConditions.elementToBeClickable(draftsFolder));
        clickWithJs(draftsFolder);
        return new DraftsPage(driver);
    }

    public SentPage goToSent() {
        logAction("Navigating to Sent folder");
        wait.until(ExpectedConditions.elementToBeClickable(sentFolder));
        clickWithJs(sentFolder);
        return new SentPage(driver);
    }

    public LoginPage logout() {
        logAction("Performing user logout");
        driver.manage().deleteAllCookies();
        driver.get(ConfigProvider.getInstance().getProperty("base.url"));
        return new LoginPage(driver);
    }
}
