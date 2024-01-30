package com.silent.silentgoosebot.others;

import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * 消息接受bot,接受监听到的消息并进行转发
 */
@Slf4j
public class SilentGooseBot extends AbilityBot {


    public SilentGooseBot(String botToken, String botUsername) {
        super(botToken, botUsername);
    }

    public SilentGooseBot(String botToken, String botUsername, DefaultBotOptions options) {
        super(botToken, botUsername, options);
    }

//    @Override
//    public void onUpdatesReceived(List<Update> updates) {
//        log.info("bot receive message, begin distribute");
//        updates.stream()
//                .filter(Update::hasMessage)
//                .map(Update::getMessage)
//                .forEach(message -> {
//                    Chat curChat = message.getChat();
//                    log.info("curChat " + curChat.toString());
//                    log.info("curMsg " + message);
//                    if (curChat.isChannelChat()) {
//                        log.info("channel chat");
//                    } else if (curChat.isUserChat()) {
//                        log.info("user chat");
//                    }
//                });
//    }

    @Override
    public void onClosing() {

    }

    @Override
    public long creatorId() {
        return 0;
    }

    // Ability
    public Ability welcome() {
        return Ability.builder()
                .name("welcome")
                .info("welcome our new member")
                .locality(Locality.GROUP) //used in group
                .privacy(Privacy.PUBLIC) //access by every member
                .action(this::welcomeNewMember)
                .build();
    }


    private void welcomeNewMember(MessageContext messageContext) {
        log.info("welcome new member");
        Update update = messageContext.update();
        if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
            Message receiveMessage = update.getMessage();
            List<User> userList = receiveMessage.getNewChatMembers();

            long chatId = receiveMessage.getChatId();
            userList.forEach(user -> {
                log.info("we got a new member");
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Welcome " + user.getUserName() + " as our new member!");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    // Replay
    /**
     * called when new member join group update
     *
     * @return
     */
    public Reply sendWelcomeMessage() {
        // 使用Reply.of()方法创建一个Reply对象

        return Reply.of((baseAbilityBot, update) -> {
            var message = update.getMessage();
            // 检查消息是否包含新成员
            if (message.getNewChatMembers() != null) {
                // 获取群组ID和新成员的姓名
                long chatId = message.getChatId();
                var userList = message.getNewChatMembers();
                userList.forEach(user -> {
                    log.info("we got a new member");
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Welcome @" + user.getUserName() + " as our new member!");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, Update::hasMessage); // 设置Reply的触发条件，这里是收到消息
    }
}
