package com.kafka.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.producer.domain.LibraryEvent;
import com.kafka.producer.domain.LibraryEventType;
import com.kafka.producer.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class LibraryEventsController {

    @Autowired
    private LibraryEventProducer libraryEventProducer;

    @GetMapping("/ping")
    public String test_ping() {
        return "Producer is Active !!!";
    }

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        // set the library event
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        // initialize kafka producer
        libraryEventProducer.sendLibraryEvents(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping("/v1/libraryevent-syn")
    public ResponseEntity<LibraryEvent> postLibraryEventSynchronous(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        // set the library event
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);

        // initialize kafka producer synchronously
        SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventsSynchronous(libraryEvent);


        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping("/v1/libraryevent-producer")
    public ResponseEntity<LibraryEvent> postLibraryEventProducerRecord(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        // set the library event
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        // initialize kafka producer synchronously
        libraryEventProducer.sendLibraryEventsUsingProducerRecord(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
