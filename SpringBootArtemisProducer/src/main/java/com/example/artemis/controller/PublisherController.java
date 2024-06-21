package com.example.artemis.controller;

import com.example.artemis.jms.Producer;
import lombok.RequiredArgsConstructor;
import com.example.artemis.model.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class PublisherController {
    private final Producer producer;

    @PostMapping
    public ResponseEntity<String> publish(@RequestBody Email email) {
        try {
            producer.send(email);
            return new ResponseEntity<>("Message sent", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
