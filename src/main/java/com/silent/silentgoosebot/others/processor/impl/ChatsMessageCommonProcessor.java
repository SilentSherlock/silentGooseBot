package com.silent.silentgoosebot.others.processor.impl;

import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.processor.ChatsMessageProcessor;
import it.tdlight.jni.TdApi;

import java.util.List;

public class ChatsMessageCommonProcessor implements ChatsMessageProcessor {

    @Override
    public void process(TdApi.Chat chat, MoistLifeApp moistLifeApp, boolean firstFlag) {

    }

    @Override
    public void process(List<TdApi.Chat> chats, MoistLifeApp moistLifeApp, boolean firstFlag) {

    }
}
