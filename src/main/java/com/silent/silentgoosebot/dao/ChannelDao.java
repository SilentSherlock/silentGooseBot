package com.silent.silentgoosebot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silent.silentgoosebot.entity.Channel;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelDao extends BaseMapper<Channel> {
}
