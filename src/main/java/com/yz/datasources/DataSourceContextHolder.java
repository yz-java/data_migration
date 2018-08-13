package com.yz.datasources;

import javax.sql.DataSource;

/**
 * Created by yz on 17/2/7.
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSource> contextHolder = new ThreadLocal<DataSource>();

    /**
     * @Description: 设置数据源类型
     * @param dataSource  数据源名称
     * @return void
     * @throws
     */
    public static void setDataSource(DataSource dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * @Description: 获取数据源名称
     * @param
     * @return String
     * @throws
     */
    public static DataSource getDataSource() {
        return contextHolder.get();
    }

    /**
     * @Description: 清除数据源名称
     * @param
     * @return void
     * @throws
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }
}
