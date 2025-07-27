package com.backend.consumer.service;

import com.backend.consumer.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserEventProcessingService {
    private static Logger logger = LoggerFactory.getLogger(UserEventProcessingService.class);

    public void processLoginEvent(UserEvent userEvent) {
        logger.info("Processing login of the user: {}", userEvent.getUserId());

        try {
            updateLastLogin(userEvent.getUserId());
            sendLoginNotification(userEvent);
            logUserActivity(userEvent);
            logger.info("Login processed successfully for the user: {}", userEvent.getUserId());
        } catch (Exception e) {
            logger.error("Error processing login fot the user: {}", userEvent.getUserId());
            throw e;
        }
    }

    public void processLogoutEvent(UserEvent userEvent) {
        logger.info("Processing logout of the user: {}", userEvent.getUserId());
        try {
            clearActiveSessions(userEvent.getUserId());
            logSessionDuration(userEvent);
            logUserActivity(userEvent);

            logger.info("Logout processed successfully for the user: {}", userEvent.getUserId());
        } catch (Exception e) {
            logger.error("Error processing logout of the user: {}", userEvent.getUserId());
            throw e;
        }
    }

    public void processRegisterEvent(UserEvent userEvent) {
        logger.info("Processing register of the user: {}", userEvent.getUserId());
        try {
            createUserProfile(userEvent);
            sendWelcomeEmail(userEvent);
            logUserActivity(userEvent);
            logger.info("Register processed successfully for the user: {}", userEvent.getUserId());
        } catch (Exception e) {
            logger.error("Error processing register of the user: {}", userEvent.getUserId());
            throw e;
        }
    }

    public void processGenericUserEvent(UserEvent userEvent) {
        logger.info("Processing generic user event: {} for the user: {}", userEvent.getEventType(), userEvent.getUserId());
        try {
            logUserActivity(userEvent);
            switch (userEvent.getEventType()) {
                case "UPDATE_PROFILE":
                    processProfileUpdate(userEvent);
                    break;
                case "CHANGE_PASSWORD":
                    processPasswordChange(userEvent);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing generic user event: {}", userEvent.getUserId());
        }
    }

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

    private void processPasswordChange(UserEvent userEvent) {
        logger.debug("Processing password change for the user: {}", userEvent.getUserId());
        simulateProcessing(150);
    }

    private void simulateProcessing(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
