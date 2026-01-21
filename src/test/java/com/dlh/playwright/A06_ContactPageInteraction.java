package com.dlh.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class A06_ContactPageInteraction {

    static Playwright playwright;
    static Browser browser;
    BrowserContext browserContext;
    Page page;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
                        .setHeadless(false)
        );
    }

    @BeforeEach
    void setup() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextFields {

        @DisplayName("Complete the form")
        @Test
        void completeForm() throws URISyntaxException {
            page.navigate("https://practicesoftwaretesting.com/contact");
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailAddressField = page.getByLabel("Email address");
            var messageField = page.getByLabel("Message *");
            var subjectDropDown = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");


            firstNameField.fill("Sarah-jane");
            lastNameField.fill("Smith");
            emailAddressField.fill("sarah-jane@example.com");
            messageField.fill("Hello, world!");
            subjectDropDown.selectOption(new SelectOption().setIndex(2));
            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
            page.setInputFiles("#attachment", fileToUpload);


            assertThat(firstNameField).hasValue("Sarah-jane");
            assertThat(lastNameField).hasValue("Smith");
            assertThat(emailAddressField).hasValue("sarah-jane@example.com");
            assertThat(messageField).hasValue("Hello, world!");
            assertThat(subjectDropDown).hasValue("webmaster");
            String uploadedFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");

        }
    }

    @DisplayName("Mandatory fields")
    @ParameterizedTest
    @ValueSource(strings = {"First name", "Last name", "Email address", "Message *"})
    void mandatoryFields(String fieldName) throws URISyntaxException {
        page.navigate("https://practicesoftwaretesting.com/contact");
        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailAddressField = page.getByLabel("Email address");
        var messageField = page.getByLabel("Message *");
        var subjectDropDown = page.getByLabel("Subject");
        var uploadField = page.getByLabel("Attachment");
        var sendButton = page.getByText("Send");

        firstNameField.fill("Sarah-jane");
        lastNameField.fill("Smith");
        emailAddressField.fill("sarah-jane@example.com");
        messageField.fill("Learning Playwright with JUnit builds strong automation skills through practice, structure, and curiosity every day");
        subjectDropDown.selectOption(new SelectOption().setIndex(2));
        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        page.setInputFiles("#attachment", fileToUpload);



        // Fill in the Field Values

        assertThat(firstNameField).hasValue("Sarah-jane");
        assertThat(lastNameField).hasValue("Smith");
        assertThat(emailAddressField).hasValue("sarah-jane@example.com");
        assertThat(messageField).hasValue("Learning Playwright with JUnit builds strong automation skills through practice, structure, and curiosity every day");

        // Clear one of the fields
        page.getByLabel(fieldName).clear();
        sendButton.click();;

        // Check the error meassage for that field
        var errorMessage =  page.getByRole(AriaRole.ALERT).getByText(fieldName+ " is required.");
        assertThat(errorMessage).isVisible();

    }
}
