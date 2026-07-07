package com.playwright.framework.tests.api;

import com.playwright.framework.api.ApiFactory;
import com.playwright.framework.api.TokenManager;
import io.qameta.allure.Allure;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseApiTest {

    @BeforeMethod(alwaysRun = true)
    public void setUpApi() {
        ApiFactory.createContext();
        Thread thread = Thread.currentThread();
        Allure.parameter("Execution Thread", "Thread-" + thread.threadId() + " (" + thread.getName() + ")");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownApi() {
        TokenManager.clearToken();
        ApiFactory.closeContext();
    }
}
