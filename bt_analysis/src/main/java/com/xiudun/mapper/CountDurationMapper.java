package com.xiudun.mapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 修盾科技:张茂修 on 2019/2/6.
 * 获取数据后进行reduce--再分析数据
 */
public class CountDurationMapper extends TableMapper<ComDimension, Text>{
    //封装联系人、联系电话、年、月、日
    private ComDimension comDimension = new ComDimension();
    //通话时间
    private Text durationText = new Text();
    private Map<String, String> phoneNameMap;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        phoneNameMap = new HashMap<>();
        phoneNameMap.put("17078388295", "刘备");
        phoneNameMap.put("13980337439", "关羽");
        phoneNameMap.put("14575535933", "张飞");
        phoneNameMap.put("19902496992", "赵云");
        phoneNameMap.put("18549641558", "马超");
        phoneNameMap.put("17005930322", "黄忠");
        phoneNameMap.put("18468618874", "诸葛亮");
        phoneNameMap.put("18576581848", "孙权");
        phoneNameMap.put("15978226424", "周瑜");
        phoneNameMap.put("15542823911", "黄盖");
        phoneNameMap.put("17526304161", "曹操");
        phoneNameMap.put("15422018558", "曹丕");
        phoneNameMap.put("17269452013", "曹植");
        phoneNameMap.put("17764278604", "司马懿");
        phoneNameMap.put("15711910344", "司马师");
        phoneNameMap.put("15714728273", "颜良");
        phoneNameMap.put("16061028454", "文丑");
        phoneNameMap.put("16264433631", "徐庶");
        phoneNameMap.put("17601615878", "庞统");
        phoneNameMap.put("15897468949", "水晶先生");
    }

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context)
            throws IOException, InterruptedException {
        //region编号_通话时间_主叫电话_被叫电话_主被叫标记__通话时长
        //05_19902496992_20170312154840_15542823911_1_1288
        String rowKey = Bytes.toString(key.get());
        String[] splits = rowKey.split("_");
        //1表示主叫、0表示被叫
        if(splits[4].equals("0")) return;

        //以下数据全部是主叫数据，但是也包含了被叫电话的数据
        String caller = splits[1];
        String callee = splits[3];
        String buildTime = splits[2];
        String duration = splits[5];
        durationText.set(duration);
        //年月日
        String year = buildTime.substring(0, 4);
        String month = buildTime.substring(4, 6);
        String day = buildTime.substring(6, 8);

        //组装ComDimension
        //组装DateDimension 年月日
        ////05_19902496992_20170312154840_15542823911_1_1288
        DateDimension yearDimension = new DateDimension(year, "-1", "-1");
        DateDimension monthDimension = new DateDimension(year, month, "-1");
        DateDimension dayDimension = new DateDimension(year, month, day);

        //组装ContactDimension 联系人、联系电话
        ContactDimension callerContactDimension = new ContactDimension(caller, phoneNameMap.get(caller));

        //开始封装主叫数据(同时添加通话时长)
        comDimension.setContactDimension(callerContactDimension);
        //年
        comDimension.setDateDimension(yearDimension);
        context.write(comDimension, durationText);
        //月
        comDimension.setDateDimension(monthDimension);
        context.write(comDimension, durationText);
        //日
        comDimension.setDateDimension(dayDimension);
        context.write(comDimension, durationText);

        //开始封装被叫数据()
        ContactDimension calleeContactDimension = new ContactDimension(callee, phoneNameMap.get(callee));
        comDimension.setContactDimension(calleeContactDimension);
        //年
        comDimension.setDateDimension(yearDimension);
        context.write(comDimension, durationText);
        //月
        comDimension.setDateDimension(monthDimension);
        context.write(comDimension, durationText);
        //日
        comDimension.setDateDimension(dayDimension);
        context.write(comDimension, durationText);
    }
}










































