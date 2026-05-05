package com.domushub.device.light;

import com.domushub.model.RoomEnum;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("local")
public class MockShellyDevice implements ShellyDeviceInterface {

    @Override
    public void turnOn(RoomEnum room) {
        log.info("Mock shelly device turned on - Room: {}", room.getName());
    }

    @Override
    public void turnOff(RoomEnum room) {
        log.info("Mock shelly device turned off - Room: {}", room.getName());
    }
}
