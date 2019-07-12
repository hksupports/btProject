package com.xiudun.kafka;

import com.xiudun.hbase.HbaseDao;
import com.xiudun.util.PropertiesUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;

/**
 * Created by 修盾科技:张茂修 on 2019/2/3.
 */
public class HBaseConsumer {
    public static void main(String[] args) {
        //创建消费者
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(PropertiesUtil.properties);
        //获取主题
        kafkaConsumer.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));
        //获取hbaseDao
        HbaseDao dao = new HbaseDao();
        //添加数据
        while (true){
            //100毫秒读取一次
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String,String> cr:records){
                //System.out.println(cr.value());
                dao.put(cr.value());
            }
        }
    }
}





























