package com.hemonth.jmsclient;

import com.hemonth.consumer.JmsConsumer;
import com.hemonth.producer.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Is used for restcontroller.WebController
@Service
public class JmsClientImpl implements JmsClient{

    @Autowired
    JmsConsumer jmsConsumer;

    @Autowired
    JmsProducer jmsProducer;

    @Override
    public void send(String msg) {
        jmsProducer.send(msg);
    }

    @Override
    public String receive() {
        return jmsConsumer.receive();
    }
}
