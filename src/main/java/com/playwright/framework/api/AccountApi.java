package com.playwright.framework.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.playwright.framework.models.CreateUserRequest;
import com.playwright.framework.models.GenerateTokenRequest;

import java.util.Map;

/**
 * Client for DemoQA account endpoints.
 */
public class AccountApi extends BaseApiClient {

    private static final String CREATE_USER = "/Account/v1/User";
    private static final String GENERATE_TOKEN = "/Account/v1/GenerateToken";
    private static final String AUTHORIZED = "/Account/v1/Authorized";
    private static final String USER_BY_ID = "/Account/v1/User/{UUID}";

    public AccountApi() {
        super();
    }

    public AccountApi(APIRequestContext requestContext) {
        super(requestContext);
    }

    public APIResponse createUser(CreateUserRequest request) {
        return post(CREATE_USER, request);
    }

    public APIResponse generateToken(GenerateTokenRequest request) {
        return post(GENERATE_TOKEN, request);
    }

    public APIResponse authorize(GenerateTokenRequest request) {
        return post(AUTHORIZED, request);
    }

    public APIResponse getUser(String userId, String token) {
        return get(USER_BY_ID, Map.of("UUID", userId), Map.of(), authorizationHeader(token));
    }

    public APIResponse deleteUser(String userId, String token) {
        return delete(USER_BY_ID, null, Map.of("UUID", userId), Map.of(), authorizationHeader(token));
    }

    private Map<String, String> authorizationHeader(String token) {
        return Map.of("Authorization", "Bearer " + token);
    }
}
