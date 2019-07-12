package com.xiudun.hbase;

import com.xiudun.util.HBaseUtil;
import com.xiudun.util.PropertiesUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by 修盾科技:张茂修 on 2019/2/3.
 */
public class CalleeWriteObserver extends BaseRegionObserver{
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        super.postPut(e, put, edit, durability);
        //读取配置文件获取表名称
        String targetTableName = PropertiesUtil.getProperty("hbase.xiudun.tablename");
        //获取hbase中的、添加数据后的表
        String currentTableName = e.getEnvironment().getRegionInfo().getTable().getNameAsString();
        //判断表是否存在
        if (!targetTableName.equals(currentTableName)) return;
        //获取数据行
        String oriRowKey = Bytes.toString(put.getRow());
        //拆分行
        String[] splitOriRowKey = oriRowKey.split("_");
        //获取电话号码信息
        String oldFlag = splitOriRowKey[4];
        //如果当前插入的是被叫数据，则直接返回(因为默认提供的数据全部为主叫数据)
        if(oldFlag.equals("0")) return;
        //获取分区数
        int regions = Integer.valueOf(PropertiesUtil.getProperty("hbase.xiudun.regions"));
        //获取主叫、被叫、时间、主被标记、通话时长
        String caller = splitOriRowKey[1];
        String callee = splitOriRowKey[3];
        String buildTime = splitOriRowKey[2];
        String flag = "0";
        String duration = splitOriRowKey[5];
        //获取分区码
        String regionCode = HBaseUtil.genRegionCode(callee,buildTime,regions);
        //获取主叫rowkey
        String calleeRowKey = HBaseUtil.genRowKey(regionCode,callee,buildTime,caller,flag,duration);
        //生成时间戳
        String buildTimeTs = "";
        try {
            buildTimeTs = String.valueOf(sdf.parse(buildTime).getTime());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        //添加数据hbase
        Put calleePut = new Put(Bytes.toBytes(calleeRowKey));
        calleePut.addColumn(Bytes.toBytes("info2"),Bytes.toBytes("call1"),Bytes.toBytes(callee));
        calleePut.addColumn(Bytes.toBytes("info2"),Bytes.toBytes("call2"),Bytes.toBytes(caller));
        calleePut.addColumn(Bytes.toBytes("info2"),Bytes.toBytes("build_time"),Bytes.toBytes(buildTime));
        calleePut.addColumn(Bytes.toBytes("info2"),Bytes.toBytes("flag"),Bytes.toBytes(flag));
        calleePut.addColumn(Bytes.toBytes("info2"),Bytes.toBytes("duration"),Bytes.toBytes(duration));
        calleePut.addColumn(Bytes.toBytes("info2"),Bytes.toBytes("build_time_ts"),Bytes.toBytes(buildTimeTs));
        //把数据添加到表中
        Table table = e.getEnvironment().getTable(TableName.valueOf(targetTableName));
        table.put(calleePut);
        //释放资源
        table.close();
    }
}



















































