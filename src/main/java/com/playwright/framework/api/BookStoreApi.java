package com.playwright.framework.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;

import java.util.Map;

/**
 * Client for DemoQA BookStore endpoints.
 */
public class BookStoreApi extends BaseApiClient {

    private static final String BOOKS = "/BookStore/v1/Books";
    private static final String BOOK = "/BookStore/v1/Book";

    public BookStoreApi() {
        super();
    }

    public BookStoreApi(APIRequestContext requestContext) {
        super(requestContext);
    }

    public APIResponse getBooks() {
        return get(BOOKS);
    }

    public APIResponse addBooks(Object request, String token) {
        return post(BOOKS, request, Map.of(), Map.of(), authorizationHeader(token));
    }

    public APIResponse addBooks(Object request) {
        return post(BOOKS, request);
    }

    public APIResponse deleteBook(Object request, String token) {
        return delete(BOOK, request, Map.of(), Map.of(), authorizationHeader(token));
    }

    public APIResponse updateBook(Object request, String token) {
        return put(BOOKS, request, Map.of(), Map.of(), authorizationHeader(token));
    }

//    private Map<String, String> authorizationHeader(String token) {
//        return Map.of("Authorization", "Bearer " + token);
//    }

    private Map<String, String> authorizationHeader(String token) {
        if (token == null || token.isBlank()) {
            return Map.of();
        }
        return Map.of("Authorization", "Bearer " + token);
    }
}
