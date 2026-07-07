package com.playwright.framework.pages;

import com.microsoft.playwright.Page;

/**
 * Page object for the DemoQA Book Store login and profile pages.
 */
public class BookStoreLoginPage extends BasePage {

    private static final String LOGIN_URL = "/login";
    private static final String USERNAME_INPUT = "#userName";
    private static final String PASSWORD_INPUT = "#password";
    private static final String LOGIN_BUTTON = "#login";
    private static final String USER_NAME_LABEL = "#userName-value";
    private static final String BOOK_BY_NAME = "a:has-text('%s')";
    private static final String LOGOUT = "button:has-text('Logout')";
    ;

    public BookStoreLoginPage(Page page) {
        super(page);
    }

    public void open(String baseUrl) {
        navigate(baseUrl + LOGIN_URL);
        waitForPageLoad();
    }

    public void login(String userName, String password) {
        fillWhenVisible(USERNAME_INPUT, userName);
        fillWhenVisible(PASSWORD_INPUT, password);
        clickWhenVisible(LOGIN_BUTTON);
    }

    public String profileUserName() {
        return waitForVisibleLocator(USER_NAME_LABEL).innerText();
    }

    private String buildBookName(String bookName) {
        logger.debug("Building locator for book: {}", bookName);
        return String.format(BOOK_BY_NAME, bookName);
    }

    public void clickBook(String bookName) {
        click(buildBookName(bookName));
    }

    public boolean isBookPresentByName(String bookName) {
        return isVisible(buildBookName(bookName));
    }

   public void logout() {
        clickWhenVisible(LOGOUT);
    }

}
