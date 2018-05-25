package com.zy.demo;

import com.zy.demo.protocol.SendSms;
import com.zy.demo.protocol.sgip.thread.ListenThread;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Service
public class SgipServiceImpl implements SgipService{

    /*@PostConstruct
    public void init() {
        try {
            ListenThread listenThread = new ListenThread();
            new Thread(listenThread).start();
        } catch (Exception e){

        }

    }*/


    /**
     * 短信发送
     */
    @Override
    @Transactional
    public String smsSend(HttpServletRequest request) {
        try {
            String mobile = request.getParameter("mobile");
            String content = request.getParameter("content");
            String extNo = request.getParameter("extNo");
            SendSms.sendSms(mobile,content,extNo);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
}
