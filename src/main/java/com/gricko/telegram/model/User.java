package com.gricko.telegram.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private Long chatId;
    private String email;
    private String phone;
    private Integer statetId;

    private Boolean admin;
    private Boolean notified = false;

    public User() {
    }

    public User(Long chatId, Integer statetId) {
        this.chatId = chatId;
        this.statetId = statetId;
    }

    public User(Long chatId, Integer statetId, Boolean admin) {
        this.chatId = chatId;
        this.statetId = statetId;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatetId() {
        return statetId;
    }

    public void setStatetId(Integer statetId) {
        this.statetId = statetId;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }
}
