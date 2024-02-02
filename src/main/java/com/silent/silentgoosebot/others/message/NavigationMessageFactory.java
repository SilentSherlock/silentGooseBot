package com.silent.silentgoosebot.others.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Date: 2024/2/2
 * Author: SilentSherlock
 * Description: create format message, classified by chatId
 */
public class NavigationMessageFactory {

    public static SendMessage createMessage(long chatId) {
        // TODO: 2024/2/2 通过反射构建消息发送器
        return new SendMessage();
    }

}
