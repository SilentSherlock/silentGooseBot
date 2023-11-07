package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.dao.MessageProcessorDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Date: 2023/11/7
 * Author: SilentSherlock
 * Description: describe the file
 */
@Service
public class MessageProcessorService {

    @Resource
    private MessageProcessorDao messageProcessorDao;
}
