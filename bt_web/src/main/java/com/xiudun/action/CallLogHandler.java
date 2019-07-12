package com.xiudun.action;

import com.xiudun.domain.CallLog;
import com.xiudun.domain.Contact;
import com.xiudun.domain.QueryInfo;
import com.xiudun.service.CallLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 修盾科技:张茂修 on 2019/2/7.
 */
@RestController
public class CallLogHandler {
    @Autowired
    private CallLogService callLogService;

    /**
     * 按照id查找用户信息
     * @param contact
     * @return
     */
    @RequestMapping("/queryContact")
    public ModelAndView query(Contact contact) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("id", String.valueOf(contact.getId()));
        List<Contact> contactList = callLogService.getContactWithId(paramMap);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("queryContact");
        modelAndView.addObject("contacts", contactList);
        return modelAndView;
    }

    /**
     * 查询所有人信息
     * @return
     */
    @RequestMapping("/queryContactList")
    public ModelAndView querylist() {
        List<Contact> contactList = callLogService.getContacts();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("queryContact");
        modelAndView.addObject("contacts", contactList);
        return modelAndView;
    }

    /**
     * 按日期查询电话号码对应的所有信息
     * @param queryInfo
     * @return
     */
    @RequestMapping("/queryCallLogList")
    public ModelAndView queryCallLog(QueryInfo queryInfo,String date){
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("telephone", String.valueOf(queryInfo.getTelephone()));
        paramMap.put("year",date.substring(0,4));
       // paramMap.put("month", String.valueOf(queryInfo.getMonth()));
       // paramMap.put("day", String.valueOf(queryInfo.getDay()));
        List<CallLog> callLogList = callLogService.getCallLogList(paramMap);
        System.out.println("------------->>"+callLogList);
        //Gson gson = new Gson();
        //String resultList = gson.toJson(callLogList);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("callLogList");
        modelAndView.addObject("callLogList", callLogList);
        return modelAndView;
    }

    @RequestMapping("/queryCallLogList2")
    public ModelAndView queryCallLog2(String date, QueryInfo queryInfo,Model model){
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("telephone", String.valueOf(queryInfo.getTelephone()));
        paramMap.put("year", date.substring(0,4));
       // paramMap.put("day", String.valueOf(queryInfo.getDay()));
        List<CallLog> callLogList = callLogService.getCallLogList(paramMap);
        StringBuilder dateString = new StringBuilder();
        StringBuilder countString = new StringBuilder();
        StringBuilder durationString = new StringBuilder();
        for(int i = 0; i < callLogList.size(); i++){
            CallLog callLog = callLogList.get(i);
            if(Integer.valueOf(callLog.getMonth()) > 0){
                dateString.append(callLog.getMonth()).append("月").append(",");
                countString.append(callLog.getSum()).append(",");
                durationString.append(Float.valueOf(callLog.getDuration()) / 60f).append(",");
            }
        }
        //1 月,2 月,3 月,4 月,5 月,6 月,7 月,8 月,9 月,10 月,11 月,12 月,
        model.addAttribute("telephone", callLogList.get(0).getTelephone());
        model.addAttribute("name", callLogList.get(0).getName());
        model.addAttribute("date", dateString.deleteCharAt(dateString.length() - 1));
        model.addAttribute("count", countString.deleteCharAt(countString.length() - 1));
        model.addAttribute("duration", durationString.deleteCharAt(durationString.length() - 1));

        System.out.println("--------------->>>"+model);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("callLogListEchart");
        modelAndView.addObject("callLogList", model);
        return modelAndView;
    }
}



























