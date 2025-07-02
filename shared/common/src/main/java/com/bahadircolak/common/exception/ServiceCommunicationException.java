package com.bahadircolak.common.exception;

public class ServiceCommunicationException extends CommonException {
    
    public ServiceCommunicationException(String message) {
        super(message, "SERVICE_COMMUNICATION_ERROR");
    }
    
    public ServiceCommunicationException(String message, Throwable cause) {
        super(message, "SERVICE_COMMUNICATION_ERROR", cause);
    }
} 