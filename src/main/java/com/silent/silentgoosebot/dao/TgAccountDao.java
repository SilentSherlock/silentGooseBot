package com.silent.silentgoosebot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silent.silentgoosebot.entity.TgAccount;
import java.util.List;

public interface TgAccountDao extends BaseMapper<TgAccount> {

    List<TgAccount> selectAllAccount();
}
