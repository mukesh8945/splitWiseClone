package com.anupam.Splitwise.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mukesh.Splitwise.service.audit.AuditService;

import java.time.Instant;

@EnableScheduling
@Component
@Slf4j
public class AuditScheduler {

    @Autowired
    private AuditService auditService;

    @Scheduled(fixedDelayString = "${splitwise.audit.scheduler.persist.time.hour}")
    public void persistAuditMessages() {
        log.debug("started scheduler persistAuditMessages at:{}", Instant.now());
        auditService.triggerPersistInDB();
        log.debug("Ended scheduler persistAuditMessages at:{}", Instant.now());
    }
}
