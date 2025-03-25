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

    /**
     * 抽取的公共预处理方法，将频道里的消息先落流水表，去掉已经落表的消息，最大消息数量设置默认值，同时各个实现类也可以自己指定
     * @param chat
     * @param moistLifeApp
     */
    default void preProcess(TdApi.Chat chat, MoistLifeApp moistLifeApp) {

    }
}
