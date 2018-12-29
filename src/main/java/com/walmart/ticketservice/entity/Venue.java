package com.walmart.ticketservice.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Data
@Component
public class Venue {

    private int numOfRows;
    private int numOfColumns;
    private Seats[][] seats;
    private int occupancy;


/*public Venue(int numOfRows, int numOfColumns) {
        super();
        this.numOfRows = numOfColumns;
        this.numOfColumns = numOfColumns;
        this.occupancy = (this.numOfRows * this.numOfColumns);
        seatsInitailization();
    }


    public void occupanyAndSeatsInitailization(int numOfRows, int numOfColumns)
    {
        this.numOfRows = numOfColumns;
        this.numOfColumns = numOfColumns;
        this.occupancy = (this.numOfRows * this.numOfColumns);
        seatsInitailization();
    }*/


    public void seatsInitailization(int numOfRows ,int numOfColumns){
        this.numOfRows = numOfColumns;
        this.numOfColumns = numOfColumns;
        this.occupancy = (this.numOfRows * this.numOfColumns);
        seats = new Seats[numOfRows][numOfColumns];
        for(int i=1; i<numOfRows; i++) {
            for (int j = 1; j <numOfColumns; j++) {
                seats[i][j] = new Seats(i, j, SeatStatus.AVAILABLE);
            }

        }
    }
}
