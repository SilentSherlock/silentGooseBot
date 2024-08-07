package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.SilentGooseBot;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.BotUtils;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @RequestMapping(value = "/appStart", method = RequestMethod.POST)
    public void appStart(String phone) throws Exception{
        if (StringUtils.isEmpty(phone)) {
            log.info("phone number is blank");
            return;
        }

        log.info("start app with phone {}", phone);

        MoistLifeApp.login(phone, update -> {
            TdApi.AuthorizationState state = update.authorizationState;
            if (state instanceof TdApi.AuthorizationStateReady) {
                log.info("user logged in, put moistLifeApp into context");
//                        context.setMoistLifeApp(moistLifeApp);
                // todo 添加登录完成逻辑
            } else if (state instanceof TdApi.AuthorizationStateClosing) {
                log.info("user closing");
            } else if (state instanceof TdApi.AuthorizationStateClosed) {
                log.info("user close");
            } else if (state instanceof TdApi.AuthorizationStateLoggingOut) {
                log.info("user logged out");
            } else if (state instanceof TdApi.AuthorizationStateWaitCode) {
                // todo 返回前端，要求输入验证码

            } else if (state instanceof TdApi.AuthorizationStateWaitPassword) {
                // 当状态为 AuthorizationStateWaitPassword 时，提示用户输入两步验证密码
                // todo 返回前端，要求输入两部验证码
            }
        });
    }
}
