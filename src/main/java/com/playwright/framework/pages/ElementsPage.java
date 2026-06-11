package com.playwright.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page object representing the DemoQA Elements section.
 *
 * <p>This class contains section-menu locators and navigation operations only.
 * Assertions and test-specific behavior belong in the test layer.</p>
 */
public final class ElementsPage {

    private static final String ELEMENTS_URL = "https://demoqa.com/elements";

    private final Page page;
    private final Locator textBoxMenu;
    private final Locator checkBoxMenu;
    private final Locator radioButtonMenu;
    private final Locator webTablesMenu;
    private final Locator buttonsMenu;
    private final Locator linksMenu;
    private final Locator brokenLinksMenu;
    private final Locator uploadDownloadMenu;
    private final Locator dynamicPropertiesMenu;

    /**
     * Creates an Elements page object backed by the supplied Playwright page.
     *
     * @param page active Playwright page
     */
    public ElementsPage(Page page) {
        this.page = page;
        this.textBoxMenu = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Text Box"));// TODO Add locator
        this.checkBoxMenu = page.locator("[data-testid='TODO-check-box-menu']"); // TODO Add locator
        this.radioButtonMenu = page.locator("[data-testid='TODO-radio-button-menu']"); // TODO Add locator
        this.webTablesMenu = page.locator("[data-testid='TODO-web-tables-menu']"); // TODO Add locator
        this.buttonsMenu = page.locator("[data-testid='TODO-buttons-menu']"); // TODO Add locator
        this.linksMenu = page.locator("[data-testid='TODO-links-menu']"); // TODO Add locator
        this.brokenLinksMenu = page.locator("[data-testid='TODO-broken-links-menu']"); // TODO Add locator
        this.uploadDownloadMenu = page.locator("[data-testid='TODO-upload-download-menu']"); // TODO Add locator
        this.dynamicPropertiesMenu = page.locator("[data-testid='TODO-dynamic-properties-menu']"); // TODO Add locator
    }

    /**
     * Navigates directly to the DemoQA Elements section.
     */
    public void navigateToElementsPage() {
        page.navigate(ELEMENTS_URL);
    }

    /**
     * Opens the Text Box page.
     */
    public TextBoxPage openTextBoxPage() {
        textBoxMenu.click();
        return new TextBoxPage(page);
    }

    /**
     * Opens the Check Box page.
     */
    public void openCheckBoxPage() {
        checkBoxMenu.click();
    }

    /**
     * Opens the Radio Button page.
     */
    public void openRadioButtonPage() {
        radioButtonMenu.click();
    }

    /**
     * Opens the Web Tables page.
     */
    public void openWebTablesPage() {
        webTablesMenu.click();
    }

    /**
     * Opens the Buttons page.
     */
    public void openButtonsPage() {
        buttonsMenu.click();
    }

    /**
     * Opens the Links page.
     */
    public void openLinksPage() {
        linksMenu.click();
    }

    /**
     * Opens the Broken Links - Images page.
     */
    public void openBrokenLinksPage() {
        brokenLinksMenu.click();
    }

    /**
     * Opens the Upload and Download page.
     */
    public void openUploadDownloadPage() {
        uploadDownloadMenu.click();
    }

    /**
     * Opens the Dynamic Properties page.
     */
    public void openDynamicPropertiesPage() {
        dynamicPropertiesMenu.click();
    }
}
