package com.silent.silentgoosebot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.silent.silentgoosebot.dao.MessageProcessorDao;
import com.silent.silentgoosebot.entity.MessageProcessor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * Date: 2023/11/7
 * Author: SilentSherlock
 * Description: describe the file
 */
@Service
public class MessageProcessorService {

    @Resource
    private MessageProcessorDao messageProcessorDao;

    public MessageProcessor selectByChannelId(String channelId) {
        LambdaQueryWrapper<MessageProcessor> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MessageProcessor::getChannelId, channelId);
        return messageProcessorDao.selectOne(wrapper);
    }

    public MessageProcessor selectOne(QueryWrapper<MessageProcessor> wrapper) {
        return messageProcessorDao.selectOne(wrapper);
    }

    public MessageProcessor selectById(String id) {
        return messageProcessorDao.selectById(id);
    }
}
