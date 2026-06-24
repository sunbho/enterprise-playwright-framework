package com.playwright.framework.tests;

import com.playwright.framework.pages.CheckBoxPage;
import com.playwright.framework.pages.ElementsPage;
import com.playwright.framework.pages.HomePage;
import org.testng.annotations.Test;

public class CheckBoxesTest extends BaseTest {

    @Test
    public void checkUncheckVerificationTest() {
        // Step 1: Navigate to Home Page and click Elements
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        homePage.openElementsPage();
        ElementsPage elementsPage = new ElementsPage(page);
        elementsPage.openCheckBoxPage();
        CheckBoxPage checkBoxPage = new CheckBoxPage(page);
        checkBoxPage.expandNode("Home");
        checkBoxPage.expandNode("Desktop");
        checkBoxPage.expandNode("Documents");
        checkBoxPage.expandNode("WorkSpace");
        checkBoxPage.expandNode("Office");
        checkBoxPage.expandNode("Downloads");
        checkBoxPage.collapseNode("Desktop");
        checkBoxPage.collapseNode("WorkSpace");
        checkBoxPage.selectCheckbox("Office");
        checkBoxPage.selectCheckbox("Excel File.doc");
        page.waitForTimeout(5000);
        assert checkBoxPage.isCheckboxChecked("Excel File.doc") : "Office checkbox should be checked";
        assert checkBoxPage.verifySelectedItems("Public", "Private", "Classified", "General");




    }
}
