package com.silent.silentgoosebot.others;

import com.silent.silentgoosebot.others.base.AppConst;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.MessageFormat;
import java.util.Arrays;
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
        return 6558200354L;
    }

    // region Ability

    /**
     * get commands which can be used in this group
     * @return
     */
    public Ability navigate() {
        return Ability.builder()
                .name(AppConst.Tg.Command.NAVIGATION)
                .info("Get all command in this group")
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .action(this::getNavigation)
                .setStatsEnabled(true)
                .build();
    }

    private void getNavigation(MessageContext messageContext) {
        // TODO: 2024/1/31 查询数据库获取当前群组可以使用的命令列表返回
        log.info("navigate");
        // 命令列表
    }

    /**
     * start nav schedule in a group or all group
     * input group id or 'all' to specify which be start
     * @return
     */
    public Ability startGroupNavigateSchedule() {
        return Ability.builder()
                .name(AppConst.Tg.Command.START_GROUP_NAVIGATE_SCHEDULE)
                .info("start group navigate timer task")
                .input(1) // need one arg, 'all' or group id
                .locality(Locality.USER)
                .privacy(Privacy.ADMIN)
                .action(this::startGroupNavigateTimer)
                .build();
    }

    private void startGroupNavigateTimer(MessageContext messageContext) {

        log.info("startGroupNavigateTimer start");
        String[] args = messageContext.arguments();
        String groupId = "";
        if (null == args) groupId = "all";
        // TODO: 2024/1/31 根据groupId 查询对应的定时器配置，考虑把定时器单独写成类
    }

    /**
     * get all groups the bot managed
     * @return
     */
    public Ability groupsUnderWatch() {
        return Ability.builder()
                .name("getallgroups")
                .info("Get all command in this group")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(messageContext -> {
                    log.info("Get ALL groups");
                    SendMessage message = new SendMessage();
                    message.setChatId(messageContext.update().getMessage().getChatId());
                    message.setText("Get all groups under watch");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }

    private void getAllGroups(MessageContext messageContext) {
        log.info("Get all groups");
        // TODO: 2024/1/31 获取当前机器人管理的所有群组
    }

    public Ability playWithMe() {
        String playMessage = "Play with me!";

        return Ability.builder()
                .name("play")
                .info("Do you want to play with me?")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.ALL)
                .input(0)
                .action(ctx -> {log.info("play in");})
                .build();
    }

//    public Ability welcome() {
//        return Ability.builder()
//                .name("welcome")
//                .info("Get all command in this group")
//                .locality(Locality.ALL)
//                .privacy(Privacy.PUBLIC)
//                .action((messageContext) -> {log.info("we into welcome");})
//                .reply(sendWelcomeMessage())
//                .build();
//    }

    // This method defines an ability that responds to /link command
    public Ability linkAbility() {
        return Ability
                .builder()
                .name("link")
                .info("Sends an inline link with html syntax")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(this::sendLink)
                .build();
    }

    // This method sends an inline link with html syntax to the chat
    private void sendLink(MessageContext context) {
        // Create a SendMessage object
        SendMessage message = new SendMessage();

        // Set the chat id
        message.setChatId(context.chatId());

        // Set the text with html tags
        message.setText("这是一个使用html语法的<a href=\"https://github.com/rubenlagus/TelegramBots\">inline link</a>。");

        // Enable html parsing mode
        message.setParseMode("html");

        // Send the message using silent method
        silent.execute(message);
    }

    // endregion

    // region Reply
    /**
     * called when new member join group, send a welcome message
     * this message will be deleted in 5s
     * @return
     */
    public Reply sendWelcomeMessage() {
        // 使用Reply.of()方法创建一个Reply对象

        return Reply.of((baseAbilityBot, update) -> {
            log.info("sendWelcomeMessage receive new message");
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
                    // formant welcome msg with html
                    log.info("formant welcome msg with html");
                    MessageFormat messageFormat = new MessageFormat("欢迎<a href=\"{0}\">{1}</a>加入{2}!");

                    user.getFirstName();
                    String[] args = {
                            "tg://user?id=".concat(String.valueOf(user.getId())),
                            user.getFirstName()
                                    .concat(" ").
                                    concat(user.getLastName() == null ? "" : user.getLastName()),
                            message.getChat().getTitle()
                    };
                    log.info("args:" + Arrays.toString(args));
                    String welcomeText = messageFormat.format(args)/*.replace("<", "&lt;").replace(">", "&gt;")*/;
                    String countText = "消息将在" + count[0] + "秒后自毁";
                    welcome.setText(welcomeText.concat(countText));
                    welcome.setParseMode(ParseMode.HTML);
                    log.info(welcome.getText());

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
                                if (1 == count[0]) cancel();
                                String countText = "消息将在" + count[0] + "秒后自毁";
                                editMessageText.setText(welcomeText.concat(countText));
                                editMessageText.setParseMode(ParseMode.HTML);
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
                        timer.schedule(editTask, 1000, 1000);
                        timer.schedule(deleteTask, 5000);

                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                });
            }
        }, update -> update.hasMessage() && update.getMessage().getNewChatMembers().size() > 0); // 设置Reply的触发条件，这里是有新成员加入
    }




    // endregion
}
