package com.playwright.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Page object representing the DemoQA Elements section.
 *
 * Uses BasePage helpers (navigate, getLocator, etc.) and waits for locators
 * instead of sleeping with page.waitForTimeout.
 */
public final class ElementsPage extends BasePage {

    private static final String ELEMENTS_URL = "https://demoqa.com/elements";

    // Use selector strings (role/text selectors are supported by Playwright)
    private static final String TEXT_BOX_SELECTOR = "text=Text Box"; // or "text=Text Box"
    private static final String CHECK_BOX_SELECTOR = "text=Check Box"; // replace if needed
    private static final String RADIO_BUTTON_SELECTOR = "text=Radio Button";
    private static final String WEB_TABLES_SELECTOR = "text=Web Tables";
    private static final String BUTTONS_SELECTOR = "text=Buttons";
    private static final String LINKS_SELECTOR = "text=Links";
    private static final String BROKEN_LINKS_SELECTOR = "text=Broken Links - Images";
    private static final String UPLOAD_DOWNLOAD_SELECTOR = "text=Upload and Download";
    private static final String DYNAMIC_PROPERTIES_SELECTOR = "text=Dynamic Properties";

    /**
     * Creates an Elements page object backed by the supplied Playwright page.
     *
     * @param page active Playwright page
     */
    public ElementsPage(Page page) {
        super(page); // BasePage protects against null page
    }

    /**
     * Navigates directly to the DemoQA Elements section.
     */
    public void navigateToElementsPage() {
        navigate(ELEMENTS_URL); // BasePage.navigate
    }

//    private Locator waitForVisible(String selector) {
//        Locator locator = getLocator(selector);
//        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//        return locator;
//    }

    /**
     * Opens the Text Box page.
     */
    public TextBoxPage openTextBoxPage() {
        waitForVisibleLocator(TEXT_BOX_SELECTOR).click();
        return new TextBoxPage(page);
    }

    /**
     * Opens the Check Box page.
     */
    public void openCheckBoxPage() {
        waitForVisibleLocator(CHECK_BOX_SELECTOR).click();
    }

    /**
     * Opens the Radio Button page.
     */
    public void openRadioButtonPage() {
        waitForVisibleLocator(RADIO_BUTTON_SELECTOR).click();
    }

    /**
     * Opens the Web Tables page.
     */
    public void openWebTablesPage() {
        waitForVisibleLocator(WEB_TABLES_SELECTOR).click();
    }

    /**
     * Opens the Buttons page.
     */
    public void openButtonsPage() {
        waitForVisibleLocator(BUTTONS_SELECTOR).click();
    }

    /**
     * Opens the Links page.
     */
    public void openLinksPage() {
        waitForVisibleLocator(LINKS_SELECTOR).click();
    }

    /**
     * Opens the Broken Links - Images page.
     */
    public void openBrokenLinksPage() {
        waitForVisibleLocator(BROKEN_LINKS_SELECTOR).click();
    }

    /**
     * Opens the Upload and Download page.
     */
    public void openUploadDownloadPage() {
        waitForVisibleLocator(UPLOAD_DOWNLOAD_SELECTOR).click();
    }

    /**
     * Opens the Dynamic Properties page.
     */
    public void openDynamicPropertiesPage() {
        waitForVisibleLocator(DYNAMIC_PROPERTIES_SELECTOR).click();
    }
}