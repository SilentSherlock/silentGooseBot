package com.silent.silentgoosebot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silent.silentgoosebot.dao.AutoMessageCreatorDao;
import com.silent.silentgoosebot.dao.GroupMessageScheduleDao;
import com.silent.silentgoosebot.entity.AutoMessageCreator;
import com.silent.silentgoosebot.entity.GroupMessageSchedule;
import com.silent.silentgoosebot.others.base.MessageType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date: 2024/2/6
 * Author: SilentSherlock
 * Description: describe the file
 */
@Service
@Slf4j
public class TimerService {

    @Resource
    private GroupMessageScheduleDao groupMessageScheduleDao;
    @Resource
    private AutoMessageCreatorDao autoMessageCreatorDao;

    public AutoMessageCreator selectCreatorByScheduleId(Integer scheduleId) {
         GroupMessageSchedule groupMessageSchedule = groupMessageScheduleDao.selectById(scheduleId);
         if (null == groupMessageSchedule) {
             log.error("didn't get schedule by " + scheduleId);
             return null;
         }
        return autoMessageCreatorDao.selectById(
                groupMessageSchedule.getAutoMessageCreatorId()
        );
    }
    public AutoMessageCreator selectCreatorByCreatorId(Integer creatorId) {
        return autoMessageCreatorDao.selectById(creatorId);
    }

    public GroupMessageSchedule selectScheduleByChatIdAndMsgType(long chatId, MessageType messageType) {
        LambdaQueryWrapper<GroupMessageSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMessageSchedule::getChatId, chatId)
                .eq(GroupMessageSchedule::getMessageType, messageType);
        return groupMessageScheduleDao.selectOne(wrapper);
    }

    public List<GroupMessageSchedule> selectSchedulesByMsgType(MessageType messageType) {
        LambdaQueryWrapper<GroupMessageSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMessageSchedule::getMessageType, messageType);
        return groupMessageScheduleDao.selectList(wrapper);
    }
}
