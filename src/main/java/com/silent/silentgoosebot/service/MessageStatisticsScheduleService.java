package com.silent.silentgoosebot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silent.silentgoosebot.dao.MessageStatisticsScheduleDao;
import com.silent.silentgoosebot.entity.MessageStatisticsSchedule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MessageStatisticsScheduleService {

    @Resource
    private MessageStatisticsScheduleDao messageStatisticsScheduleDao;

    public List<MessageStatisticsSchedule> getMessageStatisticsScheduleByType(String scheduleType) {
        LambdaQueryWrapper<MessageStatisticsSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageStatisticsSchedule::getScheduleType, scheduleType);
        return messageStatisticsScheduleDao.selectList(queryWrapper);
    }

    public int addMessageStatisticsSchedule(MessageStatisticsSchedule messageStatisticsSchedule) {
        return messageStatisticsScheduleDao.insert(messageStatisticsSchedule);
    }
}
