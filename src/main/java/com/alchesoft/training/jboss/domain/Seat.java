package com.alchesoft.training.jboss.domain;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Seat {

    private final int id;
    private final String seat;
    private final int price;

    private boolean booked;
}
