package com.silent.silentgoosebot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Channel {

    @TableId
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
