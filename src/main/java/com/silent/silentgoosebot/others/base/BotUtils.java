package com.silent.silentgoosebot.others.base;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;

/**
 * Date: 2023/11/14
 * Author: SilentSherlock
 * Description: offer static method for bot
 */
@Slf4j
public class BotUtils {

    public static DefaultBotOptions getDefaultOption() {
        DefaultBotOptions options = new DefaultBotOptions();
        options.setProxyHost(AppConst.Proxy.proxy_host);
        options.setProxyPort(AppConst.Proxy.proxy_port);
        options.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        return options;
    }

    /**
     * match chatId pattern
     * @param chatId Match 10 to 13 digits, a string of pure numbers
     *               starting with -, optional, and match 5 to 32 digits,
     *               a string of alphanumeric characters starting with @, optional
     * @return
     */
    public static boolean isChatId(String chatId) {
        return chatId.matches("-?\\d{10,13}") || chatId.matches("@?(\\w{5,32})");
    }
}
