package com.walmart.ticketservice.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Venue class to store info about occupancy,rows ,columns
 * created by Laxmi Kalyan Kistapuram on 12/28/18
 */
@Data
@Component
public class Venue {

    private int numOfRows;
    private int numOfColumns;
    private Seats[][] seats;
    private int occupancy;

    /**
     *  Seats initialisation and make the status available based on no of rows and no of columns
     * @param numOfRows
     * @param numOfColumns
     */
    public void seatsInitialisation(int numOfRows ,int numOfColumns){
        this.numOfRows = numOfColumns;
        this.numOfColumns = numOfColumns;
        this.occupancy = (this.numOfRows * this.numOfColumns);
        seats = new Seats[numOfRows][numOfColumns];
        for(int i=0; i<numOfRows; i++) {
            for (int j = 0; j <numOfColumns; j++) {
                seats[i][j] = new Seats(i, j, SeatStatus.AVAILABLE);
            }

        }
    }
}
