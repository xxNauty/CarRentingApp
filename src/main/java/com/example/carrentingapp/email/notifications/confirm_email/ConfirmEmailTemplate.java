package com.example.carrentingapp.email.notifications.confirm_email;

public class ConfirmEmailTemplate {
    public static String template(String token){
        final String url = "http://localhost:8080/api/v1/auth/register/verify?token=" + token;
        return
                "<h1>Confirm your email address</h1>" +
                "<h4>Click <a href='"+url+"'>here</a> to confirm your email address." +
                "</br>If the link doesn't work,</br> go to "+url+"</h4>";
    }
}
