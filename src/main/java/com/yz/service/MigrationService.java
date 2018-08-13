package com.yz.service;

import com.yz.bean.Property;

/**
 * Created by yz on 17/2/7.
 */
public interface MigrationService {
    /**
     * 创建数据源
     * @param sessionId
     * @param property
     * @param flag
     * @return
     */
    public boolean createDataSource(String sessionId, Property property,int flag);

    /**
     * 数据迁移
     * @param fields 源数据库表字段
     * @param targetFields 目标数据库表字段
     * @param tableName 源数据库表名
     * @param targetTableName 目标数据库表名
     * @return
     */
    public boolean dataMigration(String sessionId,String[]fields,String[]targetFields,String tableName,String targetTableName);

    /**
     * 执行
     * @param sessionId
     * @param sourceSql
     * @param targetSql
     * @return
     */
    public long execute(String sessionId,String sourceSql,String targetSql);

}
