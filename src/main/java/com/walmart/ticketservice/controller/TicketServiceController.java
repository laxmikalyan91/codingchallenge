package com.walmart.ticketservice.controller;

import com.walmart.ticketservice.entity.BestSeatHold;
import com.walmart.ticketservice.entity.AvailableSeats;
import com.walmart.ticketservice.entity.ReservedSeats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  TicketServiceController Interface to handle http requests from the user.
 *  created by Laxmi Kalyan Kistapuram on 12/28/18
 */
@RestController
@RequestMapping("/walmart/ticket-service")
public interface TicketServiceController {

    /**
     * Number of seats with in the venue that are neither held nor reserved
     * @return the number of seats available in the venue
     */
    @GetMapping(value = "/availableseats")
    ResponseEntity<AvailableSeats> availableSeatsWithInVenue();


    /**
     * Find and hold the best available seats for the customer
     * @param numOfSeats
     * @param customerEmail
     * @return BestSeatHold object with seats held and customer information
     */
    @GetMapping(value = "/findandholdbestseats")
    ResponseEntity<BestSeatHold> findAndHoldBestSeats(@RequestParam("numOfSeats") int numOfSeats,
                                                      @RequestParam("customerEmail") String customerEmail);


    /**
     * Reserve seats held for a specific customer
     *
     * @param holdId identifier for the seat held
     * @param customerEmail customer email for the assigned seats held
     * @return confirmation code for reserved seats
     */
    @GetMapping(value = "/reserve-seats")
    ResponseEntity<ReservedSeats> reserveSeats(@RequestParam("holdId") int holdId,
                                               @RequestParam("customerEmail") String customerEmail);

}
