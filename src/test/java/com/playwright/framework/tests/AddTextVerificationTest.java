package com.playwright.framework.tests;

import com.playwright.framework.dataproviders.GenericDataProvider;
import com.playwright.framework.models.TextBoxData;
import com.playwright.framework.pages.HomePage;
import com.playwright.framework.pages.ElementsPage;
import com.playwright.framework.pages.TextBoxPage;
import com.playwright.framework.utils.AssertionUtils;
import com.playwright.framework.utils.WaitUtils;
import org.testng.annotations.Test;

/**
 * Test to verify the Text Box form submission flow.
 *
 * <p>Navigates: Home Page → Elements Page → Text Box Page
 * Fills form with test data and verifies the submitted values.</p>
 */
public class AddTextVerificationTest extends BaseTest {

    private static final String FULL_NAME = "Sunil Bhosale";
    private static final String EMAIL = "Sunil@demo.com";
    private static final String CURRENT_ADDRESS = "Pune India";
    private static final String PERMANENT_ADDRESS = "Same as Above";

    @Test
    public void testAddTextAndVerifySubmission() {
        // Step 1: Navigate to Home Page and click Elements
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        ElementsPage elementsPage = homePage.openElementsPage();
        WaitUtils.waitForLoadState(page);
        // Step 2: Click on Text Box menu item
        elementsPage.openTextBoxPage();
        // Step 3: Fill form with test data
        TextBoxPage textBoxPage = new TextBoxPage(page);
        WaitUtils.waitForLoadState(page);
        textBoxPage.submitFormWithData(FULL_NAME, EMAIL, CURRENT_ADDRESS, PERMANENT_ADDRESS);
        // Step 4: Verify submitted values
        String nameOutput = textBoxPage.getNameOutput();
        String emailOutput = textBoxPage.getEmailOutput();
        String outputSectionText = textBoxPage.getOutputSectionText();
        AssertionUtils.verifyContains(nameOutput, "Name:" + FULL_NAME);
        AssertionUtils.verifyContains(emailOutput, "Email:" + EMAIL);
        AssertionUtils.verifyContains(outputSectionText, "Current Address :" + CURRENT_ADDRESS);
    }

    @Test(
            dataProvider = "textBoxFormData",
            dataProviderClass = GenericDataProvider.class,
            description = "Submit DemoQA testBox form with JSON data")
    public void testAddTextAndVerifySubmissionDataProvider(TextBoxData data) {
        // Step 1: Navigate to Home Page and click Elements
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        ElementsPage elementsPage = homePage.openElementsPage();
        WaitUtils.waitForLoadState(page);
        // Step 2: Click on Text Box menu item
        elementsPage.openTextBoxPage();
        // Step 3: Fill form with test data
        TextBoxPage textBoxPage = new TextBoxPage(page);
        WaitUtils.waitForLoadState(page);
        textBoxPage.submitFormWithData(data);
        textBoxPage.verifyTextData(data);
    }


}