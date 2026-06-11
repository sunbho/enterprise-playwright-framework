package com.playwright.framework.tests;

import com.microsoft.playwright.Page;
import com.playwright.framework.config.ConfigManager;
import com.playwright.framework.pages.HomePage;
import com.playwright.framework.pages.ElementsPage;
import com.playwright.framework.pages.TextBoxPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test to verify the Text Box form submission flow.
 *
 * <p>Navigates: Home Page → Elements Page → Text Box Page
 * Fills form with test data and verifies the submitted values.</p>
 */
public class AddTextVerificationTest extends BaseTest {
//    ConfigManager config = ConfigManager.getInstance();

    private static final String FULL_NAME = "Sunil Bhobor";
    private static final String EMAIL = "Sunil@demo.com";
    private static final String CURRENT_ADDRESS = "Pune India";
    private static final String PERMANENT_ADDRESS = "Same as Above";

    @Test
    public void testAddTextAndVerifySubmission() {


        // Step 1: Navigate to Home Page and click Elements
//        HomePage homePage = new HomePage(page);
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        ElementsPage elementsPage = homePage.openElementsPage();


        // Step 2: Click on Text Box menu item
        elementsPage.openTextBoxPage();

        // Step 3: Fill form with test data
        TextBoxPage textBoxPage = new TextBoxPage(page);
        textBoxPage.submitFormWithData(FULL_NAME, EMAIL, CURRENT_ADDRESS, PERMANENT_ADDRESS);


        // Step 4: Verify submitted values
        page.waitForTimeout(1000);
        String nameOutput = textBoxPage.getNameOutput();
        String emailOutput = textBoxPage.getEmailOutput();
        String outputSectionText = textBoxPage.getOutputSectionText();

        Assert.assertTrue(
                nameOutput.contains("Name:" + FULL_NAME),
                "Expected name output to contain 'Name:" + FULL_NAME + "', but got: " + nameOutput);

        Assert.assertTrue(
                emailOutput.contains("Email:" + EMAIL),
                "Expected email output to contain 'Email:" + EMAIL + "', but got: " + emailOutput);

        Assert.assertTrue(
                outputSectionText.contains("Current Address :" + CURRENT_ADDRESS),
                "Expected current address in output, but got: " + outputSectionText);

        Assert.assertTrue(
                outputSectionText.contains("Permananet Address :" + PERMANENT_ADDRESS),
                "Expected permanent address in output, but got: " + outputSectionText);
    }
}