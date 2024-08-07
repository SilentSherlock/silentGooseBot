package com.silent.silentgoosebot.others;

import com.silent.silentgoosebot.others.base.AppAccountMap;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import com.silent.silentgoosebot.others.utils.ContextUtils;
import it.tdlight.Init;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.util.UnsupportedNativeLibraryException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Date: 2023/12/6
 * Author: SilentSherlock
 * Description: my own tg application
 */
@Slf4j
@Data
public class MoistLifeApp {

    private final SimpleTelegramClient client;
    private final SimpleTelegramClientBuilder builder;
    private final SimpleAuthenticationSupplier<?> authenticationSupplier;
    private final boolean runFlag;

    public MoistLifeApp(@NotNull SimpleTelegramClientBuilder builder,
                        @NotNull SimpleAuthenticationSupplier<?> authenticationSupplier,
                        @NotNull SimpleTelegramClient client) {
        this.builder = builder;
        this.client = client;
        this.authenticationSupplier = authenticationSupplier;
        this.runFlag = true;
    }

    /**
     * start MoistLife by login
     * @param phoneNumber login account
     * @param handler handle login state status
     */
    public static MoistLifeApp login(String phoneNumber,
                                     GenericUpdateHandler<TdApi.UpdateAuthorizationState> handler) {

        log.info("MoistLifeApp starting!");
        MoistLifeApp moistLifeApp = null;
        try {
            Init.init();
        } catch (UnsupportedNativeLibraryException e) {
            throw new RuntimeException(e);
        }

        try {
            SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory();
            APIToken apiToken = new APIToken(Integer.parseInt(Objects.requireNonNull(MyPropertiesUtil.getProperty(AppConst.Tg.app_api_id))),
                    MyPropertiesUtil.getProperty(AppConst.Tg.app_api_hash));

            TDLibSettings settings = TDLibSettings.create(apiToken);
            //configure session
            Path sessionPath = Paths.get("tdlib-session-" + phoneNumber);
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

            //prepare a client builder
            SimpleTelegramClientBuilder builder = clientFactory.builder(settings);
            builder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, handler);

            //configure authentication
            SimpleAuthenticationSupplier<?> supplier = AuthenticationSupplier.user(phoneNumber);
            try {
                SimpleTelegramClient client = builder.build(supplier);
                moistLifeApp = new MoistLifeApp(builder, supplier, client);
                log.info("send proxy");
                TdApi.AddProxy proxy = new TdApi.AddProxy(
                        AppConst.Proxy.proxy_host,
                        AppConst.Proxy.proxy_port,
                        true,
                        new TdApi.ProxyTypeHttp()
                );

                client.send(proxy, result -> {
                    log.info("proxy set success");
                });
                log.info("context.setMoistLifeApp(moistLifeApp)");
                ContextUtils.getBean(AppAccountMap.class).getAccountMap().put(phoneNumber, moistLifeApp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Login end");
        return moistLifeApp;
    }



    /**
     * handle new Msg
     * @param update
     */
    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        //get msg content
        System.out.println("收到消息来自：" + update.message.senderId);
        TdApi.MessageContent messageContent = update.message.content;

        String text = "";
        if (messageContent instanceof TdApi.MessageText) {
            text = ((TdApi.MessageText) messageContent).text.text;
        } else {
            // return other msg's type
            text = messageContent.getClass().getSimpleName();
        }

        //send msg
        long chatId = update.message.chatId;
        String finalText = text;
        client.send(new TdApi.GetChat(chatId))
                .whenCompleteAsync((chatIdResult, error) -> {
                    if (error != null) {
                        log.error("return msg error");
                    } else {
                        // Get the chat name
                        String title = chatIdResult.title;
                        // Print the message
                        System.out.printf("Received new message from chat %s (%s): %s%n", title, chatId, finalText);
                    }
                });
    }


}
