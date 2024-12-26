package com.silent.silentgoosebot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("message_statistics_schedule")
public class MessageStatisticsSchedule {

    @TableId
    private String messageStatisticScheduleId;
    private String phone;
    private long chatId;
    private String scheduleType;
    private String messageProcessorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
