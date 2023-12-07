package com.silent.silentgoosebot.others;

import it.tdlight.client.SimpleAuthenticationSupplier;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.SimpleTelegramClientBuilder;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

/**
 * Date: 2023/12/6
 * Author: SilentSherlock
 * Description: my own tg application
 */
@Slf4j
public class MoistLifeApp implements AutoCloseable{

    private final SimpleTelegramClient client;

    public SimpleTelegramClient getClient() {
        return client;
    }

    public MoistLifeApp(SimpleTelegramClientBuilder builder, SimpleAuthenticationSupplier<?> authenticationSupplier) {

        //add start handler
        builder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);
        //add msg handler
        builder.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);

        this.client = builder.build(authenticationSupplier);
    }

    /**
     * handle user state update
     * @param update
     */
    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
        TdApi.AuthorizationState state = update.authorizationState;
        if (state instanceof TdApi.AuthorizationStateReady) {
            log.info("user logged in");
        } else if (state instanceof TdApi.AuthorizationStateClosing) {
            log.info("user closing");
        } else if (state instanceof TdApi.AuthorizationStateClosed) {
            log.info("user close");
        } else if (state instanceof TdApi.AuthorizationStateLoggingOut) {
            log.info("user logged out");
        }
    }

    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        //get msg content
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

    @Override
    public void close() throws Exception {
        client.close();
    }
}
