package com.xiudun.reduce;

import com.sun.corba.se.spi.ior.Writeable;
import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CountDurationValue implements Writable {
    private String callSum;//呼叫总次数
    private String callDurationSum;//呼叫总时长

    public CountDurationValue(){  }

    public String getCallSum() {
        return callSum;
    }

    public void setCallSum(String callSum) {
        this.callSum = callSum;
    }

    public String getCallDurationSum() {
        return callDurationSum;
    }

    public void setCallDurationSum(String callDurationSum) {
        this.callDurationSum = callDurationSum;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(callSum);
        out.writeUTF(callDurationSum);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.callSum = in.readUTF();
        this.callDurationSum = in.readUTF();
    }
}
