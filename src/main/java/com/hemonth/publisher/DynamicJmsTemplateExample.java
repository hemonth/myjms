package com.hemonth.publisher;

import com.hemonth.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

public class DynamicJmsTemplateExample {

    @Autowired
    @Qualifier("cacheConnectionFactory")
    ConnectionFactory cacheConnectionFactory;

    @Autowired
    MessageConverter messageConverter;

    public void sendEmail(Email email, int priority, int timeToLive){
        JmsTemplate jmsTemplate = new JmsTemplate(cacheConnectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setPriority(priority);
        jmsTemplate.setTimeToLive(timeToLive);
        jmsTemplate.convertAndSend("Emails",email);
    }
}
