package com.epam.rd.autotasks.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RichTextEditor {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public RichTextEditor(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    public void setBody(String body, WebElement bodyIframe, WebElement fallbackBody) {
        wait.until(driverInstance -> js.executeScript("return typeof tinymce !== 'undefined'").equals(true));

        Boolean isTinymceSet = (Boolean) js.executeScript(
                "if (typeof tinymce !== 'undefined' && tinymce.activeEditor) {" +
                        "    tinymce.activeEditor.setContent(arguments[0]);" +
                        "    tinymce.activeEditor.save();" +
                        "    return true;" +
                        "} return false;", body);

        if (Boolean.TRUE.equals(isTinymceSet)) return;

        try {
            wait.until(ExpectedConditions.visibilityOf(bodyIframe));
            driver.switchTo().frame(bodyIframe);
            js.executeScript("document.body.innerHTML = '<p>' + arguments[0] + '</p>';", body);
        } catch (TimeoutException e) {
            fallbackBody.sendKeys(body);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    public String getBody() {
        String content = (String) js.executeScript(
                "if (typeof tinymce !== 'undefined' && tinymce.activeEditor) {" +
                        "    return tinymce.activeEditor.getContent({format : 'text'});" +
                        "}" +
                        "let editor = document.querySelector('div[contenteditable=\"true\"]');" +
                        "return editor ? editor.innerText : '';"
        );
        return content != null ? content.trim() : "";
    }
}
