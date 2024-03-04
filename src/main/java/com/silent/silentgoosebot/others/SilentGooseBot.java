package com.silent.silentgoosebot.others;

import com.google.common.collect.Lists;
import com.silent.silentgoosebot.entity.GroupMessageSchedule;
import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.BotUtils;
import com.silent.silentgoosebot.others.base.MessageType;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import com.silent.silentgoosebot.others.timer.AutoMessageSendTimerTask;
import com.silent.silentgoosebot.service.TimerService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.MessageFormat;
import java.util.*;

/**
 * 消息接受bot,接受监听到的消息并进行转发
 */
@Slf4j
public class SilentGooseBot extends AbilityBot {

    @Resource
    private TimerService timerService;

    public SilentGooseBot(String botToken, String botUsername) {
        super(botToken, botUsername);
    }

    public SilentGooseBot(String botToken, String botUsername, DefaultBotOptions options) {
        super(botToken, botUsername, options);
    }

    @Override
    public void onClosing() {

    }

    @Override
    public long creatorId() {
        String id = MyPropertiesUtil.getProperty(AppConst.Tg.creator_id);
        if (StringUtils.isAllBlank(id)) {
            log.error("creator_id:{}", id);
            return 0;
        }
        log.info("creator_id:{}", id);
        return Long.parseLong(id);
    }

    // region Ability

    public Ability chatInfoInGroup() {
        return Ability.builder()
                .name(AppConst.Tg.Command.WHERE_AM_I)
                .info("start drink water")
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .input(0)
                .action(this::getChatInfoInGroup)
                .setStatsEnabled(true)
                .build();
    }

    private void getChatInfoInGroup(MessageContext messageContext) {
        log.info("Get current chat info in group");

        long chatId = messageContext.chatId();
        Message message = messageContext.update().getMessage();
        Chat chat = message.getChat();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(message.getMessageId());

        MessageFormat messageFormat = new MessageFormat("Hello, <a href={0}>{1}</a>, " +
                "You ard in a group, the group info blows\n" +
                "ChatId:{2}\n" +
                "ChatLink:{3}\n" +
                "ChatName:{4}\n" + "<b>Wish You Happy Here</b>");

        String[] args = {
                AppConst.Tg.User.link_prefix.concat(String.valueOf(message.getFrom().getId())),
                message.getFrom().getFirstName(),
                String.valueOf(chat.getId()),
                chat.getUserName(),
                chat.getTitle()
        };
        sendMessage.setText(messageFormat.format(args));
        sendMessage.setParseMode(ParseMode.HTML);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /*public Ability getAllDrinkWaterGroup() {
        return null;
    }*/


    private void getAllDrinkWaterGroup(MessageContext messageContext) {
        log.info("Get all drink water groups");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(messageContext.chatId());

        List<GroupMessageSchedule> schedules = timerService.selectSchedulesByMsgType(MessageType.DRINK_WATER);
        //schedules.forEach();
    }

    /**
     * start drink water timer in given chat group
     * input--chatId
     * @return
     */
    public Ability startDrinkWaterTimer() {
        return Ability.builder()
                .name(AppConst.Tg.Command.DRINK_WATER)
                .info("start drink water")
                .locality(Locality.USER)
                .privacy(Privacy.ADMIN)
                .input(1)
                .action(this::startDrinkWater)
                .setStatsEnabled(true)
                .build();
    }

    private void startDrinkWater(MessageContext messageContext) {
        log.info("start drink water timer");
        String[] args = messageContext.arguments();
        long chatId = 0L;
        if (null == args || args.length == 0) {
            log.info("drink water doesn't receive chatId, will start all drink water");
        } else {
            chatId = Long.parseLong(args[0]);
        }
        List<GroupMessageSchedule> schedules = new ArrayList<>();
        if (chatId != 0L) {
            schedules.add(timerService.selectScheduleByChatIdAndMsgType(chatId, MessageType.DRINK_WATER));
        } else {
            schedules.addAll(timerService.selectSchedulesByMsgType(MessageType.DRINK_WATER));
        }

        log.info("start timer");
        List<SendMessage> messages = new ArrayList<>();
        if (schedules.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(messageContext.chatId());
            sendMessage.setText("Schedules is empty");
            messages.add(sendMessage);
        } else {
            schedules.forEach(groupMessageSchedule -> {
                AutoMessageSendTimerTask.start(
                        this,
                        groupMessageSchedule,
                        new HashMap<>()
                );
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(messageContext.chatId());
                sendMessage.setText("Drink Water start in " + groupMessageSchedule.getChatId());
                messages.add(sendMessage);
            });
        }
        messages.forEach(sendMessage -> {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * get chat info by chat ID (Long or String start with @, 123456 or @IamAChat )
     * @return
     */
    public Ability chatInfo() {
        return Ability.builder()
                .name(AppConst.Tg.Command.CHAT_INFO)
                .info("Give Info of given chat")
                .input(1)
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action(this::getChatInfo)
                .setStatsEnabled(true)
                .build();
    }

    private void getChatInfo(MessageContext messageContext) {
        log.info("GetChatInfo start");

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(messageContext.chatId());
        String[] args = messageContext.arguments();
        if (null == args || args.length == 0) {
            log.info("Chat Info receive empty args, return");
            sendMessage.setText("Command eg: /chatinfo 123456 or @IamAChat");
        } else {
            String chatId = args[0];
            log.info("Chat Info receive args {}", chatId);
            if (!BotUtils.isChatId(chatId)) {
                sendMessage.setText("Chat Id illegal:" + chatId);
            } else {
                log.info("Get Chat Info");
                GetChat getChat = new GetChat();
                if (chatId.startsWith("@")) getChat.setChatId(chatId);
                else getChat.setChatId(Long.parseLong(chatId));
                try {
                    Chat chatInfo = execute(getChat);
                    if (null == chatInfo) {
                        sendMessage.setText("Doesn't get Chat with " + chatId);
                    } else {
                        sendMessage.setText(
                                "ChatId: " + chatInfo.getId() + "\n"
                                + "Title: " + chatInfo.getTitle() + "\n"
                                + "First: " + chatInfo.getFirstName() + "\n"
                                + "Last: " + chatInfo.getLastName() + "\n"
                                + "UserName: " + chatInfo.getUserName()
                        );
                    }
                } catch (TelegramApiException e) {
                    sendMessage.setText("Doesn't get Chat with " + chatId);
                    e.printStackTrace();
                }
            }
        }

        log.info("Send Result");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Send Chat Info Result Error");
            e.printStackTrace();
        }
    }


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
