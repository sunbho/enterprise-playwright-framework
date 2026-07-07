package com.playwright.framework.tests.api;

import com.microsoft.playwright.APIResponse;
import com.playwright.framework.api.AccountApi;
import com.playwright.framework.api.ApiResponseValidator;
import com.playwright.framework.models.CreateUserRequest;
import com.playwright.framework.models.GenerateTokenRequest;
import com.playwright.framework.models.GenerateTokenResponse;
import com.playwright.framework.models.UserResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GenerateTokenTest extends BaseApiTest {

    @Test
    public void shouldGenerateTokenForValidUser() {
        AccountApi accountApi = new AccountApi();
        String userName = "token_user_" + System.currentTimeMillis();
        String password = "SecurePass!123";

        APIResponse createResponse = accountApi.createUser(new CreateUserRequest(userName, password));
        ApiResponseValidator.verifyStatusCode(createResponse, 201);
        UserResponse user = accountApi.deserialize(createResponse, UserResponse.class);

        APIResponse tokenResponse = accountApi.generateToken(new GenerateTokenRequest(userName, password));
        ApiResponseValidator.verifyStatusCode(tokenResponse, 200);
        GenerateTokenResponse token = accountApi.deserialize(tokenResponse, GenerateTokenResponse.class);

        try {
            Assert.assertNotNull(token.getToken(), "Generated token should not be null");
            Assert.assertFalse(token.getToken().isBlank(), "Generated token should not be blank");
            ApiResponseValidator.verifyJsonField(tokenResponse, "status", "Success");
        } finally {
            accountApi.deleteUser(user.getUserID(), token.getToken());
        }
    }
}
