package com.playwright.framework.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.playwright.APIResponse;
import com.playwright.framework.exceptions.ApiException;
import org.testng.Assert;

import java.util.Locale;

/**
 * Reusable API response assertions.
 */
public final class ApiResponseValidator {

    private ApiResponseValidator() {
        throw new IllegalStateException("ApiResponseValidator must not be instantiated");
    }

    public static void verifyStatusCode(APIResponse response, int expectedStatusCode) {
        Assert.assertEquals(response.status(), expectedStatusCode, "Unexpected API status code");
    }

    public static void verifyResponseTime(long actualTimeMillis, long maxTimeMillis) {
        Assert.assertTrue(actualTimeMillis <= maxTimeMillis,
                "Expected response time <= " + maxTimeMillis + " ms but was " + actualTimeMillis + " ms");
    }

    public static void verifyJsonField(APIResponse response, String fieldName, Object expectedValue) {
        try {
            JsonNode root = BaseApiClient.OBJECT_MAPPER.readTree(response.text());
            JsonNode value = root.at(toJsonPointer(fieldName));
            Assert.assertFalse(value.isMissingNode(), "JSON field was not found: " + fieldName);
            Assert.assertEquals(value.asText(), String.valueOf(expectedValue), "Unexpected JSON field value");
        } catch (Exception exception) {
            throw new ApiException("Unable to verify JSON field: " + fieldName, exception);
        }
    }

    public static void verifyHeader(APIResponse response, String headerName, String expectedValue) {
        String actualValue = response.headers().get(headerName.toLowerCase(Locale.ROOT));
        if (actualValue == null) {
            actualValue = response.headers().get(headerName);
        }
        Assert.assertEquals(actualValue, expectedValue, "Unexpected response header value");
    }

    public static void verifyBodyContains(APIResponse response, String expectedText) {
        Assert.assertTrue(response.text().contains(expectedText),
                "Response body did not contain expected text: " + expectedText);
    }

    private static String toJsonPointer(String fieldName) {
        if (fieldName.startsWith("/")) {
            return fieldName;
        }
        return "/" + fieldName.replace(".", "/");
    }
}
