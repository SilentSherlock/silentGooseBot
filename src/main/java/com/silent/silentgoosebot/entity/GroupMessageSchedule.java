package com.silent.silentgoosebot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Date: 2024/2/6
 * Author: SilentSherlock
 * Description: describe the file
 */
@Data
@Accessors(chain = true)
public class GroupMessageSchedule {

    private Integer groupMessageScheduleId;
    private long chatId;
    private String messageType;
    private Integer autoMessageCreatorId;
    private long delay;
    private long period;

}
