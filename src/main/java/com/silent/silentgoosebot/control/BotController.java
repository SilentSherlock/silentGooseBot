package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.others.MoistLifeAppThread;
import com.silent.silentgoosebot.others.SilentGooseBot;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.BotUtils;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import jakarta.annotation.Resource;
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

    @RequestMapping(value = "/botStart", method = RequestMethod.GET)
    public void botStart() {

        DefaultBotOptions options = BotUtils.getDefaultOption();
        log.info("botToken:" + MyPropertiesUtil.getProperty(AppConst.Tg.bot_token));
        SilentGooseBot bot = new SilentGooseBot(
                MyPropertiesUtil.getProperty(AppConst.Tg.bot_token),
                MyPropertiesUtil.getProperty(AppConst.Tg.bot_username),
                options
        );

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @RequestMapping(value = "/appStart", method = RequestMethod.GET)
    public void appStart() throws Exception{
        //initialize native lib
        MoistLifeAppThread moistLifeAppThread = new MoistLifeAppThread();
        Thread thread = new Thread(moistLifeAppThread, "MoistLife86");
        thread.start();
    }
}
