# Selenium UI Automation Framework (Ukr.net Mailbox)

Automated UI testing framework for the Ukr.net mail service built with **Java**, **Selenium WebDriver**, **TestNG**, **Cucumber**, and **Log4j2**. The framework follows the **Page Object Model (POM)** pattern, **S.O.L.I.D.** principles, and core **Design Patterns**, and supports both conventional TestNG tests and Behavior-Driven Development (BDD) scenarios.

---

## 🏛 Architecture & Design Patterns

The framework architecture implements design patterns and separation of concerns to support maintainability, reusability, and clear test organization:

- **Singleton (Double-Checked Locking):** Implemented in `ConfigProvider` for centralized, thread-safe configuration management.
- **Factory Method:** Implemented through `WebDriverFactory` and the `DriverFactory` interface to separate browser-driver creation from test logic (SRP & OCP).
- **Decorator:** Selenium's `EventFiringDecorator`, together with the custom `LoggingWebDriverListener`, provides automatic WebDriver interaction logging without requiring logging code in Page Objects.
- **Builder:** Applied to the `EmailMessage` model to make test-message construction clear and flexible.
- **Page Object Model (POM):** Page Objects and reusable components encapsulate UI interactions and keep test steps readable.
- **Separation of Concerns:** Browser management, configuration, test data, page interactions, logging, and BDD step definitions are organized into dedicated components.

---

## 🛠 Tech Stack & Tools

- **Language:** Java 21 LTS
- **Automation Tool:** Selenium WebDriver 4.23.0
- **Test Framework:** TestNG 7.10.2
- **BDD Framework:** Cucumber 7.18.1
- **Logging:** Log4j2
- **Build Tool:** Maven (Surefire plugin and TestNG suite management)
- **CI/CD:** Jenkins (`Jenkinsfile`)
- **Version Control:** Git

---

## ✨ Key Features

- 🌐 **Cross-browser testing** with Chrome, Firefox, and Edge support.
- 🧱 **Reusable Page Objects and components** based on the POM pattern.
- ⚙️ **Centralized configuration** for environment settings and test data.
- 📝 **WebDriver interaction logging** through a custom Selenium listener.
- 🧪 **Smoke and Regression suites** managed through TestNG XML files.
- 🥒 **BDD testing with Cucumber** using readable Gherkin feature files and step definitions.
- ✉️ **Mailbox workflow coverage:** draft creation, draft-content verification, sending messages, and checking Drafts and Sent folders.
- 📸 **Failure diagnostics** through the TestNG listener and screenshot capture where configured.
- 📊 **Test reports** including Cucumber HTML and JSON reports.
- 🚀 **Jenkins integration** for configurable CI test execution.

---

## 📂 Project Structure

- `src/main/java/.../pages/` — Page Objects and reusable components: `BasePage`, `LoginPage`, `MailFolderPage`, `InboxPage`, `DraftsPage`, `SentPage`, `ComposeEmailComponent`, and `RichTextEditor`.
- `src/main/java/.../models/` — Test data models: `User` and `EmailMessage` (Builder pattern).
- `src/main/java/.../utils/` — Configuration management through `ConfigProvider`.
- `src/test/java/.../driver/` — `DriverManager`, `DriverFactory`, `WebDriverFactory`, browser-specific factories, and `LoggingWebDriverListener`.
- `src/test/java/.../listeners/` — `TestListener` for test execution events and failure diagnostics.
- `src/test/java/.../tests/` — Conventional TestNG UI tests.
- `src/test/java/.../bdd/hooks/` — Cucumber scenario hooks and browser cleanup.
- `src/test/java/.../bdd/runner/` — Cucumber TestNG runner and report configuration.
- `src/test/java/.../bdd/steps/` — Cucumber step definitions.
- `src/test/resources/features/` — Gherkin feature files, including `mailbox.feature`.
- `src/test/resources/` — TestNG XML suites and `cucumber-testng.xml`.
- `src/main/resources/` — Environment properties, Log4j2 configuration, and the example secret-properties template.

---

## ⚙️ Configuration

Before running authenticated tests, create a local `secret.properties` file in `src/main/resources/`. Use `secret.properties.example` as a template.

Example:

