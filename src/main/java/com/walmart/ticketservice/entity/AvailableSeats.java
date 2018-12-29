package com.walmart.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This Entity class is to store the information of Seats Available and expose to client
 * created by Laxmi Kalyan Kistapuram on 12/29/18
 */
@Data
@AllArgsConstructor
public class AvailableSeats {

    private int seatsAvailable;
    private int occupancyAtVenue;

}
