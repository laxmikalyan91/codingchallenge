package com.walmart.ticketservice.controller;

import com.walmart.ticketservice.entity.BestSeatHold;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walmart/ticket-service")
public interface TicketServiceController {

    /**
     * Number of seats with in the venue that are neither held nor reserved
     * @return the number of seats available in the venue
     */

    @GetMapping(value = "/availableseats")
    int availableSeatsWithInVenue();



    /**
     * Find and hold the best available seats for the customer
     * @param numOfSeats
     * @param customerEmail
     * @return BestSeatHold object with seats held and customer information
     */
    @GetMapping(value = "/findandholdbestseats")
    public BestSeatHold findAndHoldBestSeats(@RequestParam("numOfSeats") int numOfSeats,
                                             @RequestParam("customerEmail") String customerEmail);


    /**
     * Reserve seats held for a specific customer
     *
     * @param holdId identifier for the seat held
     * @param customerEmail customer email for the assigned seats held
     * @return confirmation code for reserved seats
     */

    @GetMapping(value = "/reserveseats")
    public String reserveSeats(@RequestParam("holdId") int holdId,
                                       @RequestParam("customerEmail") String customerEmail);

}
