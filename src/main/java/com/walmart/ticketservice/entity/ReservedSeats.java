package com.walmart.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/**
 * This ReservedSeats Entity class is to store the information of Reserved Seats and expose to user
 * created by Laxmi Kalyan Kistapuram on 12/29/18
 */
@Data
@AllArgsConstructor
public class ReservedSeats {

    private List seatsList;
    private int holdId;
    private String confirmationNumber;
    private String customerEmail;
}
