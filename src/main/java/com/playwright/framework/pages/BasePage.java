package com.playwright.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.playwright.framework.utils.LoggerUtils;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * Base abstraction for application page objects.
 *
 * <p>Provides a small set of reusable Playwright interactions while allowing
 * concrete pages to retain ownership of their selectors and workflows.</p>
 */
public abstract class BasePage {

    protected final Page page;
    protected final Logger logger;

    /**
     * Creates a page object backed by an active Playwright page.
     *
     * @param page active Playwright page
     */
    protected BasePage(Page page) {
        this.page = Objects.requireNonNull(page, "page must not be null");
        this.logger = LoggerUtils.getLogger(getClass());
    }

    protected void click(String locator) {
        logger.info("Clicking element: {}", locator);
        getLocator(locator).click();
    }

    protected void type(String locator, String text) {
        logger.info("Entering value into element: {}", locator);
        getLocator(locator).fill(text);
    }

    protected void clearAndType(String locator, String text) {
        logger.info("Clearing and entering value into element: {}", locator);
        Locator element = getLocator(locator);
        element.clear();
        element.fill(text);
    }

    protected String getText(String locator) {
        logger.info("Reading text from element: {}", locator);
        return getLocator(locator).innerText();
    }

    protected boolean isVisible(String locator) {
        logger.info("Verifying visibility of element: {}", locator);
        return getLocator(locator).isVisible();
    }

    protected Locator getLocator(String locator) {
        return page.locator(locator);
    }

    protected void navigate(String url) {
        logger.info("Navigating to URL: {}", url);
        page.navigate(url);
    }

    protected String getPageTitle() {
        logger.info("Reading page title");
        return page.title();
    }

    protected void waitForPageLoad() {
        logger.info("Waiting for page DOM content to load");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    protected void scrollIntoView(String locator) {
        logger.info("Scrolling element into view: {}", locator);
        getLocator(locator).scrollIntoViewIfNeeded();
    }

    protected void hover(String locator) {
        logger.info("Hovering over element: {}", locator);
        getLocator(locator).hover();
    }

    protected void doubleClick(String locator) {
        logger.info("Double-clicking element: {}", locator);
        getLocator(locator).dblclick();
    }

    protected void rightClick(String locator) {
        logger.info("Right-clicking element: {}", locator);
        getLocator(locator).click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
    }

    protected Locator waitForVisibleLocator(String selector) {
        logger.info("Waiting for element to be visible: {}", selector);
        Locator l = getLocator(selector);
        l.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return l;
    }

    protected void fillWhenVisible(String selector, String text) {
        logger.info("Entering value into visible element: {}", selector);
        waitForVisibleLocator(selector).fill(text);
    }

    protected void clickWhenVisible(String selector) {
        logger.info("Clicking visible element: {}", selector);
        waitForVisibleLocator(selector).click();
    }
}
