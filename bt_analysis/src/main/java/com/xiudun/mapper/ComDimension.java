package com.xiudun.mapper;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by 修盾科技:张茂修 on 2019/2/6.
 */
public class ComDimension extends BaseXiudun {
    //封装联系人、联系电话
    private ContactDimension contactDimension = new ContactDimension();
    //封装年、月、日
    private DateDimension dateDimension = new DateDimension();

    public ComDimension(){
    }

    public ContactDimension getContactDimension() {
        return contactDimension;
    }

    public void setContactDimension(ContactDimension contactDimension) {
        this.contactDimension = contactDimension;
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    @Override
    public int compareTo(BaseXiudun o) {
        ComDimension anotherComDimension = (ComDimension) o;

        int result = this.dateDimension.compareTo(anotherComDimension.dateDimension);
        if(result != 0) return result;

        result = this.contactDimension.compareTo(anotherComDimension.contactDimension);
        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        contactDimension.write(out);
        dateDimension.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        contactDimension.readFields(in);
        dateDimension.readFields(in);
    }
}



























