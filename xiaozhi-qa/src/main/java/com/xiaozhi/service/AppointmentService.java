package com.xiaozhi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaozhi.dto.Appointment;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
