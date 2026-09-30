package com.Taskmanager.dto;
//This is login request Dto
/*
 Why DTO is used ?
 - DTOs are used to transfer only the required data between the client and the server.
 - They improve security by hiding sensitive fields like passwords, prevent clients from
 - modifying fields they shouldn't (such as roles), reduce the amount of data transferred,
 - and keep the API independent of the database entity structure.
*
*/
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

