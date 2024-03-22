package com.example.carrentingapp.car.response;

import com.example.carrentingapp.car.BaseCar;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCarResponse {

    private BaseCar car;

}
