
package com.epam.rd.autotasks.bdd.steps;

import com.epam.rd.autotasks.driver.DriverManager;
import com.epam.rd.autotasks.models.EmailMessage;
import com.epam.rd.autotasks.models.User;
import com.epam.rd.autotasks.pages.ComposeEmailComponent;
import com.epam.rd.autotasks.pages.DraftsPage;
import com.epam.rd.autotasks.pages.InboxPage;
import com.epam.rd.autotasks.pages.LoginPage;
import com.epam.rd.autotasks.pages.SentPage;
import com.epam.rd.autotasks.utils.TestDataFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class MailboxStepDefinitions {

    private LoginPage loginPage;
    private InboxPage inboxPage;
    private DraftsPage draftsPage;
    private SentPage sentPage;
    private ComposeEmailComponent composeMail;

    private final TestDataFactory testDataFactory = new TestDataFactory();

    private User testUser;
    private EmailMessage testMessage;

    private String subject;

    @Given("the user is on the mailbox login page")
    public void userIsOnMailboxLoginPage() {
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @Given("the user is logged in to the mailbox")
    public void userIsLoggedInToMailbox() {
        testUser = testDataFactory.createUser();
        testMessage = testDataFactory.createEmailMessage();
        subject = testMessage.getSubject();

        inboxPage = loginPage.login(testUser);

        Assert.assertTrue(
                inboxPage.isLoginSuccessful(),
                "Login failed."
        );
    }

    @Then("the login page should be displayed")
    public void loginPageShouldBeDisplayed() {
        Assert.assertTrue(
                loginPage.isLoginScreenDisplayed(),
                "Login page is not displayed."
        );
    }

    @When("the user creates a draft email")
    public void userCreatesDraftEmail() {
        composeMail = inboxPage.clickCompose();
        composeMail.fillEmailDetails(testMessage);
        composeMail.saveAsDraft();

        draftsPage = inboxPage.goToDrafts();
    }

    @Then("the draft should contain the correct recipient, subject, and body")
    public void draftShouldContainCorrectDetails() {
        Assert.assertTrue(
                draftsPage.containsMail(subject),
                "Draft was not found in Drafts folder."
        );

        composeMail = draftsPage.openDraft(subject);

        Assert.assertEquals(
                composeMail.getRecipientsValue(),
                testMessage.getTo(),
                "Draft recipient does not match."
        );

        Assert.assertEquals(
                composeMail.getSubjectValue(),
                testMessage.getSubject(),
                "Draft subject does not match."
        );

        Assert.assertEquals(
                composeMail.getBodyValue(),
                testMessage.getBody(),
                "Draft body does not match."
        );

        composeMail.saveAsDraft();
    }

    @When("the user sends the draft email")
    public void userSendsDraftEmail() {
        draftsPage = inboxPage.goToDrafts();
        composeMail = draftsPage.openDraft(subject);
        composeMail.sendEmail();

        draftsPage = inboxPage.goToDrafts();
    }

    @Then("^the email should (not )?be present in (Drafts|Sent)$")
    public void emailShouldBePresentInFolder(String negation, String folder) {
        boolean shouldExist = negation == null;
        boolean actualResult;

        if ("Drafts".equals(folder)) {
            draftsPage = inboxPage.goToDrafts();
            actualResult = draftsPage.containsMail(subject);
        } else {
            sentPage = inboxPage.goToSent();
            actualResult = sentPage.containsMail(subject);
        }

        Assert.assertEquals(
                actualResult,
                shouldExist,
                "Email presence in " + folder + " does not match expectation."
        );
    }

    @When("the user logs out")
    public void userLogsOut() {
        loginPage = inboxPage.logout();
    }
}
