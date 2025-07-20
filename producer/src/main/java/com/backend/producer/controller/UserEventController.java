package com.backend.producer.controller;

import com.backend.producer.model.UserEvent;
import com.backend.producer.service.EventProducerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserEventController {
    private final static Logger logger = LoggerFactory.getLogger(UserEventController.class);
    private final EventProducerService eventProducerService;

    public UserEventController(EventProducerService eventProducerService) {
        this.eventProducerService = eventProducerService;
    }

    @PostMapping("/{userId}/login")
    public ResponseEntity<Map<String, String>> login(@PathVariable("userId") String userId, @RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            UserEvent event = new UserEvent(userId, "LOGIN", email, "User logged in the system.");
            eventProducerService.sendUserEvent(event);

            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Login event sent succesfully",
                    "eventId", event.getEventId()
            ));
        } catch (Exception e) {
            logger.error("Erro ao processar login: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error sending login event."
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserEvent userEvent) {
        try {
            userEvent.setEventType("REGISTER");
            userEvent.setMessage("New user registered in the system.");
            eventProducerService.sendUserEvent(userEvent);

            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Register event sent succesfully.",
                    "eventId", userEvent.getEventId()
            ));
        } catch (Exception e) {
            logger.error("Erro ao processar registro: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error sending register event."
            ));
        }
    }
}
