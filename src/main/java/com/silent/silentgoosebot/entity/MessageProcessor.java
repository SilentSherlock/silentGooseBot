package com.silent.silentgoosebot.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Date: 2023/11/6
 * Author: SilentSherlock
 * Description: describe the file
 */
@Data
public class MessageProcessor {

    private String messageProcessorId;
    private String channelId;
    private String processorClasspath;
    private String keywords;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
}
