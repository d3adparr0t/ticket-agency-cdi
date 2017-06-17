package com.alchesoft.training.jboss.domain;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class Seat implements Serializable {

    private final int id;
    private final String name;
    private final int price;

    private boolean booked;
}
