package com.walmart.ticketservice.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceValidationsTest {


    TicketServiceValidations ticketServiceValidations;


    @Before
    public void init()
    {
        ticketServiceValidations = mock(TicketServiceValidations.class);
    }

    @Test
    public void validateEmail() {

        String email = "laxmikalyan91@gmail.com";
        String email2 = "laxmikalyan.com";

        Boolean flag = TicketServiceValidations.validateEmail(email);
        Boolean flag2 = TicketServiceValidations.validateEmail(email2);

        Assert.assertEquals(true,flag);
        Assert.assertEquals(false,flag2);
    }

    @Test
    public void validateCustomerEmailWithExisting() {

        String email = "laxmikalyan91@gmail.com";
        String email2 = "laxmikalyan91@ticketservice.com";
        String email3 = "laxmikalyan91@gmail.com";

        Boolean flag = TicketServiceValidations.validateCustomerEmailWithExisting(email,email3);
        Boolean flag2 = TicketServiceValidations.validateCustomerEmailWithExisting(email,email2);

        Assert.assertEquals(true,flag);
        Assert.assertEquals(false,flag2);

    }
}