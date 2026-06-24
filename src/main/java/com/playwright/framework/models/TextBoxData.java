package com.playwright.framework.models;

public class TextBoxData {
    private String fullName;
    private String email;
    private String address;
    private String permanentAddress;

    public TextBoxData() {
    }

    public TextBoxData(
            String fullName,
            String email,
            String address,
            String permanentAddress) {
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.permanentAddress = permanentAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName) {
        this.fullName = firstName;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    @Override
    public String toString() {
        return "TextBoxData{"
                + "fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + ", address='" + address + '\''
                + ", permanentAddress='" + permanentAddress + '\''
                + '}';
    }

}
