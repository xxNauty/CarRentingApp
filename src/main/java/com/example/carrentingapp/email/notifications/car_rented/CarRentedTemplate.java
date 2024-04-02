package com.example.carrentingapp.email.notifications.car_rented;

import com.example.carrentingapp.car.CarBase;

public class CarRentedTemplate {
    public static String template(CarBase car) {
        return
                "<h1>Hello!</h1>" +
                        "<h2>You have just rented a car. Some info about this car: </h2>" +
                        "<table>" +
                        "<tr><th>Brand</th><th>" + car.getBrand() + "</th></tr>" +
                        "<tr><th>Model</th><th>" + car.getModel() + "</th></tr>" +
                        "<tr><th>Year of production</th><th>" + car.getYearOfProduction() + "</th></tr>" +
                        "<tr><th>Power</th><th>" + car.getPower() + "</th></tr>" +
                        "</table>" +
                        "<h2>We will send you an email when this car will be ready to pick</h2>";
    }
}
