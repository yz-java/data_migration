package com.yz.database;

import com.yz.dao.BaseDao;
import com.yz.datasources.DataSourceContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by yz on 17/2/8.
 */
@Component
public class MysqlOperation implements Operation {

    @Resource(name = "baseDao")
    private BaseDao baseDao;

    @Override
    public List<Map<String, Object>> queryTableSchema(String tableName) {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "show columns from "+tableName;
        List<Map<String, Object>> mapList = baseDao.getJdbcTemplate().queryForList(sql);
        return mapList;
    }

    @Override
    public List<String> queryTables() {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "show tables";
        List<String> strings = baseDao.getJdbcTemplate().queryForList(sql, String.class);
        return strings;
    }
}
