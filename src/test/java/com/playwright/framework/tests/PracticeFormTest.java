package com.playwright.framework.tests;

import com.playwright.framework.dataproviders.GenericDataProvider;
import com.playwright.framework.models.PracticeFormData;
import com.playwright.framework.pages.PracticeFormPage;
import com.playwright.framework.utils.AllureUtils;
import com.playwright.framework.utils.LoggerUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.testng.annotations.Test;

/**
 * Data-driven tests for DemoQA automation practice form.
 */
@Epic("DemoQA")
@Feature("Practice Form")
public class PracticeFormTest extends BaseTest {

    private static final Logger LOGGER = LoggerUtils.getLogger(PracticeFormTest.class);

    @Test(
            dataProvider = "practiceFormData",
            dataProviderClass = GenericDataProvider.class,
            description = "Submit DemoQA practice form with JSON data")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Loads practice form data from JSON, submits the form, and verifies modal output.")
    public void shouldSubmitPracticeFormUsingJsonData(PracticeFormData data) {
        LOGGER.info("Starting practice form data-driven test for {}", data);
        AllureUtils.attachText("Practice Form Test Data", data.toString());

        PracticeFormPage practiceFormPage = new PracticeFormPage(page);
        navigateToPracticeForm(practiceFormPage);
        fillPracticeForm(practiceFormPage, data);
        submitPracticeForm(practiceFormPage);
        verifyPracticeFormSubmission(practiceFormPage, data);

        LOGGER.info("Completed practice form data-driven test for {}", data);
    }

    @Step("Navigate to Practice Form")
    private void navigateToPracticeForm(PracticeFormPage practiceFormPage) {
        practiceFormPage.navigateToPracticeForm();
    }

    @Step("Fill Practice Form")
    private void fillPracticeForm(PracticeFormPage practiceFormPage, PracticeFormData data) {
        practiceFormPage.fillPracticeForm(data);
    }

    @Step("Submit Practice Form")
    private void submitPracticeForm(PracticeFormPage practiceFormPage) {
        practiceFormPage.clickSubmit();
    }

    @Step("Verify Practice Form Modal Data")
    private void verifyPracticeFormSubmission(
            PracticeFormPage practiceFormPage, PracticeFormData data) {
        practiceFormPage.verifyConfirmationModal();
        practiceFormPage.verifySubmittedData(data);
    }
}
