package com.nutrition.model;

public class ContactMessage {
    private int messageId;
    private Integer userId;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String createdAt;

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
