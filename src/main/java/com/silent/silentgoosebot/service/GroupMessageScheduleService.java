package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.dao.GroupMessageScheduleDao;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

@Service
@Slf4j
public class GroupMessageScheduleService {

    @Resource
    private GroupMessageScheduleDao groupMessageScheduleDao;

}