```properties
mail.email=your_email@ukr.net
mail.password=your_password
test.recipient=recipient_email@ukr.net
```

Use valid test-account credentials and a recipient address you control.

**🔐 Security notes:**
- `secret.properties` should be excluded from Git through `.gitignore`.
- Never commit real passwords or other sensitive credentials.
- Keep `secret.properties.example` limited to placeholder values.
- Avoid sharing logs or screenshots that expose credentials or private mailbox content.

Environment-specific settings are stored in `qa.properties` and `dev.properties`. Select the target environment with `-Denv=qa` or `-Denv=dev` where supported by the active test configuration.

---

## ⚠️ Important Note — UI Language

The Ukr.net interface language must be set to **Ukrainian**.

The tests rely on Ukrainian element locators and text selectors, including folder names and buttons. If your account uses a different interface language, switch it to Ukrainian in your mailbox settings before running the tests.

Authentication may require additional account verification. Complete any required verification manually through the official Ukr.net website before running authenticated scenarios.

---

## 🚀 How to Run Tests

Run Maven commands from the project root directory.

### 📌 Available Parameters

- `-Dbrowser` — target browser: `chrome`, `firefox`, or `edge`.
- `-Denv` — target environment: `qa` or `dev`.
- `-DsuiteXmlFile` — path to the TestNG XML suite.

### 🧪 Execution Examples

**1. Run the Regression suite with the default configuration:**

```bash
mvn clean test
```

**2. Run the Smoke suite on Chrome with QA configuration:**

```bash
mvn clean test -Dbrowser=chrome -Denv=qa "-DsuiteXmlFile=src/test/resources/smoke.xml"
```

**3. Run the Regression suite on Firefox with DEV configuration:**

```bash
mvn clean test -Dbrowser=firefox -Denv=dev "-DsuiteXmlFile=src/test/resources/regression.xml"
```

---

## 🥒 BDD Testing with Cucumber

The project includes a Cucumber BDD suite that describes mailbox behavior through readable Gherkin scenarios.

### Covered Scenarios

- Checking that mailbox test configuration is available.
- Creating a draft email.
- Verifying the draft recipient, subject, and body.
- Sending the draft email.
- Verifying that the email no longer appears in **Drafts** and is present in **Sent**.
- Logging out of the mailbox.
- Verifying the login page in supported browsers using a **Scenario Outline** with **Examples**.

### Run the BDD Suite

**Chrome:**

```bash
mvn test "-DsuiteXmlFile=src/test/resources/cucumber-testng.xml" "-Dbrowser=chrome"
```

**Firefox:**

```bash
mvn test "-DsuiteXmlFile=src/test/resources/cucumber-testng.xml" "-Dbrowser=firefox"
```

The BDD suite is configured through `CucumberTestRunner.java` and `cucumber-testng.xml`. Step implementations are located in `MailboxStepDefinitions.java`, while `CucumberHooks.java` handles browser cleanup after each scenario.

**Note:** The browser used by a Scenario Outline example is specified in the feature file. The authenticated mailbox workflow reads the `browser` system property, defaulting to Firefox if no property is provided.

---

## 📊 Reporting & CI/CD

- **📝 Logs:** Log4j2 is configured through `src/main/resources/log4j2.xml`. Log destinations and rollover behavior are determined by the active logging configuration.
- **📸 Screenshots on Failure:** `TestListener` supports failure diagnostics and screenshot capture. Where configured, screenshots are saved under `target/screenshots/`.
- **🥒 Cucumber HTML Report:** `target/cucumber-report.html`
- **📄 Cucumber JSON Report:** `target/cucumber-report.json`
- **🚀 Jenkins Pipeline:** The repository includes a `Jenkinsfile` for CI execution. Pipeline parameters, test result publishing, and artifact archiving depend on the current pipeline configuration.

After execution, review the generated reports to identify passed scenarios, failed steps, and relevant error details.

---

## 📝 Notes

- Test stability depends on the current Ukr.net interface, network conditions, browser compatibility, and account security requirements.
- Use a dedicated test account and a controlled recipient for scenarios that create or send emails.
- Keep environment configuration and credentials separate.
- Check browser selection in both the feature file and step definitions when changing the browser used by BDD tests.
