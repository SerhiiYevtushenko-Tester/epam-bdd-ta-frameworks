package com.epam.rd.autotasks.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DraftsPage extends MailFolderPage {

    public DraftsPage(WebDriver driver) {
        super(driver);
    }

    public ComposeEmailComponent openDraft(String subject) {
        logAction("Opening draft with subject: {}", subject);

        driver.navigate().refresh();

        String safeSubject = subject.replace("'", "', \"'\", '");
        WebElement subjectElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), concat('', '" + safeSubject + "'))]")
        ));

        clickWithJs(subjectElement);
        return new ComposeEmailComponent(driver);
    }
}
