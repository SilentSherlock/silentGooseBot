package com.silent.silentgoosebot.others.processor.impl;

import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.processor.ChatsMessageProcessor;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * ls消息处理器默认实现
 * 获取给定聊天对象的消息记录进行处理
 * 每条消息会判断是否是ls的信息，提取关键字落表
 * 全量处理，每次处理的消息条数上限配置在代码中
 * 通过message_id流水表来进行重复判断，重复跳过
 * 处理过的message_id会发送已读回执给tg服务器
 */
@Slf4j
public class ChatsTeacherMessageCommonProcessor implements ChatsMessageProcessor {

    @Override
    public void process(TdApi.Chat chat, MoistLifeApp moistLifeApp) {
        log.info("chat {} start process", chat.title);

        boolean processShutDown = false;
        long fromMessageId = 0;
        int totalMessages = 0;
        while (!processShutDown) {
            CompletableFuture<TdApi.Messages> messagesCompletableFuture = moistLifeApp.getClient().send(
                    new TdApi.GetChatHistory(chat.id, fromMessageId, 0, 100, false)
            );

            TdApi.Messages messages = messagesCompletableFuture.join();
            Arrays.stream(messages.messages).forEach(message -> {
                log.info("message {}", message);
                // todo 处理消息内容
            });


            totalMessages += messages.messages.length;
            if (messages.messages.length < 100 || totalMessages >= AppConst.Tg.message_max_size) {
                processShutDown = true;
            }


        }

    }

    @Override
    public void process(List<TdApi.Chat> chats, MoistLifeApp moistLifeApp) {

    }
}
