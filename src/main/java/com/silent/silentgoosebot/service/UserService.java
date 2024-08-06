package com.silent.silentgoosebot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silent.silentgoosebot.dao.UserDao;
import com.silent.silentgoosebot.entity.User;
import com.silent.silentgoosebot.others.base.Result;
import jakarta.annotation.Resource;
import org.jvnet.hk2.annotations.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public Result checkUser(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, user.getEmail());
        wrapper.eq(User::getPassword, user.getPassword());
        User loginUser = userDao.selectOne(wrapper);

        if (loginUser == null) {
            return Result.createByFalse("screw you, user not exist");
        }
        Result result = Result.createBySuccess("user login success");
        Map<String, Object> data = new HashMap<>();
        data.put("token", "this is your fucking token");
        result.setResultMap(data);
        return result;
    }

}
