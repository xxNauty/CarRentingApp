package com.example.carrentingapp.car.response;

import com.example.carrentingapp.car.CarBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarGetResponse {

    private CarBase car;

}
