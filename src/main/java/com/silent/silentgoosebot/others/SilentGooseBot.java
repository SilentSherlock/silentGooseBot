package com.silent.silentgoosebot.others;

import com.silent.silentgoosebot.others.base.AppConst;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    // region Ability

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

    // endregion

    // region Reply
    /**
     * called when new member join group, send a welcome message
     * this message will be delete in 5s
     * @return
     */
    public Reply sendWelcomeMessage() {
        // 使用Reply.of()方法创建一个Reply对象

        return Reply.of((baseAbilityBot, update) -> {
            Message message = update.getMessage();
            // 检查消息是否包含新成员
            if (message.getNewChatMembers() != null) {
                // 获取群组ID和新成员的姓名
                long chatId = message.getChatId();
                List<User> userList = message.getNewChatMembers();
                userList.forEach(user -> {
                    log.info("we got a new member");
                    SendMessage welcome = new SendMessage();
                    welcome.setChatId(chatId);
                    final int[] count = {5};
                    String welcomeText = AppConst.Tg.Group.welcome
                            + message.getChat().getTitle()
                            + AppConst.Tg.User.link
                            + user.getUserName().concat("\n");
                    String countText = "该条消息将在" + count[0] + "秒后删除";
                    welcome.setText(welcomeText.concat(countText));
                    try {
                        log.info("send welcome text");
                        Message sentMessage = execute(welcome);

                        log.info("edit message text schedule task");
                        int sentMessageId = sentMessage.getMessageId();
                        TimerTask editTask = new TimerTask() {
                            @SneakyThrows
                            @Override
                            public void run() {
                                EditMessageText editMessageText = new EditMessageText();
                                editMessageText.setMessageId(sentMessageId);
                                editMessageText.setChatId(chatId);
                                count[0]--;
                                String countText = "该条消息将在" + count[0] + "秒后删除";
                                editMessageText.setText(welcomeText.concat(countText));
                                execute(editMessageText);
                            }
                        };

                        log.info("delete message schedule task");
                        TimerTask deleteTask = new TimerTask() {
                            @SneakyThrows
                            @Override
                            public void run() {
                                DeleteMessage deleteMessage = new DeleteMessage();
                                deleteMessage.setMessageId(sentMessageId);
                                deleteMessage.setChatId(chatId);
                                execute(deleteMessage);
                            }
                        };

                        log.info("set timer execute schedules");
                        Timer timer = new Timer();
                        timer.schedule(editTask, 1000);
                        timer.schedule(deleteTask, 5000);

                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                });
            }
        }, Update::hasMessage); // 设置Reply的触发条件，这里是收到消息
    }

    // endregion
}
