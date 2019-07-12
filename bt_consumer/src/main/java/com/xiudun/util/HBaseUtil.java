package com.xiudun.util;

import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by 修盾科技:张茂修 on 2019/2/3.
 */
public class HBaseUtil {
    /**
     * 判断表是否存在
     */
    public static boolean isExistTable(Configuration conf,String table) throws IOException {
        //创建hbase连接
        Connection connection = ConnectionFactory.createConnection(conf);
        //获取hbase的admin对象
        Admin admin = connection.getAdmin();
        //判断表是否存在
        boolean flag = admin.tableExists(TableName.valueOf(table));
        //是否资源
        admin.close();
        connection.close();
        return flag;
    }
    /**
     * 创建命名空间
     */
    public static void initNamespace(Configuration conf,String namespace) throws IOException {
        //连接hbase
        Connection connection = ConnectionFactory.createConnection(conf);
        //创建admin对象
        Admin admin = connection.getAdmin();
        //创建表空间
        NamespaceDescriptor nd = NamespaceDescriptor.create(namespace)
                .addConfiguration("createTime", String.valueOf(System.currentTimeMillis()))
                .addConfiguration("auth", "zmx").build();
        //添加表空间
        admin.createNamespace(nd);
        //是否资源
        admin.close();
        connection.close();
    }

    /**
     * region分区防止数据倾斜
     * 获取分区key
     */
    private static byte[][] genSplitKeys(int regions) {
        byte[][] splitKeys = null;
        //创建分区数组
        String[] keys = new String[regions];
        //region个数在两位以内
        DecimalFormat df = new DecimalFormat("00");
        for (int i=0;i<regions;i++)
            keys[i] = df.format(i)+"|";
        //创建高维二维数组
        splitKeys = new byte[regions][];
        //region分区必须有序，否则数据倾斜
        TreeSet<byte[]> treeSet = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (int i=0;i<regions;i++)
            treeSet.add(Bytes.toBytes(keys[i]));
        //迭代
        Iterator<byte[]> iterator = treeSet.iterator();
        int index = 0;
        while (iterator.hasNext()){
            byte[] next = iterator.next();
            splitKeys[index++] = next;
        }
        //返回分区的key
        return splitKeys;
    }

    /**
     * 创建表
     */
    public static void createTable(Configuration conf,String table,int regions,String...columnFamily) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        //创建admin对象
        Admin admin = connection.getAdmin();
        //判断表是否存在
        if (isExistTable(conf,table)) return;
        //创建表对象
        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(table));
        System.out.println("-----------start------------");
        //添加 列族
        for (String cf:columnFamily){
            System.out.println("----------列族--------->>"+cf);
            htd.addFamily(new HColumnDescriptor(cf));
        }
        System.out.println("-----------end------------");
        /*
            调用协处理器
            Observer 类似于传统数据库中的触发器，当发生某些事件的时候这类协处理器会被 Server 端调用
            CalleeWriteObserver是自定义的协处理器类
         */
        htd.addCoprocessor("com.xiudun.hbase.CalleeWriteObserver");
        //创建表--根据分区获取key
        admin.createTable(htd,genSplitKeys(regions));
        //是否资源
        admin.close();
        connection.close();
    }
    /**
     * 创建rowkey
     */
    public static String genRowKey(String regionCode,String call1,String buildTime,String call2,String flag,String duration){
        StringBuilder sb = new StringBuilder();
        //根据主叫电话-被叫电话-时间-通话时长，创建rowKey
        sb.append(regionCode).append("_").append(call1).append("_").append(buildTime).append("_")
                .append(call2).append("_").append(flag).append("_").append(duration);
        return sb.toString();
    }
    /**
     * 创建region分区码
     */
    public static String genRegionCode(String call1,String buildTime,int regions){
        //主叫电话号码长度
        int len = call1.length();
        //截取
        String lastPhone = call1.substring(len - 4);
        //通话时间
        String yearMonth = buildTime.replaceAll("-", "").replaceAll(":", "")
                .replaceAll(" ", "").substring(0, 6);
        //防止重复，做离散操作1
        Integer x = Integer.valueOf(lastPhone) ^ Integer.valueOf(yearMonth);
        int a = 10;
        int b = 20;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        //离散操作2
        int y = x.hashCode();
        //生成分区号
        int regionCode = y % regions;
        //格式化
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(regionCode);
    }
}





























