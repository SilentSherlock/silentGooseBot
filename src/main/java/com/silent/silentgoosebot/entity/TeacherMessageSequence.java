package com.silent.silentgoosebot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TeacherMessageSequence {

    @TableId
    private Integer teacherMessageSequenceId;
    private long chatId;
    private long messageId;
    private String teacherMessage;
    private LocalDateTime createTime;
    private LocalDateTime updatedTime;

}
