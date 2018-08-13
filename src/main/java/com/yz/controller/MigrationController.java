package com.yz.controller;

import com.yz.bean.Property;
import com.yz.database.Operation;
import com.yz.database.OperationFactory;
import com.yz.datasources.DataSourceContextHolder;
import com.yz.datasources.DataSourceManager;
import com.yz.service.MigrationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yz on 17/2/7.
 */
@RestController
public class MigrationController {

    @Resource(name = "operationFactory")
    private OperationFactory operationFactory;

    @Resource(name = "migrationServiceImpl")
    private MigrationService migrationServiceImpl;

    @RequestMapping("/create_datasource")
    public Object createDataSource(HttpSession session, Property property, int flag, int databaseType){
        switch (databaseType){
            case 1:
                property.setDriver("com.mysql.jdbc.Driver");
                break;
            case 2:
                property.setDriver("org.sqlite.JDBC");
                break;
            case 3:
                property.setDriver("org.postgresql.Driver");
                break;
            case 4:
                property.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                break;
        }
        boolean result = migrationServiceImpl.createDataSource(session.getId(), property, flag);
        return result;
    }

    @RequestMapping("/tables")
    public Object getTables(HttpSession session,int databaseType,int flag){
        Operation operation = operationFactory.getOperation(databaseType);
        DataSource dataSource = DataSourceManager.getInstance().getUserDataSource(session.getId(), flag);
        DataSourceContextHolder.setDataSource(dataSource);
        List<String> tables = operation.queryTables();
        Collections.sort(tables);
        return tables;
    }

    @RequestMapping("/table/fields")
    public Object getTableSchema(HttpSession session,String tableName,int databaseType,int flag){
        DataSource dataSource = DataSourceManager.getInstance().getUserDataSource(session.getId(), flag);
        DataSourceContextHolder.setDataSource(dataSource);
        Operation operation = operationFactory.getOperation(databaseType);
        List<Map<String, Object>> mapList = operation.queryTableSchema(tableName);
        List<String> fields = mapList.stream().map(map -> map.get("Field").toString()).collect(Collectors.toList());
        Collections.sort(fields);
        return fields;
    }

    @RequestMapping("/data/migration")
    public Object migration(HttpSession session,String[]fields,String[]targetFields,String tableName,String targetTableName){
        boolean b = migrationServiceImpl.dataMigration(session.getId(), fields, targetFields, tableName, targetTableName);
        return b;
    }

    @RequestMapping("/sql/execute")
    public Object execute(HttpSession session,String sourceSql,String tegetSql){
        long execute = migrationServiceImpl.execute(session.getId(), sourceSql, tegetSql);
        return execute;
    }
}
