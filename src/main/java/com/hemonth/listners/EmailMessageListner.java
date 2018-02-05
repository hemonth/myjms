package com.hemonth.listners;

import com.hemonth.domain.Email;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageListner {

    //setting dynamic consumers between 3-10
    // JmsListner is the marker for the spring framework to instantiate of DefaultMessageListenerContainer
    @JmsListener(destination = "Emails",concurrency = "3-10")
    public void onMessage(Email email){
        System.out.println("email message received is: "+email);
    }
}
