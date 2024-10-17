package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppAccountMap;
import com.silent.silentgoosebot.others.base.Result;
import com.silent.silentgoosebot.service.ChatService;
import it.tdlight.jni.TdApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ChatController {

    @Resource
    private AppAccountMap appAccountMap;

    @Resource
    private ChatService chatService;

    @RequestMapping(value = "/getUserChats", method = RequestMethod.POST)
    public Result getUserChats(String phoneNumber) {
        MoistLifeApp curAccount = appAccountMap.getAccountMap().get(phoneNumber);
        if (curAccount == null) {
            log.info("current account is null {}", phoneNumber);
            return Result.createByFalse("without app as " + phoneNumber);
        }

        List<TdApi.Chat> chats = chatService.getClientChats(curAccount);
        List<TdApi.Chat> userChats = new ArrayList<>();
        List<TdApi.Chat> groupChats = new ArrayList<>();
        List<TdApi.Chat> channelChats = new ArrayList<>();
        for (TdApi.Chat chat : chats) {
            TdApi.ChatType chatType = chat.type;
            if (chatType instanceof TdApi.ChatTypeBasicGroup || chatType instanceof TdApi.ChatTypeSupergroup) {
                if (chatType instanceof TdApi.ChatTypeSupergroup && ((TdApi.ChatTypeSupergroup) chatType).isChannel) {
                    channelChats.add(chat);
                } else {
                    groupChats.add(chat);
                }
            } else if (chatType instanceof TdApi.ChatTypePrivate || chatType instanceof TdApi.ChatTypeSecret) {
                userChats.add(chat);
            }
        }
        Result result = Result.createBySuccess();
        result.getResultMap().put("userChats", userChats);
        result.getResultMap().put("groupChats", groupChats);
        result.getResultMap().put("channelChats", channelChats);

        return result;
    }
}
