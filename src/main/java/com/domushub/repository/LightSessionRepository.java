package com.domushub.repository;

import com.domushub.model.LightSession;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LightSessionRepository extends JpaRepository<LightSession, Long> {

    Optional<LightSession> getLightSessionByRoomAndEndedAtIsNull(String room);
}
