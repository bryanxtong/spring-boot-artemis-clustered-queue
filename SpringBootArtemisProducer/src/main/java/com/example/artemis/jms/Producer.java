package com.example.artemis.jms;

import lombok.RequiredArgsConstructor;
import com.example.artemis.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class Producer {
    Logger logger = LoggerFactory.getLogger(Producer.class);
    private final JmsTemplate jmsTemplate;
    @Value("${sendgrid.queue}")
    private String artemisQueue;

    public void send(Email message) {
        Instant now = Instant.now();
        logger.info("Sending message::{}", message);
        jmsTemplate.convertAndSend(artemisQueue, message);
    }
}
