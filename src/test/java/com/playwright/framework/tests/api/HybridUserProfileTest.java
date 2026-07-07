package com.playwright.framework.tests.api;

import com.microsoft.playwright.APIResponse;
import com.playwright.framework.api.AccountApi;
import com.playwright.framework.api.ApiFactory;
import com.playwright.framework.api.ApiResponseValidator;
import com.playwright.framework.api.BookStoreApi;
import com.playwright.framework.config.ConfigManager;
import com.playwright.framework.models.*;
import com.playwright.framework.pages.BookStoreLoginPage;
import com.playwright.framework.tests.BaseTest;
import com.playwright.framework.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class HybridUserProfileTest extends BaseTest {

    private AccountApi accountApi;

    List<Isbn> collectionOfIsbns = List.of(
            new Isbn("9781449325862"),
            new Isbn("9781449331818"),
            new Isbn("9781449337711")
    );

    @BeforeMethod(alwaysRun = true)
    public void setUpApiContext() {
        ApiFactory.createContext();
        accountApi = new AccountApi();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownApiContext() {
        ApiFactory.closeContext();
    }

    @Test
    public void shouldCreateLoginVerifyAndDeleteUser() {
        String userName = "hybrid_user_" + System.currentTimeMillis();
        String password = "SecurePass!123";
        UserResponse user = null;
        GenerateTokenResponse token = null;

        try {
            APIResponse createResponse = accountApi.createUser(new CreateUserRequest(userName, password));
            ApiResponseValidator.verifyStatusCode(createResponse, 201);
            user = accountApi.deserialize(createResponse, UserResponse.class);

            APIResponse tokenResponse = accountApi.generateToken(new GenerateTokenRequest(userName, password));
            ApiResponseValidator.verifyStatusCode(tokenResponse, 200);
            token = accountApi.deserialize(tokenResponse, GenerateTokenResponse.class);

            AddBooksRequest request = new AddBooksRequest(user.getUserID(), collectionOfIsbns);
            BookStoreApi bookStoreApi = new BookStoreApi();
            bookStoreApi.addBooks(request,token.getToken());

            BookStoreLoginPage loginPage = new BookStoreLoginPage(page);
            loginPage.open(ConfigManager.getInstance().getProperty("api.baseUrl"));

            loginPage.login(userName, password);
            Assert.assertEquals(loginPage.profileUserName(), userName, "Profile user name should match API user");
            loginPage.isBookPresentByName("Git Pocket Guide");
            loginPage.logout();
        } finally {
            if (user != null && token != null && token.getToken() != null) {
                accountApi.deleteUser(user.getUserID(), token.getToken());
            }
        }
    }
}
