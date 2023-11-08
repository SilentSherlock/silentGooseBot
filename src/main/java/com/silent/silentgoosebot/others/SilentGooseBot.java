package com.silent.silentgoosebot.others;

import com.silent.silentgoosebot.service.MessageProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息接受bot,接受监听到的消息并进行转发
 */
@Slf4j
@Component
public class SilentGooseBot extends AbilityBot {

    @Resource
    private MessageProcessorService messageProcessorService;

    protected SilentGooseBot(String botToken, String botUsername) {
        super(botToken, botUsername);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        log.info("bot receive message, begin distribute");
        updates.stream()
                .filter(Update::hasMessage)
                .map(Update::getMessage)
                .forEach(message -> {

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
