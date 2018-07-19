package com.hemonth.publisher;

import com.hemonth.domain.Order;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderGeneratorTopic {

    private JmsTemplate jmsTemplate;

    public OrderGeneratorTopic(@Qualifier("topicJmsTemplate") JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void generateOrder(){
        Order order = new Order();
        order.setOrderId("1234");
        order.setAmount("$1200.00");
        order.setReference("Ref 1");
        jmsTemplate.convertAndSend("Orders", order);
    }
}
