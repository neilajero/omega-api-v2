package com.ejb.restfulapi.ad.models;

public class ApprovalUser {
    private String username;
    private String level;
    private String type;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}