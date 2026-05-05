package com.domushub.service;

import com.domushub.model.LightEventLog;
import com.domushub.model.LightSession;
import com.domushub.repository.LightEventLogRepository;
import com.domushub.repository.LightSessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class LightEventLogService {

    private final LightSessionRepository lightSessionRepository;
    private final LightEventLogRepository lightEventLogRepository;

    @Transactional
    public void createOrUpdateLightEntities(String chatId, String room, String action) {
        switch (action) {
            case "on" -> createLightEntities(chatId, room, action);
            case "off" -> updateLightEntities(chatId, room, action);
            default -> log.error("Room: {} - Invalid action: {}", room, action);
        };
    }

    private void createLightEntities(String chatId, String room, String action) {
        log.info("Creating LightEventLog for room: {}, action: {}", room, action);
        lightEventLogRepository.save(
                LightEventLog
                        .builder()
                        .chatId(chatId)
                        .room(room)
                        .action(action)
                        .build()
        );
        lightSessionRepository.save(
                LightSession
                        .builder()
                        .room(room)
                        .build()
        );
        log.info("Created LightEventLog for room: {}, action: {}", room, action);
    }

    private void updateLightEntities(String chatId, String room, String action) {
        log.info("Updating LightEventLog for room: {}, action: {}", room, action);
        lightEventLogRepository.save(
                LightEventLog
                        .builder()
                        .chatId(chatId)
                        .room(room)
                        .action(action)
                        .build()
        );
        Optional<LightSession> opt = lightSessionRepository.getLightSessionByRoomAndEndedAtIsNull(room);
        if (opt.isPresent()) {
            LightSession session = opt.get();
            session.setEndedAt(LocalDateTime.now());
            long duration = ChronoUnit.SECONDS.between(session.getStartedAt(), session.getEndedAt());
            session.setDurationSeconds(duration);
            lightSessionRepository.save(session);
        } else {
            log.info("No LightSession found for room: {}, action: {}. Creating...", room, action);
            lightSessionRepository.save(
                    LightSession
                            .builder()
                            .room(room)
                            .endedAt(LocalDateTime.now())
                            .durationSeconds(0L)
                            .build()
            );
        }
        log.info("Updated LightEventLog for room: {}, action: {}", room, action);
    }
}
