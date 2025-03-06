package com.silent.silentgoosebot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silent.silentgoosebot.entity.Teacher;

import java.util.List;

/**
 * Date: 2023/11/6
 * Author: SilentSherlock
 * Description: describe the file
 */
public interface TeacherDao extends BaseMapper<Teacher> {
    List<String> getEmptyLoginStatusTeacherId();
}
