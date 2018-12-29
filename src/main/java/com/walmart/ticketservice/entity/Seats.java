package com.walmart.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Seats Entity to store sets Info
 * created by Laxmi Kalyan Kistapuram on 12/28/18
 *
 */
@Data
@AllArgsConstructor
public class Seats {

    private int rowNum;
    private int columnNum;
    private SeatStatus seatStatus;



}
