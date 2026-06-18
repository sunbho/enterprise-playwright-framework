package com.playwright.framework.utils;

import com.microsoft.playwright.Page;
import com.playwright.framework.config.ConfigManager;
import io.qameta.allure.Allure;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;

/**
 * Provides Allure attachments and execution environment metadata.
 */
public final class AllureUtils {

    private static final Logger LOGGER = LoggerUtils.getLogger(AllureUtils.class);
    private static final Path ALLURE_RESULTS_DIRECTORY =
            Path.of(System.getProperty("allure.results.directory", "allure-results"));
    private static final String FRAMEWORK_NAME = "Playwright Java Automation Framework";
    private static final String FRAMEWORK_VERSION = "1.0-SNAPSHOT";

    private AllureUtils() {
        throw new IllegalStateException("AllureUtils must not be instantiated");
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", Objects.requireNonNullElse(content, ""));
    }

    public static byte[] attachScreenshot(Page page, String name) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment(
                name, "image/png", new ByteArrayInputStream(screenshot), ".png");
        return screenshot;
    }

    public static void attachPageSource(Page page, String name) {
        Allure.addAttachment(name, "text/html", page.content(), ".html");
    }

    /**
     * Writes environment metadata into the active Allure results directory and
     * also attaches it to the current Allure lifecycle when available.
     */
    public static void attachEnvironmentInfo() {
        ConfigManager config = ConfigManager.getInstance();
        Properties environment = new Properties();
        environment.setProperty("Browser", valueOrUnknown(config.getProperty("browser")));
        environment.setProperty("Headless Mode", valueOrUnknown(config.getProperty("headless")));
        environment.setProperty("Java Version", System.getProperty("java.version", "Unknown"));
        environment.setProperty("OS Name", System.getProperty("os.name", "Unknown"));
        environment.setProperty("OS Version", System.getProperty("os.version", "Unknown"));
        environment.setProperty("Framework Name", FRAMEWORK_NAME);
        environment.setProperty("Framework Version", FRAMEWORK_VERSION);
        environment.setProperty(
                "Execution Time",
                OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        try {
            Files.createDirectories(ALLURE_RESULTS_DIRECTORY);
            Path environmentFile = ALLURE_RESULTS_DIRECTORY.resolve("environment.properties");
            try (var output = Files.newOutputStream(environmentFile)) {
                environment.store(output, "Allure environment information");
            }

            StringWriter writer = new StringWriter();
            environment.store(writer, "Environment");
            attachText("Environment Information", writer.toString());
            LOGGER.info("Allure environment information written to {}", environmentFile);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write Allure environment information", exception);
        }
    }

    public static void attachFile(String name, String mediaType, Path file, String extension) {
        try (InputStream input = Files.newInputStream(file)) {
            Allure.addAttachment(name, mediaType, input, extension);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to attach file to Allure: " + file, exception);
        }
    }

    public static Path saveScreenshot(byte[] screenshot, String name) {
        Path path = ScreenshotUtils.createScreenshotPath(name);
        try {
            Files.createDirectories(path.getParent());
            Files.copy(
                    new ByteArrayInputStream(screenshot),
                    path,
                    StandardCopyOption.REPLACE_EXISTING);
            return path;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save screenshot: " + path, exception);
        }
    }

    private static String valueOrUnknown(String value) {
        return value == null || value.isBlank() ? "Unknown" : value;
    }
}
