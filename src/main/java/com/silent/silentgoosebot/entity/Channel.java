package com.silent.silentgoosebot.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Channel {

    private String channelTableId;
    private String channelId;
    private String channelName;
    private String channelType;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;

}
