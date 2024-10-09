package com.silent.silentgoosebot.others.processor;

import com.silent.silentgoosebot.others.MoistLifeApp;
import it.tdlight.jni.TdApi;

import java.util.List;

/**
 * Date: 2023/11/14
 * Author: SilentSherlock
 * Description: process channel msg with pattern
 */
public interface ChatsMessageProcessor {

    /**
     * process single message
     * @param chat message history in this chat will be fetched and processed
     */
    void process(TdApi.Chat chat, MoistLifeApp moistLifeApp);
    void process(List<TdApi.Chat> chats, MoistLifeApp moistLifeApp);

    /**
     *
     * @param message
     * @return true-unread
     */
    default boolean isMessageUnRead(TdApi.Message message) {
        return !message.isOutgoing && message.containsUnreadMention;
    }
}
