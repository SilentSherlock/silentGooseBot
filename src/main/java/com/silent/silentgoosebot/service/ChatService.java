package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.dao.ChannelDao;
import com.silent.silentgoosebot.dao.GroupMessageScheduleDao;
import com.silent.silentgoosebot.dao.MessageProcessorDao;
import com.silent.silentgoosebot.entity.Channel;
import com.silent.silentgoosebot.others.MoistLifeApp;
import it.tdlight.jni.TdApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * Date: 2023/11/7
 * Author: SilentSherlock
 * Description: describe the file
 */
@Slf4j
@Service
public class ChatService {

    @Resource
    private ChannelDao channelDao;
    @Resource
    private GroupMessageScheduleDao groupMessageScheduleDao;
    @Resource
    private MessageProcessorDao messageProcessorDao;

    public int addChannel(Channel channel) {
        return channelDao.insert(channel);
    }

    /**
     * 通过异步调用，同步等待的方式，获取聊天列表
     * @param moistLifeApp
     * @return
     */
    public List<TdApi.Chat> getClientChats(MoistLifeApp moistLifeApp) {
        // 最多获取1000个聊天对象
        CompletableFuture<TdApi.Chats> chatsCompletableFuture = moistLifeApp.getClient().send(new TdApi.GetChats(null, 1000));
        TdApi.Chats chats = chatsCompletableFuture.join();
        long[] chatIds = chats.chatIds;
        List<CompletableFuture<TdApi.Chat>> completableFutureList = new ArrayList<>();
        for (long chatId : chatIds) {
            completableFutureList.add(moistLifeApp.getClient().send(
                    new TdApi.GetChat(chatId)
            ));
        }
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).join();

        log.info("chat list already");

        return completableFutureList.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
