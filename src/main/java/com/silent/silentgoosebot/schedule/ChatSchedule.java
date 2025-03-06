package com.silent.silentgoosebot.schedule;

import com.silent.silentgoosebot.entity.MessageProcessor;
import com.silent.silentgoosebot.entity.MessageStatisticsSchedule;
import com.silent.silentgoosebot.entity.Teacher;
import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppAccountMap;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.processor.ChatsMessageProcessor;
import com.silent.silentgoosebot.service.ChatService;
import com.silent.silentgoosebot.service.MessageProcessorService;
import com.silent.silentgoosebot.service.MessageStatisticsScheduleService;
import com.silent.silentgoosebot.service.TeacherService;
import it.tdlight.jni.TdApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
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
    @Resource
    private TeacherService teacherService;

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

    /**
     * 补充teacher表上线状态定时任务
     */
    @Scheduled(cron = "")
    public void teacherLoginStatusSchedule() {
        log.info("teacherLoginStatusSchedule start");

        List<Teacher> emptyStatusTeachers = teacherService.getEmptyLoginStatusTeacherId();
        emptyStatusTeachers.forEach(teacher -> {
            log.info("teacherId {} teacherTableId {} ",
                    teacher.getTeacherId(), teacher.getTeacherTableId());
            Map<String, MoistLifeApp> accountMap = appAccountMap.getAccountMap();
            if (accountMap.isEmpty()) return;
            MoistLifeApp moistLifeApp = accountMap.get(accountMap.keySet().stream().toList().get(0));
            ForkJoinPool.commonPool().submit(() -> {
                for (Teacher emptyStatusTeacher : emptyStatusTeachers) {
                    CompletableFuture<TdApi.Chat> chatCompletableFuture = moistLifeApp.getClient().send(new TdApi.SearchPublicChat(emptyStatusTeacher.getTeacherId()));
                    TdApi.Chat chat = chatCompletableFuture.join();
                    if (chat.type instanceof TdApi.ChatTypePrivate) {
                        long userId = chat.id;
                        CompletableFuture<TdApi.User> userCompletableFuture = moistLifeApp.getClient().send(new TdApi.GetUser(userId));
                        TdApi.User user = userCompletableFuture.join();
                        if (user.status instanceof TdApi.UserStatusOnline) {
                            teacher.setLastLoginStatus(AppConst.Tg.User.status_online);
                        } else if (user.status instanceof TdApi.UserStatusRecently) {
                            teacher.setLastLoginStatus(AppConst.Tg.User.status_recently);
                        } else if (user.status instanceof TdApi.UserStatusLastWeek) {
                            teacher.setLastLoginStatus(AppConst.Tg.User.status_LastWeek);
                        } else if (user.status instanceof TdApi.UserStatusLastMonth) {
                            teacher.setLastLoginStatus(AppConst.Tg.User.status_lastMonth);
                        } else if (user.status instanceof TdApi.UserStatusOffline) {
                            teacher.setLastLoginStatus(AppConst.Tg.User.status_offline);
                        } else {
                            teacher.setLastLoginStatus(AppConst.Tg.User.status_Empty);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        teacherService.updateTeacherListById(emptyStatusTeachers);
    }
}
