package com.walmart.ticketservice.services;

import com.walmart.ticketservice.entity.BestSeatHold;

public interface TicketService
{

    /**
     * Number of seats with in the venue that are neither held nor reserved
     * @return the number of seats available in the venue
     */
    int availableSeatsWithInVenue();


    /**
     * Find and hold the best available seats for the customer
     * @param numOfSeats
     * @param customerEmail
     * @return BestSeatHold object with seats held and customer information
     */
        BestSeatHold findAndHoldBestSeats(int numOfSeats, String customerEmail);

        /**
         * Reserve seats held for a specific customer
         *
         * @param holdId identifier for the seat held
         * @param customerEmail customer email for the assigned seats held
         * @return confirmation code for reserved seats
         */
        String reservedSeats(int holdId, String customerEmail);

    }
