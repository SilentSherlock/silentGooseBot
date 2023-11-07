package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.dao.ChannelDao;
import com.silent.silentgoosebot.entity.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Date: 2023/11/7
 * Author: SilentSherlock
 * Description: describe the file
 */
@Service
public class ChannelService {

    @Resource
    private ChannelDao channelDao;

    public int addChannel(Channel channel) {
        return channelDao.insert(channel);
    }
}
