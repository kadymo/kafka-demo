package com.backend.consumer.service;

import com.backend.consumer.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserEventProcessingService {
    private static Logger logger = LoggerFactory.getLogger(UserEventProcessingService.class);

    // Auxiliary methods (simulation)
    private void updateLastLogin(String userId) {
        logger.debug("Updating last login for the user: {}", userId);
        simulateProcessing(100);
    }

    private void sendLoginNotification(UserEvent userEvent) {
        logger.debug("Sending login notification to: {}", userEvent.getEmail());
        simulateProcessing(200);
    }

    private void logUserActivity(UserEvent userEvent) {
        logger.debug("Logging activity: {} fot the user: {}", userEvent.getEventType(), userEvent.getUserId());
        simulateProcessing(50);
    }

    private void clearActiveSessions(String userId) {
        logger.debug("Clearing active sessions for the user: {}", userId);
        simulateProcessing(150);
    }

    private void logSessionDuration(UserEvent userEvent) {
        logger.debug("Logging session duration for the user: {}", userEvent.getUserId());
        simulateProcessing(75);
    }

    private void createUserProfile(UserEvent userEvent) {
        logger.debug("Creating user profile for the user: {}", userEvent.getUserId());
    }

    private void sendWelcomeEmail(UserEvent userEvent) {
        logger.debug("Sending welcome email to: {}", userEvent.getEmail());
    }

    private void processProfileUpdate(UserEvent userEvent) {
        logger.debug("Processing profile update for the user: {}", userEvent.getUserId());
        simulateProcessing(200);
    }

    private void simulateProcessing(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
