package com.yz.database;

import com.yz.dao.BaseDao;
import com.yz.datasources.DataSourceContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yz on 17/2/8.
 */
@Component
public class PostgresqlOperation implements Operation {

    @Resource(name = "baseDao")
    private BaseDao baseDao;

    @Override
    public List<String> queryTables() {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "select * from pg_tables";
        List<Map<String, Object>> maps = baseDao.getJdbcTemplate().queryForList(sql);
        List<String> tablenames = maps.stream().map(map -> map.get("tablename").toString()).collect(Collectors.toList());
        return tablenames;
    }

    @Override
    public List<Map<String, Object>> queryTableSchema(String tableName) {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "SELECT A .attnum, A .attname AS Field, T .typname AS TYPE, A .attlen AS LENGTH, A .atttypmod AS lengthvar, A .attnotnull AS NOTNULL, b.description AS COMMENT FROM pg_class C, pg_attribute A LEFT OUTER JOIN pg_description b ON A .attrelid = b.objoid AND A .attnum = b.objsubid, pg_type T WHERE C.relname = '"+tableName+"' AND A .attnum > 0 AND A .attrelid = C .oid AND A .atttypid = T .oid ORDER BY A .attnum";
        List<Map<String, Object>> maps = baseDao.getJdbcTemplate().queryForList(sql);
        return maps;
    }
}
