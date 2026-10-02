package com.example.uptimemonitor.service;

import com.example.uptimemonitor.dto.CreateMonitoredUrlRequest;
import com.example.uptimemonitor.entity.MonitoredUrl;
import com.example.uptimemonitor.entity.UrlStatus;
import com.example.uptimemonitor.repository.MonitoredUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlMonitorService {

    private final MonitoredUrlRepository repository;

    public MonitoredUrl addUrl(CreateMonitoredUrlRequest request){
        log.info("Создание нового url для {}",request.recipient());
        MonitoredUrl monitoredUrl = new MonitoredUrl();
        monitoredUrl.setRecipient(request.recipient());
        monitoredUrl.setUrl(request.url());
        monitoredUrl.setChannel(request.channel());
        monitoredUrl.setStatus(UrlStatus.UNKNOWN);

        return repository.save(monitoredUrl);


    }

    public Page<MonitoredUrl> getAllUrls(int page, int size){
        log.info("Получение всех юзеров");
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repository.findAll(pageable);
    }
}
