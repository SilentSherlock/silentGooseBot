package com.silent.silentgoosebot.others.message;

import com.silent.silentgoosebot.entity.AutoMessageCreator;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Date: 2024/2/2
 * Author: SilentSherlock
 * Description: create format message, classified by chatId
 */
@Slf4j
public class AutoMessageCreateFactory {

    public static SendMessage createMessage(AutoMessageCreator creator) {

        log.info("Create message : " + creator.toString());

        return new SendMessage();
    }

    /**
     * used in groups, send navi message
     * @return
     */
    public SendMessage createNavigationPrototype() {
        SendMessage sendMessage = new SendMessage();
        return sendMessage;
    }

}
