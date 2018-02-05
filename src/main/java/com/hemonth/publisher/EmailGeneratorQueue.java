package com.hemonth.publisher;

import com.hemonth.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.logging.Logger;

@Service
public class EmailGeneratorQueue {

    Logger logger = Logger.getLogger(EmailGeneratorQueue.class.getCanonicalName());
    @Autowired
    private JmsTemplate jmsTemplate;

    public EmailGeneratorQueue() {
    }

    /*    @Scheduled(fixedDelay = 5000)*/
    public void generateEmail() {
        Email email = new Email();
        email.setEmailAddress("hemonth@gmail.com");
        email.setMessage("hello form hemonth");
        email.setName("Hemonth");
        logger.info("Sending Email:" + email);
        //simple convertandsend
        /*jmsTemplate.convertAndSend("Emails", email);*/

        //using message postProcessor
        /*jmsTemplate.convertAndSend("Emails", email, (message) -> {
            message.setStringProperty("JMSGroupID", email.getEmailAddress());
            return message;
        });*/

        // send using message creator
        jmsTemplate.send("Emails", (Session session) ->{
            TextMessage msg = session.createTextMessage();
            msg.setStringProperty("Name","hemonth");
            msg.setText("Bye!!");
            return msg;
        });
    }

}
