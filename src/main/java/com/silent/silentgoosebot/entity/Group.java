package com.silent.silentgoosebot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class Group {

    @TableId
    private Integer groupTableId;
    private long groupId;
    private String groupName;
    private long groupMemberCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
