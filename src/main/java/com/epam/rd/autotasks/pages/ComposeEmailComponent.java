package com.epam.rd.autotasks.pages;

import com.epam.rd.autotasks.models.EmailMessage;
import com.epam.rd.autotasks.utils.JsExecutorHelper;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ComposeEmailComponent extends BasePage {

    @FindBy(xpath = "//label[contains(text(), '\u0422\u0435\u043c\u0430')]//following::input[1] | //input[@name='subject'] | //input[contains(@class, 'subject')]")
    private WebElement subjectInput;

    @FindBy(id = "compose-to")
    private WebElement recipientInput;

    @FindBy(css = "div.compose button.close, button[aria-label='Close']")
    private WebElement closeBtn;

    @FindBy(xpath = "//div[contains(@class, 'compose')]//button[@type='submit' or @data-name='send']")
    private WebElement sendButton;

    @FindBy(xpath = "//div[contains(@class, 'compose')]")
    private WebElement composeWindow;

    @FindBy(xpath = "//iframe[contains(@id, 'mce') or contains(@class, 'tox')]")
    private WebElement bodyIframe;

    @FindBy(css = "div[contenteditable='true']")
    private WebElement fallbackBody;

    private final RichTextEditor editor;
    private final JsExecutorHelper jsHelper;

    public ComposeEmailComponent(WebDriver driver) {
        super(driver);
        this.editor = new RichTextEditor(driver, wait);
        this.jsHelper = new JsExecutorHelper(driver);
    }

    public void fillEmailDetails(EmailMessage message) {
        logAction("Filling email details. Recipient: {}, Subject: {}", message.getTo(), message.getSubject());

        wait.until(ExpectedConditions.elementToBeClickable(recipientInput));
        recipientInput.clear();
        recipientInput.sendKeys(message.getTo(), Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(subjectInput));
        subjectInput.clear();
        subjectInput.sendKeys(message.getSubject());

        editor.setBody(message.getBody(), bodyIframe, fallbackBody);
    }

    public void saveAsDraft() {
        logAction("Saving email as draft and closing compose window");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(closeBtn));
            clickWithJs(closeBtn);
        } catch (Exception e) {
            logger.warn("Failed to click close button", e);
        }
        waitForWindowToClose();
    }

    public void sendEmail() {
        logAction("Sending email");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(sendButton));
            clickWithJs(sendButton);
        } catch (Exception e) {
            logger.warn("Standard send button click failed, executing JS fallback");
            jsHelper.clickSendButtonFallback();
        }
        waitForWindowToClose();
        driver.navigate().refresh();
    }

    private void waitForWindowToClose() {
        try {
            wait.until(ExpectedConditions.invisibilityOf(composeWindow));
        } catch (TimeoutException e) {
            logger.warn("Compose window did not disappear in time", e);
        }
    }

    public String getRecipientsValue() {
        return jsHelper.getRecipientFromDom();
    }

    public String getSubjectValue() {
        wait.until(ExpectedConditions.visibilityOf(subjectInput));
        return subjectInput.getAttribute("value");
    }

    public String getBodyValue() {
        return editor.getBody();
    }
}
