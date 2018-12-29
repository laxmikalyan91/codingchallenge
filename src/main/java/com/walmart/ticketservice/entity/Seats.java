package com.walmart.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Seats {

    private int rowNum;
    private int columnNum;
    private SeatStatus seatStatus;



}
