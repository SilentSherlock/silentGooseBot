package com.silent.silentgoosebot.others.timer;

import com.silent.silentgoosebot.others.SilentGooseBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.TimerTask;

/**
 * Date: 2024/2/1
 * Author: SilentSherlock
 * Description: auto send Message to the specify group, scheduled by given delay and period,
 * the chatId will decide which Message will be
 */
@Slf4j
public class AutoMessageSendTimer extends TimerTask {

    private SilentGooseBot silentGooseBot;
    private long chatId;
    private long delay;
    private long period;

    private AutoMessageSendTimer(SilentGooseBot silentGooseBot, long chatId, long delay, long period) {
        this.silentGooseBot = silentGooseBot;
        this.chatId = chatId;
        this.delay = delay;
        this.period = period;
    }

    public static void start(SilentGooseBot silentGooseBot,
                             long chatId,
                             long delay,
                             long period
    ) {
        log.info("build SendNavigationTimer");
        AutoMessageSendTimer timer = new AutoMessageSendTimer(silentGooseBot, chatId, delay, period);

    }

    @Override
    public void run() {
        log.info("SendNavigationTimer Task start!");
        log.info("Current ChatId:{}, Delay:{}, Period:{}", chatId, delay, period);

        //send pre-format text, parse as html
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        // TODO: 2024/2/1 edit text
        sendMessage.setParseMode(ParseMode.HTML);

        try {
            silentGooseBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("SendNavigationTimer Task Get Wrong, {}", e.getMessage());
            e.printStackTrace();
        }

    }
}
