package com.yz.database;

import com.yz.dao.BaseDao;
import com.yz.datasources.DataSourceContextHolder;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by yz on 17/2/9.
 */
@Component("sqlServerOperation")
public class SQLServerOperation implements Operation {

    @Resource(name = "baseDao")
    private BaseDao baseDao;

    @Override
    public List<String> queryTables() {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "select name from sys.tables";
        List<String> stringList = baseDao.getJdbcTemplate().queryForList(sql, String.class);
        return stringList;
    }

    @Override
    public List<Map<String, Object>> queryTableSchema(String tableName) {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "SELECT syscolumns.name Field, systypes.name type, syscolumns.isnullable, syscolumns.length FROM syscolumns, systypes WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id('"+tableName+"');";
        List<Map<String, Object>> mapList = baseDao.getJdbcTemplate().queryForList(sql);
        return mapList;
    }
}
