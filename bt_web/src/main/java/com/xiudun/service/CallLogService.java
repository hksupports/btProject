package com.xiudun.service;

import com.xiudun.dao.CallLogMapper;
import com.xiudun.domain.CallLog;
import com.xiudun.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 修盾科技:张茂修 on 2019/2/7.
 */
@Service
public class CallLogService {
    @Autowired
    private CallLogMapper callLogMapper;

    public List<CallLog> getCallLogList(Map<String, String> paramsMap){
        return callLogMapper.getCallLogList(paramsMap);
    }

    public List<Contact> getContacts(){
        return callLogMapper.getContacts();
    }

    public List<Contact> getContactWithId(Map<String, String> hashMap){
        return callLogMapper.getContactWithId(hashMap);
    }
}
