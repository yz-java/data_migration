package com.yz.database;

import java.util.List;
import java.util.Map;

/**
 * Created by yz on 17/2/8.
 */
public interface Operation {
    /**
     * 查询表名
     * @return
     */
    public List<String> queryTables();

    /**
     * 查询表结构
     * @return
     */
    public List<Map<String, Object>> queryTableSchema(String tableName);


}
