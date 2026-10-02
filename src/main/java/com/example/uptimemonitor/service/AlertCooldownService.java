package com.example.uptimemonitor.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertCooldownService {

    private final StringRedisTemplate redisTemplate;

    public boolean canSendAlert(Long urlId){
        return !redisTemplate.hasKey("cooldown:" + urlId);
    }

    public void setCooldown(Long urlId){
        String key = "cooldown:" + urlId;
        redisTemplate.opsForValue().set(key, "locked", java.time.Duration.ofMinutes(10));
    }
}
