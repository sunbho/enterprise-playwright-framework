package com.playwright.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page object representing the DemoQA home page.
 *
 * <p>This class owns the locators and navigation operations available from
 * the home page. Assertions and test-specific behavior belong in the test
 * layer.</p>
 */
public final class HomePage {

    private static final String HOME_URL = "https://demoqa.com/";

    private final Page page;
    private final Locator elementsLink;
    private final Locator formsLink;
    private final Locator alertsLink;
    private final Locator widgetsLink;
    private final Locator interactionsLink;
    private final Locator bookStoreLink;


    /**
     * Creates a home page object backed by the supplied Playwright page.
     *
     * @param page active Playwright page
     */
    public HomePage(Page page) {
        this.page = page;
        this.elementsLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Elements"));
        this.formsLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Forms"));
        this.alertsLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Alerts, Frame & Windows"));
        this.widgetsLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Widgets"));
        this.interactionsLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Interactions"));
        this.bookStoreLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Book Store Application"));
    }

    /**
     * Navigates to the DemoQA home page.
     */
    public void navigateToHomePage() {
        page.navigate(HOME_URL);
    }

    /**
     * Opens the Elements section.
     *
     * @return page object for the Elements section
     */
    public ElementsPage openElementsPage() {
        elementsLink.click();
        return new ElementsPage(page);
    }

    /**
     * Opens the Forms section.
     */
    public void openFormsPage() {
        formsLink.click();
    }

    /**
     * Opens the Alerts, Frame and Windows section.
     */
    public void openAlertsPage() {
        alertsLink.click();
    }

    /**
     * Opens the Widgets section.
     */
    public void openWidgetsPage() {
        widgetsLink.click();
    }

    /**
     * Opens the Interactions section.
     */
    public void openInteractionsPage() {
        interactionsLink.click();
    }

    /**
     * Opens the Book Store Application section.
     */
    public void openBookStorePage() {
        bookStoreLink.click();
    }
}