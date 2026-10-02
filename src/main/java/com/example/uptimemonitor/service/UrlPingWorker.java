package com.example.uptimemonitor.service;


import com.example.uptimemonitor.config.RestClientConfig;
import com.example.uptimemonitor.entity.MonitoredUrl;
import com.example.uptimemonitor.entity.UrlStatus;
import com.example.uptimemonitor.repository.MonitoredUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlPingWorker {

    private final MonitoredUrlRepository repository;

    private final RestClientConfig config;

    @Scheduled(fixedDelay = 30000)
    public void checkAllUrls(){
        List<MonitoredUrl> urls = repository.findAll();

        for(MonitoredUrl url : urls){
            try {
                config.myRestClient().get()
                        .uri(url.getUrl()).retrieve().toBodilessEntity();
                url.setStatus(UrlStatus.UP);
            }catch (Exception e){
                log.info("Сайт {} не доступен: {}", url.getUrl(), e.getMessage());
                url.setStatus(UrlStatus.DOWN);
            }
            url.setLastChecked(LocalDateTime.now());
            repository.save(url);
        }
    }
}
