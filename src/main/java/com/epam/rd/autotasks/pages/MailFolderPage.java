package com.epam.rd.autotasks.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public abstract class MailFolderPage extends BasePage {

    public MailFolderPage(WebDriver driver) {
        super(driver);
    }

    public boolean containsMail(String subject) {
        logAction("Checking presence of email with subject: {}", subject);

        String safeSubject = subject.replace("'", "', \"'\", '");
        String xpath = "//*[contains(text(), concat('', '" + safeSubject + "')) or contains(@title, concat('', '" + safeSubject + "'))]";

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        } catch (Exception e) {
            logger.debug("Email element not immediately found via explicit wait for subject: {}", subject);
        }

        List<WebElement> mails = driver.findElements(By.xpath(xpath));
        boolean isPresent = !mails.isEmpty() && mails.get(0).isDisplayed();
        logger.info("Email presence status for '{}': {}", subject, isPresent);
        return isPresent;
    }
}
