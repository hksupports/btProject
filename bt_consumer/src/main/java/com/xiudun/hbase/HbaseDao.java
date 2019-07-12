package com.xiudun.hbase;

import com.xiudun.util.ConnectionInstance;
import com.xiudun.util.HBaseUtil;
import com.xiudun.util.PropertiesUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 修盾科技:张茂修 on 2019/2/3.
 */
public class HbaseDao {
    private int regions;
    private String namespace;
    private String tableName;
    public static final Configuration conf ;
    private HTable table;
    private Connection connection;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");

    //缓存集合
    private List<Put> cacheList = new ArrayList<>();
    //获取配置对象
    static {
        conf = HBaseConfiguration.create();
    }
    /**
     * 初始化信息
     */
    public HbaseDao(){
        try {
            //读取region分区数
            regions = Integer.valueOf(PropertiesUtil.getProperty("hbase.xiudun.regions"));
            //获取表空间
            namespace = PropertiesUtil.getProperty("hbase.xiudun.namespace");
            // 获取表名称
            tableName = PropertiesUtil.getProperty("hbase.xiudun.tablename");
            //创建表
            if (!HBaseUtil.isExistTable(conf,tableName)){
                HBaseUtil.initNamespace(conf,namespace);
                HBaseUtil.createTable(conf,tableName,regions,"info","info2");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 添加数据
     * ori数据样式： 18576581848,17269452013,2017-08-14 13:38:31,1761
     * rowkey样式：01_18576581848_20170814133831_17269452013_1_1761
     * HBase表的列：call1  call2   build_time   build_time_ts   flag   duration
     */
    public void put(String ori){
        try {
            if (cacheList.size()==0){
                //获取hbase连接
                connection = ConnectionInstance.getConnection(conf);
                //获取表
                table = (HTable) connection.getTable(TableName.valueOf(tableName));
                //设置手动提交
                table.setAutoFlushTo(false);
                //设置表容量
                table.setWriteBufferSize(2*1024*1024);
            }
            String[] splitOri = ori.split(",");
            String caller = splitOri[0];
            String callee = splitOri[1];
            String buildTime = splitOri[2];
            String duration = splitOri[3];
            String regionCode = HBaseUtil.genRegionCode(caller, buildTime, regions);

            String buildTimeReplace = sdf2.format(sdf1.parse(buildTime));
            String buildTimeTs = String.valueOf(sdf1.parse(buildTime).getTime());

            //生成rowkey
            String rowkey = HBaseUtil.genRowKey(regionCode, caller, buildTimeReplace, callee, "1", duration);
            //向表中插入该条数据
            Put put = new Put(Bytes.toBytes(rowkey));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("call1"), Bytes.toBytes(caller));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("call2"), Bytes.toBytes(callee));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("build_time"), Bytes.toBytes(buildTime));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("build_time_ts"), Bytes.toBytes(buildTimeTs));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("flag"), Bytes.toBytes("1"));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("duration"), Bytes.toBytes(duration));
            //添加到缓存集合中
            cacheList.add(put);

            if(cacheList.size() >= 30){
                table.put(cacheList);
                //手动提交
                table.flushCommits();
                //是否资源
                table.close();
                cacheList.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}




































