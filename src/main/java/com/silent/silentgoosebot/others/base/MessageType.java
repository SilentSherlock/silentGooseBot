package com.silent.silentgoosebot.others.base;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * Date: 2024/2/2
 * Author: SilentSherlock
 * Description: describe the file
 */
public enum MessageType implements IEnum<String> {

    NAVIGATION("NAVIGATION"),
    WEATHER("WEATHER"),
    DRINK_WATER("DRINK_WATER");

    private final String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String getValue() {
        return this.messageType;
    }
}
