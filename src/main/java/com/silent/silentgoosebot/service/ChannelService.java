package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.dao.ChannelDao;
import com.silent.silentgoosebot.entity.Channel;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
