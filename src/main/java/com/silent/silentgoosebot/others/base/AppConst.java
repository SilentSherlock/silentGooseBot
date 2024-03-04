package com.silent.silentgoosebot.others.base;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Date: 2023/11/6
 * Author: SilentSherlock
 * Description: contains const and some useful method
 */
public class AppConst {

    public static final String date_format = "yyyy-MM-dd HH:mm:ss";

    public static String getFormatDate(Date date) {
        if (date == null) date = new Date();
        return new SimpleDateFormat(date_format, Locale.CHINA).format(date);
    }

    public static String getFormatDate(LocalDateTime localDateTime) {
        if (localDateTime == null) localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern(date_format));
    }

    public interface Tg {
        String bot_token = "bot_token";
        String bot_username = "bot_username";
        String app_api_id = "app_api_id";
        String app_api_hash = "app_api_hash";
        String user_phone_number = "user_phone_number";
        String prefix = "https://t.me/";
        String creator_id = "creator_id";

        interface Group {
            String welcome = "欢迎新成员加入";
        }

        interface User {
            String link = "@";
            String link_prefix = "tg://user?id=";
        }

        interface Command {
            // bot commands must be lowercase
            String WELCOME = "welcome";
            String NAVIGATION = "navigation";
            String CHOOSE_HOOKER = "choose_hooker";
            String START_GROUP_NAVIGATE_SCHEDULE = "startgroupnavigateschedule";
            String GROUPS_UNDER_WATCH = "groupsUnderWatch";
            String DRINK_WATER = "drinkwater";
            String CHAT_INFO = "chatinfo";
            String WHERE_AM_I = "whereami";
        }
    }

    public interface Proxy {
        String proxy_host = "127.0.0.1";
        int proxy_port = 10809;
    }
}
