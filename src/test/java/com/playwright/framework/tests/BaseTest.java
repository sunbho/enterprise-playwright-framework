package com.playwright.framework.tests;

import com.microsoft.playwright.Page;
import com.playwright.framework.factory.PlaywrightFactory;
import com.playwright.framework.utils.AllureUtils;
import io.qameta.allure.Allure;
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
        PlaywrightFactory.initializeBrowser();
        page = PlaywrightFactory.getPage();

        Thread thread = Thread.currentThread();
        String threadInformation =
                "Thread-" + thread.threadId() + " (" + thread.getName() + ")";
        Allure.parameter("Execution Thread", threadInformation);
        AllureUtils.attachText("Thread Information", threadInformation);
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
