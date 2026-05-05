package com.domushub.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RoomEnum {
    LIVING_ROOM("living_room");

    private final String name;

    RoomEnum(String name) {
        this.name = name;
    }

    public static RoomEnum fromName(String name) {
        return Arrays.stream(values())
                .filter(r -> r.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown room: " + name));
    }
}
