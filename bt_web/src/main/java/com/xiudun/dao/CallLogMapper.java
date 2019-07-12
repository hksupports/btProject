package com.xiudun.dao;

import com.xiudun.domain.CallLog;
import com.xiudun.domain.Contact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 修盾科技:张茂修 on 2019/2/7.
 */
@Mapper
public interface CallLogMapper {
    String sql = "SELECT `call_sum`, `call_duration_sum`, `telephone`, `name`, `year` ," +
            " `month`, `day` FROM tb_dimension_date t4 INNER JOIN " +
            "( SELECT `id_date_dimension`, `call_sum`, `call_duration_sum`, `telephone`," +
            " `name` FROM tb_call t2 INNER JOIN ( SELECT `id`, `telephone`, " +
            "`name` FROM tb_contacts WHERE telephone = #{telephone} ) t1 ON t2.id_contact = t1.id ) " +
            "t3 ON t4.id = t3.id_date_dimension WHERE `year` = #{year} AND " +
            "`month` != #{month} AND `day` = #{day} ORDER BY `year`, `month`";

    //按照年统计：统计某个用户，1年12个月的所有的数据（不精确到month和day）
    @Select("SELECT `call_sum` sum, `call_duration_sum` duration, `telephone`, `name`, `year` ," +
            " `month`, `day` FROM tb_dimension_date t4 INNER JOIN " +
            "( SELECT `id_date_dimension`, `call_sum`, `call_duration_sum`, `telephone`," +
            " `name` FROM tb_call t2 INNER JOIN ( SELECT `id`, `telephone`, " +
            "`name` FROM tb_contacts WHERE telephone = #{telephone} ) t1 ON t2.id_contact = t1.id ) " +
            "t3 ON t4.id = t3.id_date_dimension WHERE `year` = #{year} and month!=-1 and day!=-1" +
            "  ORDER BY `year`,month")
    public List<CallLog> getCallLogList(Map<String, String> paramsMap);

    @Select( "SELECT id, telephone, name FROM tb_contacts")
    public List<Contact> getContacts();

    @Select("SELECT id, telephone, name FROM tb_contacts WHERE id = #{id}")
    public List<Contact> getContactWithId(Map<String, String> hashMap);

}






































