package com.example.uptimemonitor.dto;

import com.example.uptimemonitor.entity.NotificationChannel;

public record AlertMessage(
        Long id,
        String url,
        String message,
        String recipient,
        NotificationChannel channel
) {}
