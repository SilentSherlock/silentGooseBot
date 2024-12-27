package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.entity.MessageProcessor;
import com.silent.silentgoosebot.entity.MessageStatisticsSchedule;
import com.silent.silentgoosebot.entity.TgAccount;
import com.silent.silentgoosebot.others.base.IdGenerator;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ServiceTest {

    @Resource
    private TgAccountService tgAccountService;
    @Resource
    private MessageProcessorService messageProcessorService;
    @Resource
    private MessageStatisticsScheduleService messageStatisticsScheduleService;

    @Test
    public void insertTgAccount() {
        List<TgAccount> accounts = new ArrayList<>();
        TgAccount tgAccount = new TgAccount();
        tgAccount.setTgAccountId(IdGenerator.getNextTgAccountId());
        tgAccount.setUsername("@dirtygoos");
        tgAccount.setAvatarName("Nick Mag");
        tgAccount.setPhone("+44 7917 941607");
        accounts.add(tgAccount);

        accounts.forEach(tgAccount1 -> {
            tgAccountService.addAccount(tgAccount);
        });
    }

    @Test
    public void insertMessageProcessor() {
        List<MessageProcessor> messageProcessors = new ArrayList<>();
        MessageProcessor messageProcessor = new MessageProcessor();
        messageProcessor.setMessageProcessorId(IdGenerator.getNextMessageProcessorId());
        messageProcessor.setChannelId("-1001692633324");
        messageProcessor.setProcessorClasspath("com.silent.silentgoosebot.others.processor.impl.ChatsTeacherMessageCommonProcessor");
        messageProcessors.add(messageProcessor);
        messageProcessor.setInsertTime(LocalDateTime.now());
        messageProcessor.setUpdateTime(LocalDateTime.now());

        messageProcessors.forEach(messageProcessor1 -> {
            messageProcessorService.addMessageProcessor(messageProcessor1);
        });
    }

    @Test
    public void insertMessageStatisticsSchedule() {
        List<MessageStatisticsSchedule> messageStatisticsSchedules = new ArrayList<>();
        MessageStatisticsSchedule messageStatisticsSchedule = new MessageStatisticsSchedule();
        messageStatisticsSchedule.setMessageStatisticScheduleId(IdGenerator.getNextMessageStatisticsScheduleId());
        messageStatisticsSchedule.setPhone("+44 7917 941607");
        messageStatisticsSchedule.setChatId(-1001692633324L);
        messageStatisticsSchedule.setScheduleType("0");
        messageStatisticsSchedule.setMessageProcessorId("");
        messageStatisticsSchedule.setCreateTime(LocalDateTime.now());
        messageStatisticsSchedule.setUpdateTime(LocalDateTime.now());
        messageStatisticsSchedules.add(messageStatisticsSchedule);
        messageStatisticsSchedules.forEach(messageStatisticsSchedule1 -> {
            messageStatisticsScheduleService.addMessageStatisticsSchedule(messageStatisticsSchedule1);
        });

    }
}
