package com.domushub.device.light;

import com.domushub.model.RoomEnum;

public interface ShellyDeviceInterface {
    void turnOn(RoomEnum room);
    void turnOff(RoomEnum room);
}
