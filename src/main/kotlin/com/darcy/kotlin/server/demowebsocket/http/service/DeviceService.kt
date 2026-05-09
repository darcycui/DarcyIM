package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.Device
import com.darcy.kotlin.server.demowebsocket.http.repository.DeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceService @Autowired constructor(
    private val deviceRepository: DeviceRepository,
    private val userService: UserService
) {
    fun createDevice(userId: Long, deviceName: String): Device {
        val user = userService.queryUserById(userId)
        val device = Device(
            user = user,
            name = deviceName
        )
        return deviceRepository.save(device)
    }

    fun queryByUserIdAndDeviceName(userId: Long, deviceName: String): Device? {
        val device = deviceRepository.findByUserIdAndDevicename(userId, deviceName)
        return device
    }
}