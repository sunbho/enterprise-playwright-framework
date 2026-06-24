package com.playwright.framework.pages;

import com.microsoft.playwright.Page;
import com.playwright.framework.models.TextBoxData;
import com.playwright.framework.utils.AssertionUtils;

/**
 * Page object representing the DemoQA Text Box page.
 *
 * <p>This class owns the locators and operations available from the Text Box page.
 * Assertions and test-specific behavior belong in the test layer.</p>
 */
public final class TextBoxPage extends BasePage{

    private static final String TEXT_BOX_URL = "https://demoqa.com/text-box";


    private final String fullNameTextBox = "role=textbox[name='Full Name']";
    private final String emailTextBox = "role=textbox[name='name@example.com']";
    private final String currentAddressTextBox = "role=textbox[name='Current Address']";
    private final String permanentAddressTextBox = "#permanentAddress";
    private final String submitButton = "role=button[name='Submit']";
    private final String nameOutput = "#name";
    private final String emailOutput = "#email";
    private final String outputSection = "#output";

    /**
     * Creates a Text Box page object backed by the supplied Playwright page.
     *
     * @param page active Playwright page
     */
    public TextBoxPage(Page page) {
        super(page);
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
        waitForVisibleLocator(fullNameTextBox).click();
        type(fullNameTextBox, fullName);
    }

    /**
     * Fills in the Email field.
     *
     * @param email the email address to enter
     */
    public void fillEmail(String email) {
        waitForVisibleLocator(emailTextBox).click();
        type(emailTextBox, email);
    }

    /**
     * Fills in the Current Address field.
     *
     * @param address the current address to enter
     */
    public void fillCurrentAddress(String address) {
        waitForVisibleLocator(currentAddressTextBox).click();
        type(currentAddressTextBox, address);
    }

    /**
     * Fills in the Permanent Address field.
     *
     * @param address the permanent address to enter
     */
    public void fillPermanentAddress(String address) {
        waitForVisibleLocator(permanentAddressTextBox).click();
        type(permanentAddressTextBox, address);
    }

    /**
     * Submits the form by clicking the Submit button.
     */
    public void submitForm() {
        click(submitButton);
    }

    /**
     * Gets the name output text.
     *
     * @return the text content of the name output element
     */
    public String getNameOutput() {
      return getText(nameOutput);
    }

    /**
     * Gets the email output text.
     *
     * @return the text content of the email output element
     */
    public String getEmailOutput() {
        return getText(emailOutput);
    }

    /**
     * Gets the complete output section text.
     *
     * @return the text content of the output section
     */
    public String getOutputSectionText() {
        return getText(outputSection);
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

    /**
     * Submits the form using DataProvider.
     * @param textBoxData TextBoxData json
     */
    public void submitFormWithData(TextBoxData  textBoxData) {
        fillFullName(textBoxData.getFullName());
        fillEmail(textBoxData.getEmail());
        fillCurrentAddress(textBoxData.getAddress());
        fillPermanentAddress(textBoxData.getPermanentAddress());
        submitForm();
    }

    /**
     * Verify the form  data using DataProvider.
     * @param textBoxData TextBoxData json
     */
    public void verifyTextData(TextBoxData  textBoxData){
        AssertionUtils.verifyContains(getNameOutput(), "Name:" + textBoxData.getFullName());
        AssertionUtils.verifyContains(getEmailOutput(), "Email:" + textBoxData.getEmail());
        AssertionUtils.verifyContains(getOutputSectionText(), "Current Address :" + textBoxData.getAddress());
    }


}