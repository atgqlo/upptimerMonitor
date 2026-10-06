package com.example.uptimemonitor.service;

import com.example.uptimemonitor.dto.AlertMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String  TOPIC_NAME = "url_alerts";

    public void sendAlert(AlertMessage alert){
        log.info("Начинаем отправку алерта для url: {}", alert.url());

        kafkaTemplate.send(TOPIC_NAME, alert).whenComplete((result, ex) -> {
            if(ex != null){
                log.error("Ошибка отпрвки в kafka: {}", ex.getMessage(), ex);
            }
        });
    }
}
