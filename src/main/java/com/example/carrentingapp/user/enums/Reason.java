package com.example.carrentingapp.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Reason {
    FREQUENT_DELAYED_RETURNS("częste-opóźnienia-ze-zwrotami"),
    DAMAGED_CAR("uszkodzenie-samochodu"),
    DESTROYED_CAR("zniszczenie-samochodu"),
    OTHER("inne");

    private final String reason;



}
