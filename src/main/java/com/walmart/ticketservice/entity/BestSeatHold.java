package com.walmart.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class BestSeatHold {

    private List<Seats> seatsHeld;
    private String customerEmail;
    private Instant time;
    private int holdId;

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public BestSeatHold()
    {
        this.holdId = atomicInteger.getAndIncrement();
    }





}


