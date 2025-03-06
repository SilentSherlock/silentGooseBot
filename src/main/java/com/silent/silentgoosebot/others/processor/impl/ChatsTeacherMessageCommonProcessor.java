package com.silent.silentgoosebot.others.processor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silent.silentgoosebot.dao.TeacherDao;
import com.silent.silentgoosebot.dao.TeacherMessageSequenceDao;
import com.silent.silentgoosebot.entity.Teacher;
import com.silent.silentgoosebot.entity.TeacherMessageSequence;
import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.IdGenerator;
import com.silent.silentgoosebot.others.processor.ChatsMessageProcessor;
import com.silent.silentgoosebot.others.utils.ContextUtils;
import com.silent.silentgoosebot.others.utils.ProcessUtils;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
                TdApi.MessageContent content = message.content;
                if (content instanceof TdApi.MessagePhoto messagePhoto) {
                    log.info("photo {}", messagePhoto.caption.text);
                    String text = messagePhoto.caption.text;
                    text = ProcessUtils.teacherMessagePreProcess(text);
                    log.info("photo text {}", text);
                    //跳出lambda
                    if (text.isBlank()) return;
                    Map<String, String> keyValueMap = ProcessUtils.extractKeyValue(text);
                    if (keyValueMap.isEmpty()) return;
                    Teacher teacher = new Teacher();
                    teacher.setTeacherTableId(IdGenerator.getNextTeacherId());
                    teacher.setLastLoginStatus("");
                    for (int i = 0; i < ProcessUtils.teacherKeys.length; i++) {
                        String[] teacherKey = ProcessUtils.teacherKeys[i];
                        for (String k : teacherKey) {
                            if (keyValueMap.containsKey(k)) {
                                String v = keyValueMap.get(k);
                                // todo 完善字段映射
                                switch (i) {
                                    case 0:
                                        teacher.setTeacherName(v);
                                        break;
                                    case 1:
                                        teacher.setLocation(v);
                                        break;
                                    case 2:
                                        BigDecimal[] pricePGroup = ProcessUtils.extractPriceNumber(v);
                                        teacher.setPriceP(pricePGroup[0]);
                                        break;
                                    case 3:
                                        BigDecimal[] pricePpGroup = ProcessUtils.extractPriceNumber(v);
                                        teacher.setPricePp(pricePpGroup[0]);
                                        break;
                                    case 4:
//                                        teacher.setPriceNight(new java.math.BigDecimal(v));
                                        break;
                                    case 5:
                                        teacher.setTeacherId(ProcessUtils.extractUserId(v));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    TeacherDao teacherDao = ContextUtils.getBean(TeacherDao.class);
                    teacher.setUpdateTime(LocalDateTime.now());
                    teacher.setInsertTime(LocalDateTime.now());
                    teacherDao.insert(teacher);

                } else if (content instanceof TdApi.MessageText messageText) {
                    log.info("text {}", messageText.text.text);
                } else {
                    log.info("unknown message type {}", content.getClass());
                }

            });

            if (messages.messages.length > 0) {
                fromMessageId = messages.messages[messages.messages.length - 1].id;
            }
            totalMessages += messages.messages.length;
            if (messages.messages.length < 1 || totalMessages >= AppConst.Tg.message_max_size) {
                log.info("stop loop, stop condition is messages length {} or totalMessages {}", messages.messages.length, totalMessages);
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
