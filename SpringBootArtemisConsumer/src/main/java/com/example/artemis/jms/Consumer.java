package com.example.artemis.jms;

import com.example.artemis.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
    Logger logger = LoggerFactory.getLogger(Consumer.class);

    @JmsListener(destination = "${sendgrid.queue}", containerFactory = "pubSubFactory" /*concurrency = "3"*/)
    public void listener(Message message) {
        System.out.println(message + Thread.currentThread().getName());
        if(message.getPayload() instanceof Email email){
            logger.info("Message received, {}", email.toString());
        }else{
            System.err.println("unsupported type of messages");
        }
    }
}
