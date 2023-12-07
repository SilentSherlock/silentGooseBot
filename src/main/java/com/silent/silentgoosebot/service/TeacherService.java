package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.dao.TeacherDao;
import com.silent.silentgoosebot.entity.Teacher;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


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
}
