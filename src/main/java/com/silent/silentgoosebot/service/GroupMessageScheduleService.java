package com.silent.silentgoosebot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silent.silentgoosebot.dao.GroupMessageScheduleDao;
import com.silent.silentgoosebot.entity.GroupMessageSchedule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

@Service
@Slf4j
public class GroupMessageScheduleService {

    @Resource
    private GroupMessageScheduleDao groupMessageScheduleDao;

}
