package com.silent.silentgoosebot.others.timer;

import com.silent.silentgoosebot.entity.AutoMessageCreator;
import com.silent.silentgoosebot.entity.GroupMessageSchedule;
import com.silent.silentgoosebot.others.SilentGooseBot;
import com.silent.silentgoosebot.others.message.AutoMessageCreateFactory;
import com.silent.silentgoosebot.others.utils.ContextUtils;
import com.silent.silentgoosebot.service.TimerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Date: 2024/2/1
 * Author: SilentSherlock
 * Description: auto send Message to the specify group, scheduled by given delay and period,
 * the chatId will decide which Message will be
 */
@Slf4j
public class AutoMessageSendTimerTask extends TimerTask {

    private TimerService timerService;
    private SilentGooseBot silentGooseBot;
    private Map<String, Object> dataMap;
    private GroupMessageSchedule groupMessageSchedule;

    private AutoMessageSendTimerTask(SilentGooseBot silentGooseBot,
                                     Map<String, Object> dataMap,
                                     GroupMessageSchedule groupMessageSchedule
                                 ) {
        this.silentGooseBot = silentGooseBot;
        this.dataMap = dataMap;
        this.groupMessageSchedule = groupMessageSchedule;
        this.timerService = ContextUtils.getBean(TimerService.class);
    }

    /**
     *
     * @param silentGooseBot
     * @param groupMessageSchedule
     * @param dataMap send data used in building message(SendMessage, SendPhoto, etc)
     */
    public static void start(SilentGooseBot silentGooseBot,
                             GroupMessageSchedule groupMessageSchedule,
                             Map<String, Object> dataMap
    ) {
        log.info("build SendNavigationTimer");
        log.info("groupMessageSchedule:{}", groupMessageSchedule.toString());
        AutoMessageSendTimerTask timerTask = new AutoMessageSendTimerTask(silentGooseBot, dataMap, groupMessageSchedule);
        Timer timer = new Timer();
        if (null != groupMessageSchedule.getStartTime()) {
            timer.scheduleAtFixedRate(timerTask, groupMessageSchedule.getStartTime(), groupMessageSchedule.getPeriod());
        } else {
            timer.schedule(timerTask, groupMessageSchedule.getDelay(), groupMessageSchedule.getPeriod());
        }
    }

    @Override
    public void run() {
        log.info("SendNavigationTimer Task start!");

        //send pre-format text, parse as html
        AutoMessageCreator autoMessageCreator = timerService.selectCreatorByCreatorId(
                groupMessageSchedule.getAutoMessageCreatorId()
        );
        Object message = AutoMessageCreateFactory.createMessage(autoMessageCreator, dataMap);
        if (null == message) {
            log.error("Auto create message failed");
            cancel();
        }

        try {
            if (message instanceof SendMessage) {
                SendMessage send = (SendMessage) message;
                send.setChatId(groupMessageSchedule.getChatId());
                silentGooseBot.execute(send);
            } else if (message instanceof SendPhoto) {
                SendPhoto send = (SendPhoto) message;
                send.setChatId(groupMessageSchedule.getChatId());
                silentGooseBot.execute(send);
            }
        } catch (TelegramApiException e) {
            log.error("SendNavigationTimer Task Get Wrong, {}", e.getMessage());
            e.printStackTrace();
            cancel();
        }

    }
}
