package com.walmart.ticketservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Api Exception class to Handle the Error Scenario's and prompt the user with appropriate error message and status code.
 * created by Laxmi Kalyan Kistapuram on 12/28/18
 */
@Data
@AllArgsConstructor
public class ApiException {

    private int errorCode;
    private HttpStatus status;
    private String error;
    private String message;
}
