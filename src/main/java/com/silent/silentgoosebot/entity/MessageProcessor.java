package com.silent.silentgoosebot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Date: 2023/11/6
 * Author: SilentSherlock
 * Description: describe the file
 */
@Data
@Accessors(chain = true)
public class MessageProcessor {

    @TableId
    private String messageProcessorId;
    private String channelId;
    private String processorClasspath;
    private String keywords;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
}
