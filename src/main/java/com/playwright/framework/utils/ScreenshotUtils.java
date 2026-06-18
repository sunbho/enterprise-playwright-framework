package com.playwright.framework.utils;

import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures timestamped Playwright screenshots.
 */
public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIRECTORY = Path.of("screenshots");
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
        throw new IllegalStateException("ScreenshotUtils must not be instantiated");
    }

    /**
     * Captures a full-page screenshot under the project screenshots directory.
     *
     * @param page active Playwright page
     * @param screenshotName descriptive screenshot name
     * @return path of the captured screenshot
     */
    public static Path capture(Page page, String screenshotName) {
        Path screenshotPath = createScreenshotPath(screenshotName);
        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create screenshot directory", exception);
        }

        page.screenshot(new Page.ScreenshotOptions()
                .setPath(screenshotPath)
                .setFullPage(true));
        return screenshotPath;
    }

    static Path createScreenshotPath(String screenshotName) {
        String safeName = screenshotName == null || screenshotName.isBlank()
                ? "screenshot"
                : screenshotName.replaceAll("[^a-zA-Z0-9._-]", "_");
        return SCREENSHOT_DIRECTORY.resolve(
                safeName + "_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".png");
    }
}
