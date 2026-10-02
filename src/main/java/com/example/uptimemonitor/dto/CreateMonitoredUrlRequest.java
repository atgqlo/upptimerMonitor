package com.example.uptimemonitor.dto;

import com.example.uptimemonitor.entity.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CreateMonitoredUrlRequest(
        @NotBlank(message = "получатель не может быть пустым")
        String recipient,

        @URL
        @NotBlank(message = "url не может быть пустым")
        String url,

        @NotNull(message = "Канал связи обязателен")
        NotificationChannel channel

){ }
