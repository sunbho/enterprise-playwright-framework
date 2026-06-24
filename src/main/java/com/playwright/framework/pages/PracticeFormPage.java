package com.playwright.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.playwright.framework.models.PracticeFormData;
import io.qameta.allure.Step;

import java.nio.file.Path;
import java.util.List;

/**
 * Page object for the DemoQA automation practice form.
 */
public final class PracticeFormPage extends BasePage {

    private static final String PRACTICE_FORM_URL = "https://demoqa.com/automation-practice-form";
    private static final String FIRST_NAME_INPUT = "#firstName";
    private static final String LAST_NAME_INPUT = "#lastName";
    private static final String EMAIL_INPUT = "#userEmail";
    private static final String MOBILE_INPUT = "#userNumber";
    private static final String DATE_OF_BIRTH_INPUT = "#dateOfBirthInput";
    private static final String SUBJECT_INPUT = "#subjectsInput";
    private static final String PICTURE_INPUT = "#uploadPicture";
    private static final String ADDRESS_INPUT = "#currentAddress";
    private static final String STATE_DROPDOWN = "#state";
    private static final String CITY_DROPDOWN = "#city";
    private static final String SUBMIT_BUTTON = "#submit";
    private static final String CONFIRMATION_MODAL = ".modal-content";
    private static final String CONFIRMATION_MODAL_TITLE = "#example-modal-sizes-title-lg";
    private static final String SUBMITTED_DATA_TABLE = ".table-responsive";

    /**
     * Creates a practice form page object.
     *
     * @param page active Playwright page
     */
    public PracticeFormPage(Page page) {
        super(page);
    }

    @Step("Navigate to DemoQA practice form")
    public void navigateToPracticeForm() {
        navigate(PRACTICE_FORM_URL);
        waitForPageLoad();
    }

    @Step("Enter first name: {firstName}")
    public void enterFirstName(String firstName) {
        clearAndType(FIRST_NAME_INPUT, firstName);
    }

    @Step("Enter last name: {lastName}")
    public void enterLastName(String lastName) {
        clearAndType(LAST_NAME_INPUT, lastName);
    }

    @Step("Enter email: {email}")
    public void enterEmail(String email) {
        clearAndType(EMAIL_INPUT, email);
    }

    @Step("Select gender: {gender}")
    public void selectGender(String gender) {
        logger.info("Selecting gender: {}", gender);
        page.getByText(gender, new Page.GetByTextOptions().setExact(true)).click();
    }

    @Step("Enter mobile number: {mobile}")
    public void enterMobile(String mobile) {
        clearAndType(MOBILE_INPUT, mobile);
    }

    /**
     * Selects a stable default date of birth for the form example.
     */
    @Step("Select date of birth")
    public void selectDateOfBirth() {
        selectDateOfBirth("January", "1990", "1");
    }

    @Step("Select date of birth: {day} {month} {year}")
    public void selectDateOfBirth(String month, String year, String day) {
        logger.info("Selecting date of birth: {} {} {}", day, month, year);
        click(DATE_OF_BIRTH_INPUT);
        page.locator(".react-datepicker__month-select")
                .selectOption(new SelectOption().setLabel(month));
        page.locator(".react-datepicker__year-select")
                .selectOption(new SelectOption().setLabel(year));
        page.locator(".react-datepicker__day:not(.react-datepicker__day--outside-month)")
                .filter(new Locator.FilterOptions().setHasText(day))
                .first()
                .click();
    }

    @Step("Select subject: {subject}")
    public void selectSubject(String subject) {
        logger.info("Selecting subject: {}", subject);
        Locator subjectInput = waitForVisibleLocator(SUBJECT_INPUT);
        subjectInput.fill(subject);
        page.getByText(subject, new Page.GetByTextOptions().setExact(true)).click();
    }

    @Step("Select hobbies: {hobbies}")
    public void selectHobbies(List<String> hobbies) {
        logger.info("Selecting hobbies: {}", hobbies);
        for (String hobby : hobbies) {
            page.getByText(hobby, new Page.GetByTextOptions().setExact(true)).click();
        }
    }

    @Step("Upload picture: {picturePath}")
    public void uploadPicture(String picturePath) {
        logger.info("Uploading picture: {}", picturePath);
        Path resolvedPath = resolveClasspathResource(picturePath);
        page.locator(PICTURE_INPUT).setInputFiles(resolvedPath);
    }

    @Step("Enter current address")
    public void enterAddress(String address) {
        clearAndType(ADDRESS_INPUT, address);
    }

    @Step("Select state: {state}")
    public void selectState(String state) {
        logger.info("Selecting state: {}", state);
        click(STATE_DROPDOWN);
        selectReactDropdownOption(state);
    }

    @Step("Select city: {city}")
    public void selectCity(String city) {
        logger.info("Selecting city: {}", city);
        click(CITY_DROPDOWN);
        selectReactDropdownOption(city);
    }

    @Step("Submit practice form")
    public void clickSubmit() {
        logger.info("Submitting practice form");
        scrollIntoView(SUBMIT_BUTTON);
        click(SUBMIT_BUTTON);
    }

    @Step("Verify confirmation modal is displayed")
    public void verifyConfirmationModal() {
        logger.info("Verifying confirmation modal");
        waitForVisibleLocator(CONFIRMATION_MODAL_TITLE);
        if (!isVisible(CONFIRMATION_MODAL)) {
            throw new AssertionError("Practice form confirmation modal is not visible");
        }
    }

    @Step("Verify submitted practice form data")
    public void verifySubmittedData(PracticeFormData data) {
        logger.info("Verifying submitted practice form data for {}", data);
        String submittedData = getText(SUBMITTED_DATA_TABLE);
        verifyContains(submittedData, data.getFirstName() + " " + data.getLastName());
        verifyContains(submittedData, data.getEmail());
        verifyContains(submittedData, data.getGender());
        verifyContains(submittedData, data.getMobile());
        verifyContains(submittedData, data.getSubject());
        verifyContains(submittedData, String.join(", ", data.getHobbies()));
        verifyContains(submittedData, data.getAddress());
        verifyContains(submittedData, data.getState() + " " + data.getCity());
    }

    @Step("Fill practice form for: {data.firstName} {data.lastName}")
    public void fillPracticeForm(PracticeFormData data) {
        logger.info("Filling practice form with data: {}", data);
        enterFirstName(data.getFirstName());
        enterLastName(data.getLastName());
        enterEmail(data.getEmail());
        selectGender(data.getGender());
        enterMobile(data.getMobile());
        selectDateOfBirth();
        selectSubject(data.getSubject());
        selectHobbies(data.getHobbies());
        uploadPicture(data.getPicture());
        enterAddress(data.getAddress());
        selectState(data.getState());
        selectCity(data.getCity());
    }

    private void selectReactDropdownOption(String option) {
        page.getByText(option, new Page.GetByTextOptions().setExact(true)).last().click();
    }

    private Path resolveClasspathResource(String resourcePath) {
        try {
            var resource = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(resourcePath);
            if (resource == null) {
                throw new IllegalArgumentException("Resource not found on classpath: " + resourcePath);
            }
            return Path.of(resource.toURI());
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to resolve upload resource: " + resourcePath, exception);
        }
    }

    private void verifyContains(String actual, String expected) {
        if (expected == null || expected.isBlank()) {
            return;
        }
        if (!actual.contains(expected)) {
            throw new AssertionError(
                    "Expected submitted data to contain '" + expected + "', but was: " + actual);
        }
    }
}
