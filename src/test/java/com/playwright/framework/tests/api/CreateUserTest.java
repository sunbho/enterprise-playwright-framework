package com.playwright.framework.tests.api;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.playwright.framework.api.AccountApi;
import com.playwright.framework.api.ApiResponseValidator;
import com.playwright.framework.api.BookStoreApi;
import com.playwright.framework.config.ConfigManager;
import com.playwright.framework.factory.PlaywrightFactory;
import com.playwright.framework.models.*;
import com.playwright.framework.pages.BasePage;
import com.playwright.framework.pages.BookStoreLoginPage;
import com.playwright.framework.tests.BaseTest;
import com.playwright.framework.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CreateUserTest extends BaseTest {

    List<Isbn> collectionOfIsbns = List.of(
            new Isbn("9781449325862"),
            new Isbn("9781449331818"),
            new Isbn("9781449337711")
    );

    @Test
    public void shouldCreateUserAndReadProfile() {
        AccountApi accountApi = new AccountApi();
        String userName = "api_user_" + System.currentTimeMillis();
        String password = "SecurePass!123";

        APIResponse createResponse = accountApi.createUser(new CreateUserRequest(userName, password));
        ApiResponseValidator.verifyStatusCode(createResponse, 201);
        ApiResponseValidator.verifyJsonField(createResponse, "username", userName);
        UserResponse user = accountApi.deserialize(createResponse, UserResponse.class);
        String Id = user.getUserID();

        AddBooksRequest request = new AddBooksRequest(Id, collectionOfIsbns);

        APIResponse tokenResponse = accountApi.generateToken(new GenerateTokenRequest(userName, password));
        ApiResponseValidator.verifyStatusCode(tokenResponse, 200);
        GenerateTokenResponse token = accountApi.deserialize(tokenResponse, GenerateTokenResponse.class);

        try {
            APIResponse profileResponse = accountApi.getUser(user.getUserID(), token.getToken());
            ApiResponseValidator.verifyStatusCode(profileResponse, 200);
            ApiResponseValidator.verifyJsonField(profileResponse, "username", userName);
        } finally {
            APIResponse deleteResponse = accountApi.deleteUser(user.getUserID(), token.getToken());
            Assert.assertTrue(deleteResponse.status() == 200 || deleteResponse.status() == 204,
                    "User cleanup failed with status: " + deleteResponse.status());
        }
    }
    }

