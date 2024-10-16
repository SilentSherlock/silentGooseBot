package com.silent.silentgoosebot.schedule;

import com.silent.silentgoosebot.entity.MessageProcessor;
import com.silent.silentgoosebot.entity.MessageStatisticsSchedule;
import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppAccountMap;
import com.silent.silentgoosebot.others.processor.ChatsMessageProcessor;
import com.silent.silentgoosebot.service.ChatService;
import com.silent.silentgoosebot.service.MessageProcessorService;
import com.silent.silentgoosebot.service.MessageStatisticsScheduleService;
import it.tdlight.jni.TdApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Component
public class ChatSchedule {

    @Resource
    private AppAccountMap appAccountMap;
    @Resource
    private ChatService chatService;
    @Resource
    private MessageStatisticsScheduleService messageStatisticsScheduleService;
    @Resource
    private MessageProcessorService messageProcessorService;

    /**
     * 启动scheduleType=0的定时任务
     */
    @Scheduled(cron = "")
    public void JobScheduleType0() {
        String scheduleType = "0";
        List<MessageStatisticsSchedule> messageStatisticsScheduleList = messageStatisticsScheduleService.getMessageStatisticsScheduleByType(scheduleType);
        messageStatisticsScheduleList.forEach(messageStatisticsSchedule -> {
            log.info("schedule type {} start", scheduleType);
            MoistLifeApp moistLifeApp = appAccountMap.getAccountMap().get(messageStatisticsSchedule.getPhone());
            MessageProcessor messageProcessor = messageProcessorService.selectById(messageStatisticsSchedule.getMessageProcessorId());
            ForkJoinPool.commonPool().submit(() -> {
                log.info("异步启动线程处理chatId {}, phone {}", messageStatisticsSchedule.getChatId(), messageStatisticsSchedule.getPhone());
                String processorClasspath = messageProcessor.getProcessorClasspath();
                log.info("反射获取processorClasspath {}", processorClasspath);
                try {
                    Class<?> clazz = Class.forName(processorClasspath);
                    ChatsMessageProcessor chatsMessageProcessor = (ChatsMessageProcessor) clazz.getDeclaredConstructor().newInstance();
                    // 异步获取账号对应的聊天，等待结果返回后处理
                    CompletableFuture<TdApi.Chat> chatCompletableFuture = moistLifeApp.getClient().send(new TdApi.GetChat(messageStatisticsSchedule.getChatId()));
                    TdApi.Chat chat = chatCompletableFuture.join();
                    chatsMessageProcessor.process(chat, moistLifeApp);
                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}
