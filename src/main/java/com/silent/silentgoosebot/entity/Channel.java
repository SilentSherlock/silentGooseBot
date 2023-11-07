package com.silent.silentgoosebot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Channel {

    private String channelTableId;
    private String channelId;
    private String channelName;
    private String channelType;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;

    public static Channel createChannel() {
        return new Channel();
    }
}
