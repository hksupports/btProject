package com.xiudun.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

/**
 * Created by 修盾科技:张茂修 on 2019/2/3.
 */
public class ConnectionInstance {
    private static Connection conn;
    public static synchronized Connection getConnection(Configuration conf){
        try {
            if (conn==null || conn.isClosed())
                conn = ConnectionFactory.createConnection(conf);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}

































