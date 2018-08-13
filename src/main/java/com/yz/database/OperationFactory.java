package com.yz.database;

import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * Created by yz on 17/2/8.
 */
@Component
public class OperationFactory {

    @Resource(name = "mysqlOperation")
    private MysqlOperation mysqlOperation;

    @Resource(name = "postgresqlOperation")
    private PostgresqlOperation postgresqlOperation;

    @Resource(name = "sqlServerOperation")
    private SQLServerOperation sqlServerOperation;

    @Resource
    private SqliteOperation sqliteOperation;

    private Operation operation;

    public Operation getOperation(int type){

        switch (type){
            case 1:
                operation = mysqlOperation;
                break;
            case 2:
                operation = sqliteOperation;
                break;
            case 3:
                operation = postgresqlOperation;
                break;
            case 4:
                operation = sqlServerOperation;
                break;
        }
        return operation;
    }
}
