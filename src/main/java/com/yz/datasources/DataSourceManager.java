package com.yz.datasources;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yz on 17/2/8.
 */
public class DataSourceManager {

    private static final ConcurrentHashMap<String,Map> managers = new ConcurrentHashMap();

    private static final DataSourceManager instance = new DataSourceManager();

    public static DataSourceManager getInstance(){
        return instance;
    }

    public void setUserDataSources(String sessionId,String key, DataSource dataSource){
        Map map =getUserDataSources(sessionId);
        if (map!=null){
            DataSource ds = (DataSource) map.get(key);
            if (ds!=null){
                map.remove(key);
            }
        }
        if (map==null){
            map = new HashMap();
        }
        map.put(key,dataSource);
        managers.put(sessionId,map);
    }

    public Map getUserDataSources(String sessionId){
        Map map = managers.get(sessionId);
        return map;
    }

    public DataSource getUserDataSource(String sessionId,int flag){
        String key = "";
        if (flag==1){
            key="read";
        }
        if (flag==2){
            key = "write";
        }
        Map map = managers.get(sessionId);
        DataSource dataSource = (DataSource) map.get(key);
        return dataSource;
    }
}
