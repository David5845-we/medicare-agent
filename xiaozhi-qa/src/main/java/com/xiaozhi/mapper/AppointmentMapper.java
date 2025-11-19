package com.xiaozhi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaozhi.dto.Appointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
