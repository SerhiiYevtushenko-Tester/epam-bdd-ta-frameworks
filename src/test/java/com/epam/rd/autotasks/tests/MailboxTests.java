package com.epam.rd.autotasks.tests;

import com.epam.rd.autotasks.models.EmailMessage;
import com.epam.rd.autotasks.models.User;
import com.epam.rd.autotasks.pages.*;
import com.epam.rd.autotasks.utils.ConfigProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MailboxTests extends BaseTest {
    private static final Logger logger = LogManager.getLogger(MailboxTests.class);

    private InboxPage inboxPage;
    private DraftsPage draftsPage;
    private SentPage sentPage;
    private ComposeEmailComponent composeMail;

    private User testUser;
    private EmailMessage testMessage;

    private final String uniqueId = String.valueOf(System.currentTimeMillis());
    private final String subject = ConfigProvider.getInstance().getRequiredProperty("test.email.subject") + " " + uniqueId;
    private final String body = ConfigProvider.getInstance().getRequiredProperty("test.email.body");

    @Test(priority = 1)
    public void testCreateDraft() {
        logger.info("Starting testCreateDraft with subject: {}", subject);
        testUser = new User(email, password);

        testMessage = EmailMessage.builder()
                .to(recipient)
                .subject(subject)
                .body(body)
                .build();

        LoginPage loginPage = new LoginPage(getDriver());
        inboxPage = loginPage.login(testUser);

        Assert.assertTrue(inboxPage.isLoginSuccessful(), "Login failed!");

        composeMail = inboxPage.clickCompose();
        composeMail.fillEmailDetails(testMessage);
        composeMail.saveAsDraft();

        draftsPage = inboxPage.goToDrafts();
        Assert.assertTrue(draftsPage.containsMail(subject), "Draft was not found in Drafts folder!");

        composeMail = draftsPage.openDraft(subject);

        Assert.assertEquals(composeMail.getRecipientsValue(), testMessage.getTo(), "Draft recipient does not match!");
        Assert.assertEquals(composeMail.getSubjectValue(), testMessage.getSubject(), "Draft subject does not match!");
        Assert.assertEquals(composeMail.getBodyValue(), testMessage.getBody(), "Draft body does not match!");

        composeMail.saveAsDraft();
        logger.info("testCreateDraft completed successfully");
    }

    @Test(priority = 2, dependsOnMethods = "testCreateDraft")
    public void testSendDraft() {
        logger.info("Starting testSendDraft for subject: {}", subject);
        draftsPage = inboxPage.goToDrafts();
        composeMail = draftsPage.openDraft(subject);

        Assert.assertEquals(composeMail.getSubjectValue(), testMessage.getSubject(), "Subject mismatch before sending!");

        composeMail.sendEmail();

        draftsPage = inboxPage.goToDrafts();
        Assert.assertFalse(draftsPage.containsMail(subject), "Mail is still present in Drafts after sending!");
        logger.info("testSendDraft completed successfully");
    }

    @Test(priority = 3, dependsOnMethods = "testSendDraft")
    public void testVerifySentAndLogout() {
        logger.info("Starting testVerifySentAndLogout");
        sentPage = inboxPage.goToSent();
        Assert.assertTrue(sentPage.containsMail(subject), "Mail is not present in Sent folder!");

        LoginPage loginPage = inboxPage.logout();
        Assert.assertTrue(loginPage.isLoginScreenDisplayed(), "Logout was not successful, login form is not visible!");
        logger.info("testVerifySentAndLogout completed successfully");
    }
}
