package com.walmart.ticketservice.service;

import com.walmart.ticketservice.TicketserviceApplication;
import com.walmart.ticketservice.constants.TicketServiceConstants;
import com.walmart.ticketservice.entity.BestSeatHold;
import com.walmart.ticketservice.entity.Venue;
import com.walmart.ticketservice.entity.AvailableSeats;
import com.walmart.ticketservice.entity.ReservedSeats;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {TicketserviceApplication.class})
public class TicketServiceImplTest {

    @Autowired
    private MockMvc mockMvc;


    private TicketServiceImpl ticketServiceImpl;
    private  Venue venue;
    private int seatsAvailable;
    private Map<Integer, BestSeatHold> bestSeatHoldMap ;

    private int holdId;





    @Before
    public void setUp() throws Exception {

        venue = new Venue();
        venue.seatsInitailization(TicketServiceConstants.NUM_OF_ROWS,TicketServiceConstants.NUM_OF_COLUMNS);
        this.seatsAvailable = venue.getOccupancy();
        bestSeatHoldMap = new ConcurrentHashMap();
        ticketServiceImpl = new TicketServiceImpl(venue);

    }


    @Test
    public void availableSeatsWithInVenueServiceTest()throws Exception {

       ResponseEntity<AvailableSeats> responseEntity = ticketServiceImpl.availableSeatsWithInVenue();
       Assert.assertEquals(200,responseEntity.getStatusCode().value());
        Assert.assertEquals(true,responseEntity.getStatusCode().is2xxSuccessful());
       AvailableSeats availableSeats = responseEntity.getBody();
        Assert.assertEquals(20,TicketServiceConstants.NUM_OF_ROWS);
        Assert.assertEquals(400,availableSeats.getSeatsAvailable());
       Assert.assertNotNull(bestSeatHoldMap);


    }


    @Test
   public void findAndHoldBestSeats() throws Exception {

        String customerEmail = "laxmikalyan91@gmail.com";
        int numOfSeats = 5;

        ResponseEntity<BestSeatHold> responseEntity = ticketServiceImpl.findAndHoldBestSeats(numOfSeats,customerEmail);
        Assert.assertEquals(200,responseEntity.getStatusCode().value());
        Assert.assertEquals(true,responseEntity.getStatusCode().is2xxSuccessful());
        BestSeatHold bestSeatHold = responseEntity.getBody();
        Assert.assertEquals(bestSeatHold,responseEntity.getBody());
        Assert.assertEquals(20,TicketServiceConstants.NUM_OF_COLUMNS);
        holdId = bestSeatHold.getHoldId();
        Assert.assertEquals(1,holdId);
        Assert.assertEquals("laxmikalyan91@gmail.com",bestSeatHold.getCustomerEmail());
        Assert.assertNotNull(bestSeatHoldMap);

   }


    @Test
    public void reserveSeats() throws Exception {

        String customerEmail = "laxmikalyan91@gmail.com";
        int holdId = 1;
        ResponseEntity<ReservedSeats> responseEntity = ticketServiceImpl.reserveSeats(holdId,customerEmail);
        Assert.assertEquals(20,TicketServiceConstants.NUM_OF_COLUMNS);
        Assert.assertEquals(90,TicketServiceConstants.MAX_SEAT_HOLD_IN_SECS);
        Assert.assertEquals("laxmikalyan91@gmail.com",customerEmail);
        Assert.assertNotNull(bestSeatHoldMap);
        Assert.assertEquals(1,holdId);

    }
}