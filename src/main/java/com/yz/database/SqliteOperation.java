package com.yz.database;

import com.yz.dao.BaseDao;
import com.yz.datasources.DataSourceContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yz
 * @Description
 * @Date create by 14:23 18/8/2
 */
@Component
public class SqliteOperation implements Operation {

    @Resource(name = "baseDao")
    private BaseDao baseDao;

    @Override
    public List<String> queryTables() {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "select name from sqlite_master where type='table' order by name";
        List<String> strings = baseDao.getJdbcTemplate().queryForList(sql, String.class);
        return strings;
    }

    @Override
    public List<Map<String, Object>> queryTableSchema(String tableName) {
        DataSource dataSource = DataSourceContextHolder.getDataSource();
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String sql = "pragma table_info ('"+tableName+"')";
        List<Map<String, Object>> mapList = baseDao.getJdbcTemplate().queryForList(sql);
        mapList = mapList.stream().map(map -> {
            Map<String, Object> data = new HashMap();
            data.put("Field", map.get("name"));
            return data;
        }).collect(Collectors.toList());
        return mapList;
    }
}
