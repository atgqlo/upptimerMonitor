package com.example.uptimemonitor.repository;

import com.example.uptimemonitor.entity.MonitoredUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoredUrlRepository extends JpaRepository<MonitoredUrl, Long> {
}
