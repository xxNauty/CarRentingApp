package com.example.carrentingapp.email.notifications.rent_going_to_finish;

public class RentGoingToFinishTemplate {
    public static String template() {
        return
                "<h1>Hello</h1>" +
                        "<h2>Tomorrow is the deadline for the return of the car you have rented from us. " +
                        "Remember to return it on time, because for every day of delay your rank drops down";
    }
}
