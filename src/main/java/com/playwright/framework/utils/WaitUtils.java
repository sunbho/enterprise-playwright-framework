package com.playwright.framework.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Centralized Playwright synchronization utilities.
 */
public final class WaitUtils {

    private WaitUtils() {
        throw new IllegalStateException("WaitUtils must not be instantiated");
    }

    public static void waitForVisible(Page page, String locator) {
        waitForState(page, locator, WaitForSelectorState.VISIBLE);
    }

    public static void waitForHidden(Page page, String locator) {
        waitForState(page, locator, WaitForSelectorState.HIDDEN);
    }

    public static void waitForAttached(Page page, String locator) {
        waitForState(page, locator, WaitForSelectorState.ATTACHED);
    }

    public static void waitForDetached(Page page, String locator) {
        waitForState(page, locator, WaitForSelectorState.DETACHED);
    }

    public static void waitForURL(Page page, String url) {
        page.waitForURL(url);
    }

    public static void waitForLoadState(Page page) {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /**
     * Uses Playwright's event-loop-aware delay without blocking with
     * {@link Thread#sleep(long)}.
     *
     * @param milliseconds delay in milliseconds
     */
    public static void waitForTimeout(int milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("milliseconds must be zero or greater");
        }
        try (com.microsoft.playwright.Playwright playwright =
                     com.microsoft.playwright.Playwright.create()) {
            playwright.selectors();
            Page page = playwright.chromium().launch().newPage();
            try {
                page.waitForTimeout(milliseconds);
            } finally {
                page.context().browser().close();
            }
        }
    }

    private static void waitForState(
            Page page, String locator, WaitForSelectorState state) {
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(state));
    }
}
