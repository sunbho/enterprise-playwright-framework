package com.playwright.framework.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.Assert;

/**
 * Shared assertion facade for framework and test code.
 */
public final class AssertionUtils {

    private AssertionUtils() {
        throw new IllegalStateException("AssertionUtils must not be instantiated");
    }

    public static void verifyEquals(String actual, String expected) {
        Assert.assertEquals(actual, expected);
    }

    public static void verifyTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

    public static void verifyFalse(boolean condition) {
        Assert.assertFalse(condition);
    }

    public static void verifyContains(String actual, String expected) {
        Assert.assertNotNull(actual, "Actual value must not be null");
        Assert.assertTrue(
                actual.contains(expected),
                "Expected '" + actual + "' to contain '" + expected + "'");
    }

    public static void verifyVisible(Page page, String locator) {
        PlaywrightAssertions.assertThat(page.locator(locator)).isVisible();
    }

    public static void verifyText(Page page, String locator, String expected) {
        PlaywrightAssertions.assertThat(page.locator(locator)).hasText(expected);
    }
}
