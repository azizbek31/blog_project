package com.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification extends Auditable {

    @Column(nullable = false)
    private Long recipientUserId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean isRead = false;

}
