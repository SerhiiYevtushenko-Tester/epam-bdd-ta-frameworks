package com.epam.rd.autotasks.tests;

import com.epam.rd.autotasks.models.EmailMessage;
import com.epam.rd.autotasks.pages.*;
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

    private EmailMessage testMessage;

    @Test(priority = 1)
    public void testCreateDraft() {
        testMessage = testDataFactory.createEmailMessage();

        logger.info("Starting testCreateDraft with subject: {}", testMessage.getSubject());

        LoginPage loginPage = new LoginPage(getDriver());
        inboxPage = loginPage.login(testUser);

        Assert.assertTrue(inboxPage.isLoginSuccessful(), "Login failed!");

        composeMail = inboxPage.clickCompose();
        composeMail.fillEmailDetails(testMessage);
        composeMail.saveAsDraft();

        draftsPage = inboxPage.goToDrafts();
        Assert.assertTrue(
                draftsPage.containsMail(testMessage.getSubject()),
                "Draft was not found in Drafts folder!"
        );

        composeMail = draftsPage.openDraft(testMessage.getSubject());

        Assert.assertEquals(
                composeMail.getRecipientsValue(),
                testMessage.getTo(),
                "Draft recipient does not match!"
        );
        Assert.assertEquals(
                composeMail.getSubjectValue(),
                testMessage.getSubject(),
                "Draft subject does not match!"
        );
        Assert.assertEquals(
                composeMail.getBodyValue(),
                testMessage.getBody(),
                "Draft body does not match!"
        );

        composeMail.saveAsDraft();
        logger.info("testCreateDraft completed successfully");
    }

    @Test(priority = 2, dependsOnMethods = "testCreateDraft")
    public void testSendDraft() {
        logger.info("Starting testSendDraft for subject: {}", testMessage.getSubject());

        draftsPage = inboxPage.goToDrafts();
        composeMail = draftsPage.openDraft(testMessage.getSubject());

        Assert.assertEquals(
                composeMail.getSubjectValue(),
                testMessage.getSubject(),
                "Subject mismatch before sending!"
        );

        composeMail.sendEmail();

        draftsPage = inboxPage.goToDrafts();
        Assert.assertFalse(
                draftsPage.containsMail(testMessage.getSubject()),
                "Mail is still present in Drafts after sending!"
        );

        logger.info("testSendDraft completed successfully");
    }

    @Test(priority = 3, dependsOnMethods = "testSendDraft")
    public void testVerifySentAndLogout() {
        logger.info("Starting testVerifySentAndLogout");

        sentPage = inboxPage.goToSent();
        Assert.assertTrue(
                sentPage.containsMail(testMessage.getSubject()),
                "Mail is not present in Sent folder!"
        );

        LoginPage loginPage = inboxPage.logout();
        Assert.assertTrue(
                loginPage.isLoginScreenDisplayed(),
                "Logout was not successful, login form is not visible!"
        );

        logger.info("testVerifySentAndLogout completed successfully");
    }
}