Feature: Mailbox email management

Background:
Given the mailbox test configuration is available

Scenario: Create a draft, send it, and verify the sent email
Given the user is logged in to the mailbox
When the user creates a draft email
Then the draft should contain the correct recipient, subject, and body
When the user sends the draft email
Then the email should no longer exist in Drafts
And the email should be present in Sent
When the user logs out
Then the login page should be displayed

Scenario Outline: Login page is displayed in supported browsers
When the user opens the mailbox login page in "<browser>"
Then the login page should be displayed

  Examples:
    | browser |
    | firefox |
    | chrome  |
