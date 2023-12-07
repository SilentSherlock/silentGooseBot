package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.SilentGooseBot;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.BotUtils;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import it.tdlight.Init;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @RequestMapping(value = "/appStart")
    public void appStart() throws Exception{
        //initialize native lib
        Init.init();

        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()){
            APIToken apiToken = new APIToken(Integer.parseInt(Objects.requireNonNull(MyPropertiesUtil.getProperty(AppConst.Tg.app_api_id))),
                    MyPropertiesUtil.getProperty(AppConst.Tg.app_api_hash));

            TDLibSettings settings = TDLibSettings.create(apiToken);
            //configure session
            Path sessionPath = Paths.get("tdlib-session-57066");
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

            //prepare a client builder
            SimpleTelegramClientBuilder builder = clientFactory.builder(settings);

            //configure authentication
            SimpleAuthenticationSupplier<?> supplier = AuthenticationSupplier.user("+1 6124487478");
//            settings.setUseTestDatacenter(true);
            try (MoistLifeApp app = new MoistLifeApp(builder, supplier)){
                TdApi.User me = app.getClient().getMeAsync().get(1, TimeUnit.MINUTES);
                // Send a test message
                TdApi.SendMessage req = new TdApi.SendMessage();
                req.chatId = me.id;
                TdApi.InputMessageText txt = new TdApi.InputMessageText();
                txt.text = new TdApi.FormattedText("TDLight test", new TdApi.TextEntity[0]);
                req.inputMessageContent = txt;
                TdApi.Message result = app.getClient().sendMessage(req, true).get(1, TimeUnit.MINUTES);
                System.out.println("Sent message:" + result);
            }
        }
    }
}
