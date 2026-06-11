package com.playwright.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page object representing the DemoQA Text Box page.
 *
 * <p>This class owns the locators and operations available from the Text Box page.
 * Assertions and test-specific behavior belong in the test layer.</p>
 */
public final class TextBoxPage {

    private static final String TEXT_BOX_URL = "https://demoqa.com/text-box";

    private final Page page;
    private final Locator fullNameTextBox;
    private final Locator emailTextBox;
    private final Locator currentAddressTextBox;
    private final Locator permanentAddressTextBox;
    private final Locator submitButton;
    private final Locator nameOutput;
    private final Locator emailOutput;
    private final Locator outputSection;

    /**
     * Creates a Text Box page object backed by the supplied Playwright page.
     *
     * @param page active Playwright page
     */
    public TextBoxPage(Page page) {
        this.page = page;
        this.fullNameTextBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Full Name"));
        this.emailTextBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("name@example.com"));
        this.currentAddressTextBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Current Address"));
        this.permanentAddressTextBox = page.locator("#permanentAddress");
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.nameOutput = page.locator("#name");
        this.emailOutput = page.locator("#email");
        this.outputSection = page.locator("#output");
    }

    /**
     * Navigates directly to the DemoQA Text Box page.
     */
    public void navigateToTextBoxPage() {
        page.navigate(TEXT_BOX_URL);
    }

    /**
     * Fills in the Full Name field.
     *
     * @param fullName the full name to enter
     */
    public void fillFullName(String fullName) {
        fullNameTextBox.click();
        fullNameTextBox.fill(fullName);
    }

    /**
     * Fills in the Email field.
     *
     * @param email the email address to enter
     */
    public void fillEmail(String email) {
        emailTextBox.click();
        emailTextBox.fill(email);
    }

    /**
     * Fills in the Current Address field.
     *
     * @param address the current address to enter
     */
    public void fillCurrentAddress(String address) {
        currentAddressTextBox.click();
        currentAddressTextBox.fill(address);
    }

    /**
     * Fills in the Permanent Address field.
     *
     * @param address the permanent address to enter
     */
    public void fillPermanentAddress(String address) {
        permanentAddressTextBox.click();
        permanentAddressTextBox.fill(address);
    }

    /**
     * Submits the form by clicking the Submit button.
     */
    public void submitForm() {
        submitButton.click();
    }

    /**
     * Gets the name output text.
     *
     * @return the text content of the name output element
     */
    public String getNameOutput() {
        return nameOutput.textContent();
    }

    /**
     * Gets the email output text.
     *
     * @return the text content of the email output element
     */
    public String getEmailOutput() {
        return emailOutput.textContent();
    }

    /**
     * Gets the complete output section text.
     *
     * @return the text content of the output section
     */
    public String getOutputSectionText() {
        return outputSection.textContent();
    }

    /**
     * Submits the form with the provided information.
     *
     * @param fullName the full name
     * @param email the email address
     * @param currentAddress the current address
     * @param permanentAddress the permanent address
     */
    public void submitFormWithData(String fullName, String email, String currentAddress, String permanentAddress) {
        fillFullName(fullName);
        fillEmail(email);
        fillCurrentAddress(currentAddress);
        fillPermanentAddress(permanentAddress);
        submitForm();
    }
}