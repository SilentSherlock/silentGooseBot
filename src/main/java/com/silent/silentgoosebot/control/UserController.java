package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.entity.User;
import com.silent.silentgoosebot.others.base.Result;
import com.silent.silentgoosebot.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Manage
 */
@RestController
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {
        return userService.checkUser(user);
    }

}
