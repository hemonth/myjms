package com.hemonth.listners;

import com.hemonth.domain.Order;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageListner {

    @JmsListener(destination = "Orders",containerFactory = "topicListnerConnectionFactory",subscription = "orders")
    public void onMessage(Order order){
        System.out.println("order received is :"+order);
    }
}
