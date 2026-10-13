# S.O.L.I.D. Principles Implementation

This document describes the specific refactoring steps taken to enforce S.O.L.I.D. principles across the framework.

| Class / Component | Principle / Problem | Solution |
| :--- | :--- | :--- |
| `DriverManager` | **SRP broken** — created *and* stored/managed the driver | Creation extracted to `WebDriverFactory` and `DriverFactory` interface. `DriverManager` now only owns the `ThreadLocal` lifecycle. |
| **Page objects** | **OCP / SRP** — Logging of every UI action was scattered across POs | `LoggingWebDriverListener` + `EventFiringDecorator` implemented. Logging is moved out of the POs entirely (behaviour added without modifying them). |
| **Test data** | Tests passed loose `String` parameters around | Data encapsulated into `User` and `EmailMessage` models. `EmailMessage` built via an immutable **Builder** pattern. |
| `ComposeEmailComponent` | **SRP broken** — class held page structure, browser-specific JS, and DOM-scavenging heuristics | Extracted a `RichTextEditor` wrapper for TinyMCE/iframe interactions and a `JsExecutorHelper` for DOM-scavenging. The page object now reads as a sequence of pure business steps. |
| `TestListener` | **DIP broken** — depended on a concrete test class (`BaseTest`) via casting | Removed the cast `((BaseTest) currentClass).getDriver()`. The listener now retrieves the driver directly from the `DriverManager` abstraction. |
| `DraftsPage` & `SentPage` | **DRY / ISP broken** — both answered "is there a mail with this subject in this folder?", duplicating logic | Extracted an abstract `MailFolderPage extends BasePage` with `containsMail(String subject)`. `DraftsPage` and `SentPage` now inherit this logic and keep only what’s genuinely theirs. |
| `WebDriverFactory` | **OCP broken** — adding a browser meant editing a `switch` statement | Converted from a Simple Factory to the **GoF Factory Method** pattern. Created a `DriverFactory` interface and concrete classes (`ChromeDriverFactory`, etc.) resolved from a `Map`. New browsers require zero edits to existing factory routing code. |
