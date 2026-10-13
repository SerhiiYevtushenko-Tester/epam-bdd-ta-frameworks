package com.epam.rd.autotasks.bdd.steps;

import com.epam.rd.autotasks.driver.DriverManager;
import com.epam.rd.autotasks.models.EmailMessage;
import com.epam.rd.autotasks.models.User;
import com.epam.rd.autotasks.pages.ComposeEmailComponent;
import com.epam.rd.autotasks.pages.DraftsPage;
import com.epam.rd.autotasks.pages.InboxPage;
import com.epam.rd.autotasks.pages.LoginPage;
import com.epam.rd.autotasks.pages.SentPage;
import com.epam.rd.autotasks.utils.ConfigProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.testng.Assert;

public class MailboxStepDefinitions {

    private LoginPage loginPage;
    private InboxPage inboxPage;
    private DraftsPage draftsPage;
    private SentPage sentPage;
    private ComposeEmailComponent composeMail;

    private User testUser;
    private EmailMessage testMessage;

    private String subject;
    private String body;
    private String recipient;

    @Given("the mailbox test configuration is available")
    public void mailboxTestConfigurationIsAvailable() {
        ConfigProvider.getInstance().getRequiredProperty("base.url");
    }

    @When("^the user opens the mailbox login page in \"([^\"]*)\"$")
    public void openMailboxLoginPage(String browser) {
        String url = ConfigProvider.getInstance().getRequiredProperty("base.url");

        DriverManager.initDriver(browser.toLowerCase());
        DriverManager.getDriver().get(url);

        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @Then("the login page should be displayed")
    public void loginPageShouldBeDisplayed() {
        Assert.assertTrue(
                loginPage.isLoginScreenDisplayed(),
                "Login page is not displayed."
        );
    }

    @Given("the user is logged in to the mailbox")
    public void userIsLoggedInToMailbox() {
        ConfigProvider config = ConfigProvider.getInstance();

        String email = config.getRequiredProperty("mail.email");
        String password = config.getRequiredProperty("mail.password");
        String url = config.getRequiredProperty("base.url");

        recipient = config.getProperty("test.recipient");
        if (recipient == null || recipient.trim().isEmpty()) {
            recipient = config.getRequiredProperty("mail.recipient");
        }

        subject = config.getRequiredProperty("test.email.subject")
                + " " + System.currentTimeMillis();
        body = config.getRequiredProperty("test.email.body");

        testUser = new User(email, password);
        testMessage = EmailMessage.builder()
                .to(recipient)
                .subject(subject)
                .body(body)
                .build();

        String browser = System.getProperty("browser", "firefox")
                .toLowerCase();

        DriverManager.initDriver(browser);
        DriverManager.getDriver().get(url);

        loginPage = new LoginPage(DriverManager.getDriver());
        inboxPage = loginPage.login(testUser);

        Assert.assertTrue(
                inboxPage.isLoginSuccessful(),
                "Login failed."
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

    @Then("the email should no longer exist in Drafts")
    public void emailShouldNoLongerExistInDrafts() {
        Assert.assertFalse(
                draftsPage.containsMail(subject),
                "Email is still present in Drafts after sending."
        );
    }

    @And("the email should be present in Sent")
    public void emailShouldBePresentInSent() {
        sentPage = inboxPage.goToSent();

        Assert.assertTrue(
                sentPage.containsMail(subject),
                "Email is not present in Sent folder."
        );
    }

    @When("the user logs out")
    public void userLogsOut() {
        loginPage = inboxPage.logout();
    }
}
