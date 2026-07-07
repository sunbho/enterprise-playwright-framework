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

public class DeleteUserTest extends BaseApiTest {

    @Test
    public void shouldDeleteUser() {
        AccountApi accountApi = new AccountApi();
        String userName = "delete_user_" + System.currentTimeMillis();
        String password = "SecurePass!123";

        APIResponse createResponse = accountApi.createUser(new CreateUserRequest(userName, password));
        ApiResponseValidator.verifyStatusCode(createResponse, 201);
        UserResponse user = accountApi.deserialize(createResponse, UserResponse.class);

        APIResponse tokenResponse = accountApi.generateToken(new GenerateTokenRequest(userName, password));
        GenerateTokenResponse token = accountApi.deserialize(tokenResponse, GenerateTokenResponse.class);

        APIResponse deleteResponse = accountApi.deleteUser(user.getUserID(), token.getToken());
        Assert.assertTrue(deleteResponse.status() == 200 || deleteResponse.status() == 204,
                "Delete user should return success status");
    }
}
