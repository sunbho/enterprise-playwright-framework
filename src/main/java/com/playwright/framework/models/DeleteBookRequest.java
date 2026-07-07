package com.playwright.framework.models;

public class DeleteBookRequest {

    private String isbn;
    private String userId;

    public DeleteBookRequest() {
    }

    public DeleteBookRequest(String isbn, String userId) {
        this.isbn = isbn;
        this.userId = userId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
