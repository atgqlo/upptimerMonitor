package com.example.uptimemonitor.controller;

import com.example.uptimemonitor.dto.CreateMonitoredUrlRequest;
import com.example.uptimemonitor.entity.MonitoredUrl;
import com.example.uptimemonitor.service.UrlMonitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/urls")
@RequiredArgsConstructor
public class UrlMonitorController {


   private final UrlMonitorService service;


   @PostMapping
   public ResponseEntity<MonitoredUrl> addUrl(@Valid @RequestBody CreateMonitoredUrlRequest request){
       MonitoredUrl savedUrl = service.addUrl(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(savedUrl);
   }


   @GetMapping
   public ResponseEntity<Page<MonitoredUrl>> getAllUrls(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10")int size){

       Page<MonitoredUrl> urls = service.getAllUrls(page,size);
       return ResponseEntity.ok(urls);
   }

}
