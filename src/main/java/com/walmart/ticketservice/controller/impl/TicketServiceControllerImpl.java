package com.walmart.ticketservice.controller.impl;

import com.walmart.ticketservice.constants.TicketServiceConstants;
import com.walmart.ticketservice.controller.TicketServiceController;
import com.walmart.ticketservice.entity.BestSeatHold;
import com.walmart.ticketservice.entity.SeatStatus;
import com.walmart.ticketservice.entity.Seats;
import com.walmart.ticketservice.entity.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TicketServiceControllerImpl implements TicketServiceController {

    private int seatsAvailable;

    @Autowired
    private Venue venue;

    private Map<Integer, BestSeatHold> bestSeatHoldMap;


    @Autowired
      TicketServiceControllerImpl(Venue venue)
    {
        super();
        this.venue = venue;
        this.venue.seatsInitailization(TicketServiceConstants.NUM_OF_ROWS,TicketServiceConstants.NUM_OF_COLUMNS);
        this.seatsAvailable = venue.getOccupancy();
        bestSeatHoldMap = new ConcurrentHashMap();
    }


    /**
     *  Remove the expired hold seats from the bestSeatHoldMap if it exceeds the max Seat hold time.
     */
    private void removeExpiredHoldSeats()
    {
        if(!CollectionUtils.isEmpty(bestSeatHoldMap))
        {
            for(Map.Entry<Integer,BestSeatHold> entry : bestSeatHoldMap.entrySet())
            {
                BestSeatHold bestSeatHold = entry.getValue();

                if(Objects.nonNull(bestSeatHold) && TicketServiceConstants.MAX_SEAT_HOLD_IN_SECS < Instant.now().getEpochSecond() - bestSeatHold.getTime().getEpochSecond())
                {
                    seatsStatusUpdate(bestSeatHold.getSeatsHeld(), SeatStatus.AVAILABLE);
                    this.seatsAvailable += bestSeatHold.getSeatsHeld().size();
                    bestSeatHoldMap.clear();
                }
            }
        }

    }

    /**
     * Remove the expired hold seats from the bestSeatHoldMap if it exceeds the max Seat hold time with a holdId
     * @param holdId
     */
    private void removeExpiredHoldSeatsWithHoldId(int holdId)
    {
        if(Objects.nonNull(holdId))
        {
            BestSeatHold bestSeatHold = bestSeatHoldMap.get(holdId);
            if(Objects.nonNull(bestSeatHold) && TicketServiceConstants.MAX_SEAT_HOLD_IN_SECS < Instant.now().getEpochSecond() - bestSeatHold.getTime().getEpochSecond())
            {
                seatsStatusUpdate(bestSeatHold.getSeatsHeld(),SeatStatus.AVAILABLE);
                this.seatsAvailable += bestSeatHold.getSeatsHeld().size();
                bestSeatHoldMap.remove(holdId);
            }
        }

    }

    /**
     * Update seat status based on the seats held.
     * @param seatsHeld
     * @param seatStatus
     */

    private void seatsStatusUpdate(List<Seats> seatsHeld , SeatStatus seatStatus)
    {
        if(!CollectionUtils.isEmpty(seatsHeld))
        {
            for(Seats seats : seatsHeld)
            {
                seats.setSeatStatus(seatStatus);
            }
        }
    }


    /**
     * Find the Best Seats in the Venue based on the input number of seats.
     * @param numOfSeats
     * @return
     */

    private List<Seats> findBestSeats(int numOfSeats)
    {
        List<Seats> seatsList = null;
        if(numOfSeats>0)
        {

            if(this.seatsAvailable < numOfSeats)
            {
                System.out.println("Available Seats at Venue Right Now :"+this.seatsAvailable);
                return Collections.synchronizedList(new LinkedList<Seats>());
            }
            Seats[][] seats = venue.getSeats();
            seatsList = Collections.synchronizedList(new LinkedList());
            boolean flag = false;
            for(int i=1; i<venue.getNumOfRows(); i++) {
                if (flag) {
                    break;
                }
                for (int j = 1; j < venue.getNumOfColumns(); j++) {
                    Seats seat = seats[i][j];
                    if (SeatStatus.AVAILABLE == seat.getSeatStatus()) {
                        seatsList.add(seat);
                        if (--numOfSeats == 0) {
                            flag = true;
                            break;
                        }
                    }
                }
            }

        }
        return seatsList;
    }



    /**
     * Hold the Best Seats for the Customer and assign the holdId to CustomerEmail
     * @param customerEmail
     * @return BestHoldSeat Object for the Customer
     */
    private BestSeatHold holdBestSeatToCustomer(List<Seats> bestHoldSeats , String customerEmail)
    {
        if(CollectionUtils.isEmpty(bestHoldSeats))
        {
            return  null;
        }
        BestSeatHold bestSeatHold = new BestSeatHold();
        bestSeatHold.setSeatsHeld(bestHoldSeats);
        bestSeatHold.setTime(Instant.now());
        bestSeatHold.setCustomerEmail(customerEmail);
        return  bestSeatHold;
    }

    @Override
    public int availableSeatsWithInVenue() {
        removeExpiredHoldSeats();
        return seatsAvailable;
    }


    @Override
    public BestSeatHold findAndHoldBestSeats(int numOfSeats, String customerEmail) {
        removeExpiredHoldSeats();
        List<Seats> bestHoldingSeats = findBestSeats(numOfSeats);
        seatsStatusUpdate(bestHoldingSeats,SeatStatus.HOLD);
        this.seatsAvailable -= bestHoldingSeats.size();
        BestSeatHold bestSeatHold = holdBestSeatToCustomer(bestHoldingSeats,customerEmail);
        if(Objects.nonNull(bestSeatHold))
        {
            bestSeatHoldMap.put(bestSeatHold.getHoldId(),bestSeatHold);
        }
        return bestSeatHold;
    }


    @Override
    public String reserveSeats(int holdId, String customerEmail) {
        removeExpiredHoldSeatsWithHoldId(holdId);
        BestSeatHold bestSeatHold = bestSeatHoldMap.get(holdId);
        if(Objects.isNull(bestSeatHold))
        {
            System.out.println("HoldId is Invalid or its Expired");
            return null;
        }
        seatsStatusUpdate(bestSeatHold.getSeatsHeld(),SeatStatus.RESERVED);
        bestSeatHoldMap.remove(holdId);

        List<Seats> seatsList = bestSeatHold.getSeatsHeld();
        String seatReserved = null;
        for(Seats seats :seatsList)
        {
            seatReserved = "Row Number:"+seats.getRowNum()+"Column Number:"+seats.getColumnNum();

        }
        return seatReserved;
    }
}
