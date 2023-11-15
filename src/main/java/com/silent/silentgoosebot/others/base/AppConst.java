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

    public interface Bot {
        String bot_token = "bot_token";
        String bot_username = "bot_username";
    }

    public interface Proxy {
        String proxy_host = "127.0.0.1";
        int proxy_port = 10809;
    }
}
