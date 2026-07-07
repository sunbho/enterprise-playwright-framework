package com.playwright.framework.tests.api;

import com.microsoft.playwright.APIResponse;
import com.playwright.framework.api.ApiResponseValidator;
import com.playwright.framework.api.BookStoreApi;
import com.playwright.framework.models.BooksResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetBooksTest extends BaseApiTest {

    @Test
    public void shouldGetBookStoreInventory() {
        BookStoreApi bookStoreApi = new BookStoreApi();

        APIResponse response = bookStoreApi.getBooks();
        ApiResponseValidator.verifyStatusCode(response, 200);
        ApiResponseValidator.verifyBodyContains(response, "Git Pocket Guide");

        BooksResponse booksResponse = bookStoreApi.deserialize(response, BooksResponse.class);
        for (int i = 0; i < booksResponse.getBooks().size(); i++) {
            System.out.println(booksResponse.getBooks().get(i).getIsbn());
        }
        Assert.assertFalse(booksResponse.getBooks().isEmpty(), "Book inventory should not be empty");
    }
}
