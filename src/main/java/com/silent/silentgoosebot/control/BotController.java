package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.others.SilentGooseBot;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Date: 2023/11/8
 * Author: SilentSherlock
 * Description: offer bot api
 */
@RestController
@Slf4j
public class BotController {

    @RequestMapping(value = "/botStart")
    public void botStart() {
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
}
