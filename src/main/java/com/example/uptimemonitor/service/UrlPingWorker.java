package com.example.uptimemonitor.service;


import com.example.uptimemonitor.config.RestClientConfig;
import com.example.uptimemonitor.dto.AlertMessage;
import com.example.uptimemonitor.entity.MonitoredUrl;
import com.example.uptimemonitor.entity.UrlStatus;
import com.example.uptimemonitor.repository.MonitoredUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlPingWorker {

    private final MonitoredUrlRepository repository;

    private final RestClient restClient;

    private final AlertCooldownService cooldownService;

    private final AlertProducerService alertProducerService;

    @Scheduled(fixedDelay = 30000)
    public void checkAllUrls(){
        List<MonitoredUrl> urls = repository.findAll();

        for(MonitoredUrl url : urls){
            try {
                restClient.get()
                        .uri(url.getUrl()).retrieve().toBodilessEntity();
                url.setStatus(UrlStatus.UP);
            }catch (Exception e){
                url.setStatus(UrlStatus.DOWN);
                if (cooldownService.canSendAlert(url.getId())){
                    AlertMessage message = new AlertMessage(url.getId(), url.getUrl(), "сайт недоступен", url.getRecipient(), url.getChannel());
                    alertProducerService.sendAlert(message);
                    cooldownService.setCooldown(url.getId());
                }
            }
            url.setLastChecked(LocalDateTime.now());
            repository.save(url);
        }
    }
}
