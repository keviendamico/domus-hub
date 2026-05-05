package com.domushub.device.light;

import com.domushub.device.config.LightConfig;
import com.domushub.model.RoomEnum;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
@Profile("production")
public class ShellyDevice implements ShellyDeviceInterface {

    private final LightConfig lightConfig;
    private final RestClient restClient;

    @Override
    public void turnOn(RoomEnum room) {
        String ip = lightConfig.getRoomIp(room);
        restClient.get().uri(ip + "/light/0?turn=on&mode=white").retrieve().toBodilessEntity();
    }

    @Override
    public void turnOff(RoomEnum room) {
        String ip = lightConfig.getRoomIp(room);
        restClient.get().uri(ip + "/light/0?turn=off").retrieve().toBodilessEntity();
    }
}
