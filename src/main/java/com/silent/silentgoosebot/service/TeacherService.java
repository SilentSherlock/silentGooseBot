package com.silent.silentgoosebot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.silent.silentgoosebot.dao.TeacherDao;
import com.silent.silentgoosebot.entity.Teacher;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Date: 2023/11/6
 * Author: SilentSherlock
 * Description: describe the file
 */
@Service
public class TeacherService {

    @Resource
    private TeacherDao teacherDao;

    public int addTeacher(Teacher teacher) {
        return teacherDao.insert(teacher);
    }

    public List<Teacher> getEmptyLoginStatusTeacherId() {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Teacher::getTeacherId)
                .select(Teacher::getTeacherTableId)
                .eq(Teacher::getLastLoginStatus, "")
                .or()
                .eq(Teacher::getLastLoginStatus, null);
        return teacherDao.selectList(wrapper);
    }

    public void updateTeacherListById(List<Teacher> teachers) {
        LambdaUpdateWrapper<Teacher> wrapper = new LambdaUpdateWrapper<>();
        teachers.forEach(teacher -> {
            wrapper.set(Teacher::getLastLoginStatus, teacher.getLastLoginStatus())
                    .eq(Teacher::getTeacherTableId, teacher.getTeacherTableId());
            teacherDao.update(null, wrapper);
        });
    }
}
