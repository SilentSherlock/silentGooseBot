package com.silent.silentgoosebot.others.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Date: 2023/12/13
 * Author: SilentSherlock
 * Description: offer method operate sqlite
 */

public class AppSqliteDataSource {


    private final DriverManagerDataSource dataSource;
    @Value("${sqliteDriverClass}")
    private String sqliteDriverClass;
    @Value("${sqliteDatasourceUrl}")
    private String sqliteDatasourceUrl;

    public AppSqliteDataSource() {
        this.dataSource = new DriverManagerDataSource();
//        this.dataSource.setDriverClassName(sqliteDriverClass);
        this.dataSource.setDriverClassName(Objects.requireNonNull(MyPropertiesUtil.getProperty("sqliteDriverClass")));
        this.dataSource.setUrl(Objects.requireNonNull(MyPropertiesUtil.getProperty("sqliteDatasourceUrl")));
    }

    public void insert() throws SQLException {
        dataSource.getConnection();
    }
}
