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
     * @param firstFlag true--message history never processed, will fetch message history up to message_max_size, even if the message viewed or not
     */
    void process(TdApi.Chat chat, MoistLifeApp moistLifeApp, boolean firstFlag);
    void process(List<TdApi.Chat> chats, MoistLifeApp moistLifeApp, boolean firstFlag);

    /**
     *
     * @param message
     * @return true-unread
     */
    default boolean isMessageUnRead(TdApi.Message message) {
        return !message.isOutgoing && message.containsUnreadMention;
    }
}
