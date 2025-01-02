package com.silent.silentgoosebot.others.processor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silent.silentgoosebot.dao.TeacherMessageSequenceDao;
import com.silent.silentgoosebot.entity.TeacherMessageSequence;
import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.IdGenerator;
import com.silent.silentgoosebot.others.processor.ChatsMessageProcessor;
import com.silent.silentgoosebot.others.utils.ContextUtils;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            //先落流水表再处理，流水表中存在的message不再处理
            log.info("TdApi.Messages length {}", messages.messages.length);
            TeacherMessageSequenceDao teacherMessageSequenceDao = ContextUtils.getBean(TeacherMessageSequenceDao.class);
            List<TdApi.Message> unprocessedMessages = getMessages(messages, teacherMessageSequenceDao);
            unprocessedMessages.forEach(message -> {
                TeacherMessageSequence teacherMessageSequence = new TeacherMessageSequence();
                try {
                    teacherMessageSequence.setTeacherMessageSequenceId(IdGenerator.getNextTeacherMessageSequenceId())
                            .setChatId(message.chatId)
                            .setMessageId(message.id)
                            .setTeacherMessage(new ObjectMapper().writeValueAsString(message))
                            .setCreateTime(LocalDateTime.now())
                            .setUpdateTime(LocalDateTime.now());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                teacherMessageSequenceDao.insert(teacherMessageSequence);
            });
            log.info("unprocessedMessages size {}", unprocessedMessages.size());

            //处理消息内容
            unprocessedMessages.forEach(message -> {
                log.info("message {}", message);
                // todo 处理消息内容
            });

            fromMessageId = unprocessedMessages.get(unprocessedMessages.size() - 1).id;
            totalMessages += messages.messages.length;
            if (messages.messages.length < 1 || totalMessages >= AppConst.Tg.message_max_size) {
                processShutDown = true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static @NotNull List<TdApi.Message> getMessages(TdApi.Messages messages, TeacherMessageSequenceDao teacherMessageSequenceDao) {
        List<TdApi.Message> messageList = Arrays.asList(messages.messages);
        List<TdApi.Message> unprocessedMessages = new ArrayList<>();
        messageList.forEach(message -> {
            LambdaQueryWrapper<TeacherMessageSequence> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(TeacherMessageSequence::getChatId, message.chatId);
            lambdaQueryWrapper.eq(TeacherMessageSequence::getMessageId, message.id);
            if (teacherMessageSequenceDao.selectCount(lambdaQueryWrapper) <= 0) {
                unprocessedMessages.add(message);
            }
        });
        return unprocessedMessages;
    }

    @Override
    public void process(List<TdApi.Chat> chats, MoistLifeApp moistLifeApp) {
        chats.forEach(chat -> process(chat, moistLifeApp));
    }
}
