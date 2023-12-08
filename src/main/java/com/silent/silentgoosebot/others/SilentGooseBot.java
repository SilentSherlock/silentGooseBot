package com.silent.silentgoosebot.others;

import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * 消息接受bot,接受监听到的消息并进行转发
 */
@Slf4j
public class SilentGooseBot extends AbilityBot {


    public SilentGooseBot(String botToken, String botUsername) {
        super(botToken, botUsername);
    }

    public SilentGooseBot(String botToken, String botUsername, DefaultBotOptions options) {
        super(botToken, botUsername, options);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        log.info("bot receive message, begin distribute");
        updates.stream()
                .filter(Update::hasMessage)
                .map(Update::getMessage)
                .forEach(message -> {
                    Chat curChat = message.getChat();
                    log.info("curChat " + curChat.toString());
                    log.info("curMsg " + message);
                    if (curChat.isChannelChat()) {
                        log.info("channel chat");
                    } else if (curChat.isUserChat()) {
                        log.info("user chat");
                    }
                });
    }

    @Override
    public void onClosing() {

    }

    @Override
    public long creatorId() {
        return 0;
    }


}
