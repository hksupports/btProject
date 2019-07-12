package com.xiudun.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 修盾科技:张茂修 on 2019/2/3.
 */
public class PropertiesUtil {
    public static Properties properties = null;

    static{
        //读取资源文件
        InputStream is = ClassLoader.getSystemResourceAsStream("hbase_consumer.properties");
        properties = new Properties();
        try {
            //加载
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据key获取对应的值
    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
