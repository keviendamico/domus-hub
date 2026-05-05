package com.domushub.repository;

import com.domushub.model.LightEventLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LightEventLogRepository extends JpaRepository<LightEventLog, Long> {

}
