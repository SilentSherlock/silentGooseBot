package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppAccountMap;
import com.silent.silentgoosebot.others.base.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {

    @Resource
    private AppAccountMap appAccountMap;

    @RequestMapping("/getUserChats")
    public Result getUserChats(String phoneNumber) {
        MoistLifeApp curAccount = appAccountMap.getAccountMap().get(phoneNumber);
        if (curAccount == null) {
            log.info("current account is null {}", phoneNumber);
        }

    }
}
