package com.xiudun.reduce;

import com.xiudun.mapper.ComDimension;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by 修盾科技:张茂修 on 2019/2/6.
 */
public class CountDurationReducer extends Reducer<ComDimension, Text,ComDimension,CountDurationValue>{
    //创建统计通话时长和次数的对象类
    CountDurationValue countDurationValue = new CountDurationValue();

    @Override
    protected void reduce(ComDimension key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //通话次数
        int callSum = 0;
        //通话时长
        int callDurationSum = 0;
        //遍历
        for (Text t:values){
            callSum++;
            callDurationSum += Integer.valueOf(t.toString().trim());
        }
        //封装到bean中
        countDurationValue.setCallSum(String.valueOf(callSum));
        countDurationValue.setCallDurationSum(String.valueOf(callDurationSum));
        //写出去
        context.write(key,countDurationValue);
    }
}



























