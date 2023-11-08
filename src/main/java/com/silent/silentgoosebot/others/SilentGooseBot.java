package com.silent.silentgoosebot.others;

import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import com.silent.silentgoosebot.service.MessageProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.toggle.AbilityToggle;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息接受bot,接受监听到的消息并进行转发
 */
@Slf4j
public class SilentGooseBot extends AbilityBot {

    public static void main(String[] args) {

        DefaultBotOptions options = new DefaultBotOptions();
        options.setProxyHost("127.0.0.1");
        options.setProxyPort(10809);
        options.setProxyType(DefaultBotOptions.ProxyType.HTTP);

        log.info("botToken:" + MyPropertiesUtil.getProperty(AppConst.Bot.bot_token));
        SilentGooseBot bot = new SilentGooseBot(
                MyPropertiesUtil.getProperty(AppConst.Bot.bot_token),
                MyPropertiesUtil.getProperty(AppConst.Bot.bot_username),
                options
        );

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

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
