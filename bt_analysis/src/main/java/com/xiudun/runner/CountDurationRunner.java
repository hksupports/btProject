package com.xiudun.runner;

import com.xiudun.mapper.ComDimension;
import com.xiudun.mapper.CountDurationMapper;
import com.xiudun.reduce.CountDurationReducer;
import com.xiudun.reduce.CountDurationValue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Created by 修盾科技:张茂修 on 2019/2/6.
 */
public class CountDurationRunner implements Tool {
    private Configuration conf = null;
    @Override
    public void setConf(Configuration conf) {
        this.conf = HBaseConfiguration.create(conf);
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    @Override
    public int run(String[] args) throws Exception {
        //得到conf
        Configuration conf = this.getConf();
        //实例化Job
        Job job = Job.getInstance(conf);
        job.setJarByClass(CountDurationRunner.class);
        //组装Mapper InputForamt
        initHBaseInputConfig(job);
        //组装Reducer Outputformat
        initReducerOutputConfig(job);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    private void initHBaseInputConfig(Job job) {
        Connection connection = null;
        Admin admin = null;
        try {
            String tableName = "ns1:tablelog";
            connection = ConnectionFactory.createConnection(job.getConfiguration());
            admin = connection.getAdmin();
            if(!admin.tableExists(TableName.valueOf(tableName))) throw new RuntimeException("无法找到目标表.");
            Scan scan = new Scan();
            //可以优化
            //初始化Mapper
            TableMapReduceUtil.initTableMapperJob(
                    tableName,
                    scan,
                    CountDurationMapper.class,
                    ComDimension.class,
                    Text.class,
                    job,
                    true);


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(admin != null){
                    admin.close();
                }
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void initReducerOutputConfig(Job job) {
        job.setReducerClass(CountDurationReducer.class);
        job.setOutputKeyClass(ComDimension.class);
        job.setOutputValueClass(CountDurationValue.class);
        job.setOutputFormatClass(MysqlOutputFormat.class);
    }

    public static void main(String[] args) {
        try {
            int status = ToolRunner.run(new CountDurationRunner(), args);
            System.exit(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}