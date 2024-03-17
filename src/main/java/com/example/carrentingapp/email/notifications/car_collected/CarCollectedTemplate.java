package com.example.carrentingapp.email.notifications.car_collected;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.rent.CarRent;

public class CarCollectedTemplate {
    public static String template(CarRent rent){
        return
                "<h1>Hello!</h1>" +
                        "<h2>You already collected car You have rented. Return it at " +
                        rent.getRentedTo() +
                        "</h2><h4>Remember! Return car at time, because every delay results in rank drop</h4>";

    }
}
