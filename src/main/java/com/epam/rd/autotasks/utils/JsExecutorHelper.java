package com.epam.rd.autotasks.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JsExecutorHelper {
    private final JavascriptExecutor js;

    public JsExecutorHelper(WebDriver driver) {
        this.js = (JavascriptExecutor) driver;
    }

    public String getRecipientFromDom() {
        String recipientValue = (String) js.executeScript(
                "let input = document.getElementById('compose-to');" +
                        "if (input && input.value && input.value.trim() !== '') return input.value.trim();" +
                        "let chips = document.querySelectorAll('.compose__field .token, .recipient-name, [class*=\"recipient\"], [class*=\"token\"]');" +
                        "for (let chip of chips) {" +
                        "    let text = chip.textContent ? chip.textContent.trim() : '';" +
                        "    if (text.includes('@')) return text;" +
                        "}" +
                        "let elements = document.querySelectorAll('body *');" +
                        "for (let el of elements) {" +
                        "    let text = el.textContent ? el.textContent.trim() : '';" +
                        "    if (text.includes('@') && text.length < 50 && !text.includes(' ') && el.children.length === 0) {" +
                        "        return text;" +
                        "    }" +
                        "}" +
                        "return '';"
        );
        return recipientValue != null ? recipientValue.trim() : "";
    }

    public void clickSendButtonFallback() {
        js.executeScript(
                "let buttons = Array.from(document.querySelectorAll('button'));" +
                        "let sendBtn = buttons.find(b => b.textContent.includes('\u041d\u0430\u0434\u0456\u0441\u043b\u0430\u0442\u0438') || b.getAttribute('data-name') === 'send' || b.className.includes('send') || b.className.includes('primary') || b.type === 'submit');" +
                        "if (sendBtn) { sendBtn.click(); }" +
                        "else { throw new Error('Send button not found via JS fallback'); }"
        );
    }
}
