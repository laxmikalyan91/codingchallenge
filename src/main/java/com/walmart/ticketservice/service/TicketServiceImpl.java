package com.walmart.ticketservice.service;

import com.walmart.ticketservice.constants.TicketServiceConstants;
import com.walmart.ticketservice.controller.TicketServiceController;
import com.walmart.ticketservice.entity.Venue;
import com.walmart.ticketservice.entity.BestSeatHold;
import com.walmart.ticketservice.entity.SeatStatus;
import com.walmart.ticketservice.entity.Seats;
import com.walmart.ticketservice.entity.AvailableSeats;
import com.walmart.ticketservice.exception.ApiException;
import com.walmart.ticketservice.utils.GsonUtils;
import com.walmart.ticketservice.utils.LogUtils;
import com.walmart.ticketservice.entity.ReservedSeats;
import com.walmart.ticketservice.validator.TicketServiceValidations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service Class to implement the methods from controller to handle http requests and perform the business logic.
 * created by Laxmi Kalyan Kistapuram on 12/29/18
 */
@Service
public class TicketServiceImpl implements TicketServiceController {

    private int seatsAvailable;

    @Autowired
    private Venue venue;

    private Map<Integer, BestSeatHold> bestSeatHoldMap;

    private static Logger logger = LogUtils.getLogger(TicketServiceImpl.class.getName());

    @Autowired
    TicketServiceImpl(Venue venue)
    {
        super();
        this.venue = venue;
        this.venue.seatsInitailization(TicketServiceConstants.NUM_OF_ROWS,TicketServiceConstants.NUM_OF_COLUMNS);
        this.seatsAvailable = venue.getOccupancy();
        bestSeatHoldMap = new ConcurrentHashMap();
    }

    @Override
    public ResponseEntity<AvailableSeats> availableSeatsWithInVenue() {
        removeExpiredHoldSeats();
        return new ResponseEntity(new AvailableSeats(seatsAvailable,venue.getOccupancy()), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<BestSeatHold> findAndHoldBestSeats(int numOfSeats, String customerEmail) {
        removeExpiredHoldSeats();
        if(Objects.isNull(numOfSeats) || numOfSeats<=0 )
        {
            logger.error("Number of seats is Invalid {}",numOfSeats);
            return new ResponseEntity(GsonUtils.convertToJson(new ApiException(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,TicketServiceConstants.INVALID_REQUEST,"Number of Seats should be greater than 0 ")),HttpStatus.BAD_REQUEST);
        }
        if(!TicketServiceValidations.validateEmail(customerEmail))
        {
            logger.error("customerEmail  is Invalid {}",customerEmail);
            return new ResponseEntity(GsonUtils.convertToJson(new ApiException(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,TicketServiceConstants.INVALID_REQUEST,"Invalid Customer Email")),HttpStatus.BAD_REQUEST);
        }

        List<Seats> bestHoldingSeats = findBestSeats(numOfSeats);
        if(!CollectionUtils.isEmpty(bestHoldingSeats))
        {
            seatsStatusUpdate(bestHoldingSeats,SeatStatus.HOLD);
            this.seatsAvailable -= bestHoldingSeats.size();
            BestSeatHold bestSeatHold = holdBestSeatToCustomer(bestHoldingSeats,customerEmail);
            if(Objects.nonNull(bestSeatHold))
            {
                bestSeatHoldMap.put(bestSeatHold.getHoldId(),bestSeatHold);
            }
            return new ResponseEntity(bestSeatHold,HttpStatus.OK);
        }
        return  new ResponseEntity(GsonUtils.convertToJson(new ApiException(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,TicketServiceConstants.INVALID_REQUEST,"Available Seats at Venue Right Now "+this.seatsAvailable)),HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<ReservedSeats> reserveSeats(int holdId, String customerEmail) {

        if(Objects.isNull(holdId) || holdId<=0 )
        {
            logger.error("HoldId is Invalid {}",holdId);
            return new ResponseEntity(GsonUtils.convertToJson(new ApiException(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,TicketServiceConstants.INVALID_REQUEST,"Invalid HoldId ")),HttpStatus.BAD_REQUEST);
        }
        if(!TicketServiceValidations.validateEmail(customerEmail))
        {
            logger.error("customerEmail  is Invalid {}",customerEmail);
            return new ResponseEntity(GsonUtils.convertToJson(new ApiException(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,TicketServiceConstants.INVALID_REQUEST,"Invalid Customer Email")),HttpStatus.BAD_REQUEST);
        }

        removeExpiredHoldSeatsWithHoldId(holdId);
        BestSeatHold bestSeatHold = bestSeatHoldMap.get(holdId);

        if(Objects.isNull(bestSeatHold))
        {
            logger.error("HoldId is Invalid or its Expired : {}",holdId);
            return new ResponseEntity(GsonUtils.convertToJson(new ApiException(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,TicketServiceConstants.INVALID_REQUEST,"HoldId is Invalid or its lapsed the maxmium hold time")),HttpStatus.BAD_REQUEST);
        }

        if(!TicketServiceValidations.validateCustomerEmailWithExisting(customerEmail,bestSeatHold.getCustomerEmail()))
        {
            logger.error("customerEmail is Invalid doesn't match up the records {} {}",customerEmail,bestSeatHold.getCustomerEmail());
            return new ResponseEntity(GsonUtils.convertToJson(new ApiException(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST,TicketServiceConstants.INVALID_REQUEST,"Customer Email doesn't match up with records please try with a different Email")),HttpStatus.BAD_REQUEST);
        }

        seatsStatusUpdate(bestSeatHold.getSeatsHeld(),SeatStatus.RESERVED);
        List<Seats> seatsList = bestSeatHold.getSeatsHeld();
        ReservedSeats reservedSeats = new ReservedSeats(seatsList,holdId, UUID.randomUUID().toString(),customerEmail);

        return new ResponseEntity(reservedSeats,HttpStatus.OK);
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
                logger.info("Available Seats at Venue Right Now ={}", this.seatsAvailable);
                seatsList = Collections.synchronizedList(new LinkedList<Seats>());
                return  seatsList;
                 }
            Seats[][] seats = venue.getSeats();
            seatsList = Collections.synchronizedList(new LinkedList());
            boolean flag = false;
            for(int i=0; i<venue.getNumOfRows(); i++) {
                if (flag) {
                    break;
                }
                for (int j = 0; j <venue.getNumOfColumns(); j++) {
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
        if(!CollectionUtils.isEmpty(bestHoldSeats))
        {
            BestSeatHold bestSeatHold = new BestSeatHold();
            bestSeatHold.setSeatsHeld(bestHoldSeats);
            bestSeatHold.setTime(Instant.now());
            bestSeatHold.setCustomerEmail(customerEmail);
            return  bestSeatHold;
        }
       return null;
    }


}
