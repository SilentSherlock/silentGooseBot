package com.silent.silentgoosebot.others;

import it.tdlight.client.SimpleAuthenticationSupplier;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.SimpleTelegramClientBuilder;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

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
        //add user log handler
        builder.addUpdateHandler(TdApi.UpdateUserStatus.class, this::onUpdateUserStatus);

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
        } else if (state instanceof TdApi.AuthorizationStateWaitCode) {
            System.out.println("请输入获取到的验证码");
            Scanner scanner = new Scanner(System.in);
            String code = scanner.nextLine();

            // 发送验证码到 TDLib
            client.send(new TdApi.CheckAuthenticationCode(code), result -> {
                if (result.isError()){
                    log.info("验证码错误");
                }
            });
        } else if (state instanceof TdApi.AuthorizationStateWaitPassword) {
            // 当状态为 AuthorizationStateWaitPassword 时，提示用户输入两步验证密码
            System.out.println("请输入您的两步验证密码:");
            Scanner scanner = new Scanner(System.in);
            String password = scanner.nextLine();

            // 发送两步验证密码到 TDLib
            client.send(new TdApi.CheckAuthenticationPassword(password), result -> {
                if (result.isError()) {
                    // 如果有错误，打印错误信息
                    System.out.println("两步验证密码验证失败: " + result.getError().message);
                } else {
                    // 如果验证成功，继续后续操作
                    System.out.println("两步验证密码验证成功!");
                }
            });
        }
    }

    /**
     * handle user login or logout
     * @param update
     */
    private void onUpdateUserStatus(TdApi.UpdateUserStatus update) {
        TdApi.UserStatus userStatus = update.status;
        long userid = update.userId;
        if (userStatus instanceof TdApi.UserStatusOffline) {
            log.info("user off line, real" + userid);
        } else if (userStatus instanceof TdApi.UserStatusOnline) {
            log.info("user on line, real" + userid);
        }
    }

    /**
     * handle new Msg
     * @param update
     */
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
