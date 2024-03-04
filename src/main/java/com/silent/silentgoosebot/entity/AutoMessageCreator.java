package com.silent.silentgoosebot.entity;

import com.silent.silentgoosebot.others.base.MessageType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Date: 2024/2/6
 * Author: SilentSherlock
 * Description: describe the file
 */

@Data
@Accessors(chain = true)
public class AutoMessageCreator {

    private Integer autoMessageCreatorId;
    private String messageClassPath;
    private String methodName;
    private MessageType messageType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
