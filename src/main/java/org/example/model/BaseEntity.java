package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {
    @CreatedDate
    @Column(name="created_at", updatable = false)
    protected Date createdAt;

    @LastModifiedDate
    @Column(name="modified_at", updatable = true)
    protected Date modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.modifiedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = new Date();
    }
}
