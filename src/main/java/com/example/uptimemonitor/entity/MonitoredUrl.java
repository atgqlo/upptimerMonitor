package com.example.uptimemonitor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "monitored_urls")
@Getter
@Setter
@NoArgsConstructor
public class MonitoredUrl {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String recipient;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrlStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_checked_at")
    private LocalDateTime lastChecked;
}
