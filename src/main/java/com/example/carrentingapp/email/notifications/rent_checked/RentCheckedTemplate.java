package com.example.carrentingapp.email.notifications.rent_checked;

import com.example.carrentingapp.exception.exception.http_error_500.InvalidInternalArgumentException;
import com.example.carrentingapp.rent.CarRent;

public class RentCheckedTemplate {

    public static String template(String endOfRentStatus, int daysOfDelay){
        String template = "<h1>Hello!</h1><h2>We have checked the car you have rented recently. ";

        switch (endOfRentStatus){
            case "CAR_RENT_END_OF_RENT_OK":
                template += "Everything is all with it so your rank increases by 0.5 point.<br>Thank you for choosing our site.</h2>";
                break;

            case "CAR_RENT_END_OF_RENT_DAMAGED_CAR":
                template += "Unfortunately we found some damages so your rank drops by 1 point.<br>Please, take care of cars you rent and do not damage them</h2>";
                break;

            case "CAR_RENT_END_OF_RENT_LATE":
                template += "You have returned it " + daysOfDelay + " days late. For every day of delay your rank drops by 0.2 point" +
                        "<br>Please, return car on time, because after fifth time your account will be locked for a month";
                break;

            default:
                throw new InvalidInternalArgumentException("Unsupported value");
        }

        return template;
    }
}
