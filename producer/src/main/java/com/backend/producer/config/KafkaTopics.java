package com.backend.producer.config;

public class KafkaTopics {
    public static final String USER_EVENTS = "user-events";
    public static final String ORDER_EVENTS = "order-events";
    public static final String NOTIFICATION_EVENTS = "notification-events";

    public static final class UserEvents {
        public static final String LOGIN = "user.login";
        public static final String LOGOUT = "user.logout";
        public static final String REGISTER = "user.register";
        public static final String UPDATE_PROFILE = "user.update_profile";
    }

    public static final class OrderEvents {
        public static final String CREATED = "order.created";
        public static final String UPDATED = "order.updated";
        public static final String CANCELLED = "order.cancelled";
        public static final String COMPLETED = "order.completed";
    }

    private KafkaTopics() {}
}
