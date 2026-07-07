package com.playwright.framework.models;

public class UpdateBookRequest {

    private String userId;
    private String isbn;

    public UpdateBookRequest() {
    }

    public UpdateBookRequest(String userId, String isbn) {
        this.userId = userId;
        this.isbn = isbn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
