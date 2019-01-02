package com.walmart.ticketservice.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

/**
 * Validation class for the Service methods to validate request params.
 * created by Laxmi Kalyan Kistapuram on 12/29/18
 */
public class TicketServiceValidations {

    /**
     * Method to validate the Email
     * @param email
     * @return true is the customer email is valid else false
     */
    public static boolean validateEmail(String email)
    {
        if(StringUtils.isEmpty(email))
        {
            return false;
        }

        email = email.trim();
        EmailValidator emailValidator = EmailValidator.getInstance();
        return  emailValidator.isValid(email);
    }

    /**
     * Method to validate the given customer Email with the existing holdId customer Email
     * @param customerEmail
     * @param holdIdCustomerEmail
     * @return
     */
    public static boolean validateCustomerEmailWithExisting(String customerEmail, String holdIdCustomerEmail){

        if(!StringUtils.isEmpty(customerEmail))
        {
            return customerEmail.equalsIgnoreCase(holdIdCustomerEmail);
        }

        return false;
    }

}
