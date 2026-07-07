package com.playwright.framework.api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.playwright.framework.config.ConfigManager;
import com.playwright.framework.exceptions.ApiException;
import com.playwright.framework.utils.LoggerUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates and manages Playwright API request contexts per execution thread.
 */
public final class ApiFactory {

    private static final Logger LOGGER = LoggerUtils.getLogger(ApiFactory.class);
    private static final String API_BASE_URL_PROPERTY = "api.baseUrl";
    private static final String UI_BASE_URL_PROPERTY = "baseUrl";
    private static final String API_TIMEOUT_PROPERTY = "api.timeout";
    private static final String TIMEOUT_PROPERTY = "timeout";

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<APIRequestContext> REQUEST_CONTEXT = new ThreadLocal<>();

    private ApiFactory() {
        throw new IllegalStateException("ApiFactory must not be instantiated");
    }

    public static APIRequestContext createContext() {
        return createContext(Map.of(), null);
    }

    public static APIRequestContext createContext(Map<String, String> headers) {
        return createContext(headers, null);
    }

    public static APIRequestContext createContext(String bearerToken) {
        return createContext(Map.of(), bearerToken);
    }

    public static APIRequestContext createContext(Map<String, String> headers, String bearerToken) {
        APIRequestContext existingContext = REQUEST_CONTEXT.get();
        if (existingContext != null) {
            return existingContext;
        }

        try {
            Playwright playwright = Playwright.create();
            PLAYWRIGHT.set(playwright);

            Map<String, String> requestHeaders = defaultHeaders();
            if (headers != null) {
                requestHeaders.putAll(headers);
            }
            if (bearerToken != null && !bearerToken.isBlank()) {
                requestHeaders.put("Authorization", "Bearer " + bearerToken);
            }

            APIRequest.NewContextOptions options = new APIRequest.NewContextOptions()
                    .setBaseURL(baseUrl())
                    .setExtraHTTPHeaders(requestHeaders)
                    .setTimeout(timeout());

            APIRequestContext context = playwright.request().newContext(options);
            REQUEST_CONTEXT.set(context);
            LOGGER.info("{} API request context created for {}", threadLabel(), baseUrl());
            return context;
        } catch (RuntimeException exception) {
            closeContext();
            throw new ApiException("Unable to create API request context", exception);
        }
    }

    public static APIRequestContext getContext() {
        APIRequestContext context = REQUEST_CONTEXT.get();
        return context == null ? createContext() : context;
    }

    public static void closeContext() {
        LOGGER.info("{} Closing API request context", threadLabel());
        try {
            APIRequestContext context = REQUEST_CONTEXT.get();
            if (context != null) {
                context.dispose();
            }
        } finally {
            REQUEST_CONTEXT.remove();
            Playwright playwright = PLAYWRIGHT.get();
            if (playwright != null) {
                playwright.close();
            }
            PLAYWRIGHT.remove();
        }
    }

    private static Map<String, String> defaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private static String baseUrl() {
        ConfigManager config = ConfigManager.getInstance();
        String apiBaseUrl = config.getProperty(API_BASE_URL_PROPERTY);
        if (apiBaseUrl == null || apiBaseUrl.isBlank()) {
            apiBaseUrl = config.getProperty(UI_BASE_URL_PROPERTY);
        }
        if (apiBaseUrl == null || apiBaseUrl.isBlank()) {
            throw new IllegalArgumentException("API base URL is not configured");
        }
        return apiBaseUrl.trim();
    }

    private static double timeout() {
        ConfigManager config = ConfigManager.getInstance();
        String value = config.getProperty(API_TIMEOUT_PROPERTY);
        if (value == null || value.isBlank()) {
            value = config.getProperty(TIMEOUT_PROPERTY);
        }
        return Double.parseDouble(value);
    }

    private static String threadLabel() {
        Thread thread = Thread.currentThread();
        return "[Thread-" + thread.threadId() + ":" + thread.getName() + "]";
    }
}
