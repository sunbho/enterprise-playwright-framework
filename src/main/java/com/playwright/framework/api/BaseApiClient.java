package com.playwright.framework.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.playwright.framework.exceptions.ApiException;
import com.playwright.framework.utils.AllureUtils;
import com.playwright.framework.utils.LoggerUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Objects;

/**
 * Base client for reusable Playwright API operations.
 */
public abstract class BaseApiClient {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected final Logger logger;
    private final APIRequestContext requestContext;

    protected BaseApiClient() {
        this(ApiFactory.getContext());
    }

    protected BaseApiClient(APIRequestContext requestContext) {
        this.requestContext = Objects.requireNonNull(requestContext, "requestContext must not be null");
        this.logger = LoggerUtils.getLogger(getClass());
    }

    protected APIResponse get(String endpoint) {
        return get(endpoint, Map.of(), Map.of(), Map.of());
    }

    protected APIResponse get(String endpoint, Map<String, ?> pathParams, Map<String, ?> queryParams,
                              Map<String, String> headers) {
        String resolvedEndpoint = resolvePath(endpoint, pathParams);
        RequestOptions options = requestOptions(null, queryParams, headers);
        return execute("GET", resolvedEndpoint, null, options);
    }

    protected APIResponse post(String endpoint, Object body) {
        return post(endpoint, body, Map.of(), Map.of(), Map.of());
    }

    protected APIResponse post(String endpoint, Object body, Map<String, ?> pathParams,
                               Map<String, ?> queryParams, Map<String, String> headers) {
        String resolvedEndpoint = resolvePath(endpoint, pathParams);
        RequestOptions options = requestOptions(body, queryParams, headers);
        return execute("POST", resolvedEndpoint, body, options);
    }

    protected APIResponse put(String endpoint, Object body) {
        return put(endpoint, body, Map.of(), Map.of(), Map.of());
    }

    protected APIResponse put(String endpoint, Object body, Map<String, ?> pathParams,
                              Map<String, ?> queryParams, Map<String, String> headers) {
        String resolvedEndpoint = resolvePath(endpoint, pathParams);
        RequestOptions options = requestOptions(body, queryParams, headers);
        return execute("PUT", resolvedEndpoint, body, options);
    }

    protected APIResponse delete(String endpoint) {
        return delete(endpoint, null, Map.of(), Map.of(), Map.of());
    }

    protected APIResponse delete(String endpoint, Object body, Map<String, ?> pathParams,
                                 Map<String, ?> queryParams, Map<String, String> headers) {
        String resolvedEndpoint = resolvePath(endpoint, pathParams);
        RequestOptions options = requestOptions(body, queryParams, headers);
        return execute("DELETE", resolvedEndpoint, body, options);
    }

    public <T> T deserialize(APIResponse response, Class<T> responseType) {
        try {
            return OBJECT_MAPPER.readValue(response.text(), responseType);
        } catch (JsonProcessingException exception) {
            throw new ApiException("Unable to deserialize API response to " + responseType.getSimpleName(), exception);
        }
    }

    private APIResponse execute(String method, String endpoint, Object body, RequestOptions options) {
        long startTime = System.nanoTime();
        try {
            logRequest(method, endpoint, options, body);
            APIResponse response = switch (method) {
                case "GET" -> requestContext.get(endpoint, options);
                case "POST" -> requestContext.post(endpoint, options);
                case "PUT" -> requestContext.put(endpoint, options);
                case "DELETE" -> requestContext.delete(endpoint, options);
                default -> throw new ApiException("Unsupported API method: " + method);
            };
            long responseTime = elapsedMillis(startTime);
            logResponse(response, responseTime);
            attachToAllure(method, endpoint, body, response, responseTime);
            return response;
        } catch (RuntimeException exception) {
            throw new ApiException("API " + method + " request failed for endpoint: " + endpoint, exception);
        }
    }

    private RequestOptions requestOptions(Object body, Map<String, ?> queryParams, Map<String, String> headers) {
        RequestOptions options = RequestOptions.create();
        if (body != null) {
            options.setData(body);
        }
        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                if (value != null) {
                    options.setQueryParam(key, String.valueOf(value));
                }
            });
        }
        if (headers != null) {
            headers.forEach(options::setHeader);
        }
        return options;
    }

    private String resolvePath(String endpoint, Map<String, ?> pathParams) {
        String resolvedEndpoint = endpoint;
        if (pathParams != null) {
            for (Map.Entry<String, ?> entry : pathParams.entrySet()) {
                resolvedEndpoint = resolvedEndpoint.replace(
                        "{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }
        return resolvedEndpoint;
    }

    private void logRequest(String method, String endpoint, RequestOptions options, Object body) {
        logger.info("API Request: method={}, url={}, body={}", method, endpoint, toJson(body));
        AllureUtils.attachText("API Request - " + method + " " + endpoint,
                "Method: " + method + System.lineSeparator()
                        + "URL: " + endpoint + System.lineSeparator()
                        + "Body: " + toJson(body));
    }

    private void logResponse(APIResponse response, long responseTime) {
        logger.info("API Response: status={}, time={} ms, body={}",
                response.status(), responseTime, response.text());
    }

    private void attachToAllure(String method, String endpoint, Object body, APIResponse response, long responseTime) {
        String content = "Request Method: " + method + System.lineSeparator()
                + "Request URL: " + endpoint + System.lineSeparator()
                + "Request Body: " + toJson(body) + System.lineSeparator()
                + "Response Status: " + response.status() + System.lineSeparator()
                + "Response Time: " + responseTime + " ms" + System.lineSeparator()
                + "Response Headers: " + response.headers() + System.lineSeparator()
                + "Response Body: " + response.text();
        AllureUtils.attachText("API Exchange - " + method + " " + endpoint, content);
    }

    private String toJson(Object value) {
        if (value == null) {
            return "";
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            return String.valueOf(value);
        }
    }

    private long elapsedMillis(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000;
    }
}
