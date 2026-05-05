package com.domushub.device.config;

import com.domushub.model.RoomEnum;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "device.light")
public record LightConfig(String livingRoom) {
    public String getRoomIp(RoomEnum room) {
        return switch (room) {
            case RoomEnum.LIVING_ROOM -> livingRoom;
            default -> throw new IllegalArgumentException("Unknown room: " + room);
        };
    }
}
