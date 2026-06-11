package com.playwright.framework.tests;

import com.microsoft.playwright.Page;
import com.playwright.framework.factory.PlaywrightFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Provides browser lifecycle management for Playwright TestNG tests.
 */
public abstract class BaseTest {

    protected Page page;

    /**
     * Creates an isolated browser page for the current test method.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        page = PlaywrightFactory.initializeBrowser();
    }

    /**
     * Closes browser resources associated with the current test thread.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            PlaywrightFactory.closeBrowser();
        } finally {
            page = null;
        }
    }
}
