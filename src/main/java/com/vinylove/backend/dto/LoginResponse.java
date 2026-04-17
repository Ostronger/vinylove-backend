package com.vinylove.backend.dto;

import com.vinylove.backend.entity.Role;

public class LoginResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String message; 

    public LoginResponse() { // Constructeur par défaut
    }

    public LoginResponse(Long id, String email, String firstName, String lastName, Role role, String message) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
