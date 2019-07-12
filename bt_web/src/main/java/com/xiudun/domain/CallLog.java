package com.xiudun.domain;

/**
 * 用于存放返回给用户的数据
 */
public class CallLog {
    private String id_date_contact;//通话日期-联系人
    private String id_date_dimension;//通话时间维度
    private String id_contact;//联系人
    private Integer call_sum;//通话次数
    private String call_duration_sum;//通话时长
    private String telephone;//电话
    private String name;//机主
    private String year;
    private String month;
    private String day;
    //属性使用下划线不能获取数据
    private String sum;//通话次数
    private String duration;//通话时长

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId_date_contact() {
        return id_date_contact;
    }

    public void setId_date_contact(String id_date_contact) {
        this.id_date_contact = id_date_contact;
    }

    public String getId_date_dimension() {
        return id_date_dimension;
    }

    public void setId_date_dimension(String id_date_dimension) {
        this.id_date_dimension = id_date_dimension;
    }

    public String getId_contact() {
        return id_contact;
    }

    public void setId_contact(String id_contact) {
        this.id_contact = id_contact;
    }

    public Integer getCall_sum() {
        return call_sum;
    }

    public void setCall_sum(Integer call_sum) {
        this.call_sum = call_sum;
    }

    public String getCall_duration_sum() {
        return call_duration_sum;
    }

    public void setCall_duration_sum(String call_duration_sum) {
        this.call_duration_sum = call_duration_sum;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "CallLog{" +
                ", sum='" + sum + '\'' +
                ", duration='" + duration + '\'' +
                "id_date_contact='" + id_date_contact + '\'' +
                ", id_date_dimension='" + id_date_dimension + '\'' +
                ", id_contact='" + id_contact + '\'' +
                ", call_sum=" + call_sum +
                ", call_duration_sum='" + call_duration_sum + '\'' +
                ", telephone='" + telephone + '\'' +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
