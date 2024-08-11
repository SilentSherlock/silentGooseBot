package com.silent.silentgoosebot.service;

import com.silent.silentgoosebot.dao.TgAccountDao;
import com.silent.silentgoosebot.entity.TgAccount;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TgAccountService {

    @Resource
    private TgAccountDao tgAccountDao;

    public List<TgAccount> getAllAccounts() {
        return tgAccountDao.selectAllAccount();
    }

    public int addAccount(TgAccount account) {
        return tgAccountDao.insert(account);
    }
}
