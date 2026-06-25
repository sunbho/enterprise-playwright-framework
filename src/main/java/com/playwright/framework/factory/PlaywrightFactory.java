package com.playwright.framework.factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.playwright.framework.config.ConfigManager;
import com.playwright.framework.utils.LoggerUtils;
import org.slf4j.Logger;

import java.util.Locale;

/**
 * Creates and manages Playwright resources independently for each execution
 * thread.
 *
 * <p>Each thread owns its {@link Playwright}, {@link Browser},
 * {@link BrowserContext}, and {@link Page}. This isolation allows test
 * execution to be parallelized without sharing mutable browser state.</p>
 */
public final class PlaywrightFactory {

    private static final Logger LOGGER = LoggerUtils.getLogger(PlaywrightFactory.class);
    private static final String BROWSER_PROPERTY = "browser";
    private static final String HEADLESS_PROPERTY = "headless";
    private static final String TIMEOUT_PROPERTY = "timeout"; // timeout in milliseconds

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> BROWSER_CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private PlaywrightFactory() {
        throw new IllegalStateException("PlaywrightFactory must not be instantiated");
    }

    /**
     * Initializes Playwright and creates a browser, isolated browser context,
     * and page for the current thread.
     *
     * <p>The browser name and headless mode are read from
     * {@code config.properties}. Supported browser values are
     * {@code chrome}, {@code firefox}, and {@code webkit}. Calling this method
     * more than once on the same thread reuses the existing initialized page.</p>
     *
     * <p>This method also reads a {@code timeout} property (milliseconds) from
     * {@link com.playwright.framework.config.ConfigManager} and applies it to
     * the created {@link BrowserContext} via {@code setDefaultTimeout}.</p>
     *
     * @return the page owned by the current thread
     * @throws IllegalArgumentException if browser, headless, or timeout configuration is invalid
     * @throws IllegalStateException if browser initialization fails
     */
    public static Page initializeBrowser() {
        Page existingPage = PAGE.get();
        if (existingPage != null) {
            LOGGER.info("{} Reusing existing page", threadLabel());
            return existingPage;
        }

        ConfigManager config = ConfigManager.getInstance();
        String browserName = requireProperty(config, BROWSER_PROPERTY).toLowerCase(Locale.ROOT);
        boolean headless = parseBoolean(requireProperty(config, HEADLESS_PROPERTY), HEADLESS_PROPERTY);
        boolean startMaximized =
                config.getBoolean("startMaximized");

        try {
            LOGGER.info("{} Launching {} browser (headless={})",
                    threadLabel(), browserName, headless);
            Playwright playwright = Playwright.create();
            PLAYWRIGHT.set(playwright);

            BrowserType browserType = selectBrowserType(playwright, browserName);
            Browser browser = browserType.launch(
                    new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(1000));
            BROWSER.set(browser);
            LOGGER.info("{} Browser Created", threadLabel());

            BrowserContext browserContext =
                    browser.newContext(
                            new Browser.NewContextOptions()
                                    .setViewportSize(1920, 1080)
                    );

            // Read timeout (in milliseconds) from config and apply to the BrowserContext
            int timeout = parseInt(requireProperty(config, TIMEOUT_PROPERTY), TIMEOUT_PROPERTY);
            // Playwright's setDefaultTimeout accepts a double (milliseconds)
            browserContext.setDefaultTimeout((double) timeout);

            BROWSER_CONTEXT.set(browserContext);

            Page page = browserContext.newPage();
            PAGE.set(page);
            LOGGER.info("{} Page Created", threadLabel());
            return page;
        } catch (RuntimeException exception) {
            LOGGER.error("{} Browser initialization failed for {}",
                    threadLabel(), browserName, exception);
            closeBrowser();
            throw new IllegalStateException(
                    "Failed to initialize Playwright browser '" + browserName + "'", exception);
        }
    }

    /**
     * Returns the Playwright page associated with the current thread.
     *
     * @return the current thread's page
     * @throws IllegalStateException if {@link #initializeBrowser()} has not been called
     */
    public static Playwright getPlaywright() {
        Playwright playwright = PLAYWRIGHT.get();
        if (playwright == null) {
            throw new IllegalStateException(
                    "Playwright is not initialized for the current thread.");
        }
        return playwright;
    }

    /**
     * Returns the Playwright page associated with the current thread.
     */
    public static Page getPage() {
        Page page = PAGE.get();
        if (page == null) {
            throw new IllegalStateException(
                    "Browser is not initialized for the current thread. "
                            + "Call initializeBrowser() before getPage().");
        }
        return page;
    }

    /**
     * Returns the browser associated with the current execution thread.
     *
     * @return current thread's browser
     * @throws IllegalStateException if browser initialization has not completed
     */
    public static Browser getBrowser() {
        Browser browser = BROWSER.get();
        if (browser == null) {
            throw new IllegalStateException(
                    "Browser is not initialized for the current thread.");
        }
        return browser;
    }

    /**
     * Returns the browser context associated with the current execution thread.
     *
     * @return current thread's browser context
     * @throws IllegalStateException if browser initialization has not completed
     */
    public static BrowserContext getBrowserContext() {
        BrowserContext browserContext = BROWSER_CONTEXT.get();
        if (browserContext == null) {
            throw new IllegalStateException(
                    "Browser context is not initialized for the current thread.");
        }
        return browserContext;
    }

    /**
     * Closes all Playwright resources owned by the current thread in reverse
     * creation order and removes their {@link ThreadLocal} references.
     *
     * <p>This method is idempotent and may be called safely during teardown
     * even when initialization was incomplete.</p>
     */
    public static void closeBrowser() {
        LOGGER.info("{} Closing browser resources", threadLabel());
        try {
            closeResource(PAGE.get(), "Page");
            closeResource(BROWSER_CONTEXT.get(), "Browser context");
            closeResource(BROWSER.get(), "Browser");
            closeResource(PLAYWRIGHT.get(), "Playwright");
            LOGGER.info("{} Browser Closed", threadLabel());
        } finally {
            PAGE.remove();
            BROWSER_CONTEXT.remove();
            BROWSER.remove();
            PLAYWRIGHT.remove();
            LOGGER.info("{} ThreadLocal Cleanup Completed", threadLabel());
        }
    }

    private static BrowserType selectBrowserType(Playwright playwright, String browserName) {
        return switch (browserName) {
            case "chrome" -> playwright.chromium();
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + browserName
                            + "'. Supported values are: chrome, firefox, webkit");
        };
    }

    private static String requireProperty(ConfigManager config, String key) {
        String value = config.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Required configuration property '" + key + "' is missing or blank");
        }
        return value.trim();
    }

    private static boolean parseBoolean(String value, String key) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new IllegalArgumentException(
                "Configuration property '" + key + "' must be either true or false");
    }

    private static int parseInt(String value, String key) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Configuration property '" + key + "' must be a valid integer (milliseconds)", exception);
        }
    }

    private static void closeResource(AutoCloseable resource, String resourceName) {
        if (resource == null) {
            return;
        }

        try {
            resource.close();
        } catch (Exception exception) {
            LOGGER.warn("{} Unable to close {} cleanly",
                    threadLabel(), resourceName, exception);
        }
    }
    private static String threadLabel() {
        Thread thread = Thread.currentThread();
        return "[Thread-" + thread.threadId() + ":" + thread.getName() + "]";
    }
}
