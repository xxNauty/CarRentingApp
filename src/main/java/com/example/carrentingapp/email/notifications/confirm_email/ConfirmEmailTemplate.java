package com.example.carrentingapp.email.notifications.confirm_email;

public class ConfirmEmailTemplate {
    public static String template(String token){
        final String url = "http://localhost:8080/api/v1/auth/register/verify?token=" + token;
        return
                "<h1>Hello!</h1>" +
                        "<h2>Account created successfully, click <a href='"+url+"'>here</a> to confirm your email address.</h2>" +
                        "<h6>If the link doesn't work, go to "+url+"</h6>";
    }
}
