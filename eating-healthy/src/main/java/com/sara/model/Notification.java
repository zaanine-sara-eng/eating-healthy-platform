package com.sara.model;

import java.util.Date;

public class Notification {
    
    private int notificationId;
    private int subscriberId;
    private String message;
    private boolean isRead;
    private Date createdAt;
    
    // Getters and Setters
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    
    public int getSubscriberId() { return subscriberId; }
    public void setSubscriberId(int subscriberId) { this.subscriberId = subscriberId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}