package com.silent.silentgoosebot.entity;

import com.silent.silentgoosebot.others.base.Role;
import lombok.Data;

@Data
public class User {

    private String id;
    private String email;
    private String username;
    private String password;
    private Role role;

}
