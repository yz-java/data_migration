package com.yz.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.yz.bean.Property;
import com.yz.dao.BaseDao;
import com.yz.datasources.DataSourceManager;
import com.yz.service.MigrationService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yz on 17/2/7.
 */
@Service
public class MigrationServiceImpl implements MigrationService {

    @Resource(name = "baseDao")
    private BaseDao baseDao;

    @Override
    public boolean createDataSource(String sessionId, Property property,int flag) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(property.getUserName());
        dataSource.setPassword(property.getPassWord());
        dataSource.setUrl(property.getUrl());
        dataSource.setDriverClassName(property.getDriver());

        try {
            dataSource.getConnection();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        if (flag==1){
            DataSourceManager.getInstance().setUserDataSources(sessionId,"read",dataSource);
        }else if (flag==2){
            DataSourceManager.getInstance().setUserDataSources(sessionId,"write",dataSource);
        }
        return true;
    }

    @Override
    public boolean dataMigration(String sessionId,String[] fields, String[] targetFields, String tableName, String targetTableName) {
        Map userDataSources = DataSourceManager.getInstance().getUserDataSources(sessionId);
        DataSource readDataSource = (DataSource) userDataSources.get("read");
        DataSource writeDataSource = (DataSource) userDataSources.get("write");
        String rf = Arrays.stream(fields).collect(Collectors.joining(","));
        rf=rf.substring(0,rf.length()-1);
        String readSql = "select "+rf+" from "+tableName;
        baseDao.getJdbcTemplate().setDataSource(readDataSource);
        List<Map<String, Object>> mapList = baseDao.getJdbcTemplate().queryForList(readSql);
        String wf = Arrays.stream(targetFields).collect(Collectors.joining(","));
        wf=wf.substring(0,wf.length()-1);
        StringBuilder wirteSql = new StringBuilder();
        List values = new ArrayList();
        wirteSql.append("insert into "+targetTableName+" ("+wf+") values");
        for (Map data:mapList) {
            StringBuilder p = new StringBuilder();
            p.append("(");
            data.forEach((key,value)->{
                p.append("?,");
                values.add(value);
            });
            String s = p.toString();
            s=s.substring(0,s.length()-1);
            s+="),";
            wirteSql.append(s);
        }
        baseDao.getJdbcTemplate().setDataSource(writeDataSource);
        String ws = wirteSql.toString();
        ws=ws.substring(0,ws.length()-1);

        int insert = baseDao.getJdbcTemplate().update(ws,values.toArray());;
        if (insert>0){
            return true;
        }
        return false;
    }

    @Override
    public long execute(String sessionId, String sourceSql, String targetSql) {
        Map userDataSources = DataSourceManager.getInstance().getUserDataSources(sessionId);
        DataSource dataSource = (DataSource) userDataSources.get("read");
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        String[] froms = sourceSql.toLowerCase().split("from");
        String sourceTableName = froms[1];
        long count = baseDao.getJdbcTemplate().queryForObject("select count(1) from " + sourceTableName,Long.class);
        int queryNum = 0;
        if (count%10000 == 0){
            queryNum=(int)count/10000;
        }else{
            queryNum=((int)count/10000)+1;
        }
        long num = 0;
        for (int i=0;i<queryNum;i++){
            dataSource = (DataSource) userDataSources.get("read");
            baseDao.getJdbcTemplate().setDataSource(dataSource);
            List<Map<String, Object>> mapList = baseDao.getJdbcTemplate().queryForList(sourceSql+" limit 10000 offset "+i*10000);
            StringBuilder values = new StringBuilder();
            List params = new ArrayList();
            mapList.stream().forEach(map->{
                StringBuilder value = new StringBuilder();
                value.append("(");
                map.forEach((k,v)->{
                    value.append("?,");
                    params.add(v);
                });

                String v = value.substring(0, value.length() - 1);
                v+="),";
                values.append(v);
            });
            String s = values.toString();
            s = s.substring(0,s.length()-1);
            String sql = targetSql+s;
            DataSource writeDataSource = (DataSource) userDataSources.get("write");
            baseDao.getJdbcTemplate().setDataSource(writeDataSource);
            int update = baseDao.getJdbcTemplate().update(sql, params.toArray());
            if (update>0){
                num+=1;
            }
        }
        num = count;
        return num;
    }
}
