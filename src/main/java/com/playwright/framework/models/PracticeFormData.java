package com.playwright.framework.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Test data contract for the DemoQA automation practice form.
 */
public class PracticeFormData {

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String mobile;
    private String subject;
    private List<String> hobbies = new ArrayList<>();
    private String address;
    private String state;
    private String city;
    private String picture;

    public PracticeFormData() {
    }

    public PracticeFormData(
            String firstName,
            String lastName,
            String email,
            String gender,
            String mobile,
            String subject,
            List<String> hobbies,
            String address,
            String state,
            String city,
            String picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.subject = subject;
        this.hobbies = hobbies;
        this.address = address;
        this.state = state;
        this.city = city;
        this.picture = picture;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = Objects.requireNonNullElseGet(hobbies, ArrayList::new);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "PracticeFormData{"
                + "firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", email='" + email + '\''
                + ", gender='" + gender + '\''
                + ", mobile='" + mobile + '\''
                + ", subject='" + subject + '\''
                + ", hobbies=" + hobbies
                + ", address='" + address + '\''
                + ", state='" + state + '\''
                + ", city='" + city + '\''
                + ", picture='" + picture + '\''
                + '}';
    }
}
