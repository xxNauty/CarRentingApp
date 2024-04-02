package com.example.carrentingapp.email.notifications.forgot_password;

public class ForgotPasswordTemplate {
    public static String template(String token) {
        return
                "<h1>Hello!</h1>" +
                        "<h2>If you want to reset your password, just copy this token: <b>" +
                        token +
                        "</b> and paste it in the password reset form.";
    }
}
