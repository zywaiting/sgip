package com.zy.demo;

import com.zy.demo.protocol.SendSms;
import com.zy.demo.protocol.sgip.Session;
import com.zy.demo.protocol.sgip.conn.Connection;
import com.zy.demo.protocol.sgip.thread.ListenThread;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Service
public class SgipServiceImpl implements SgipService{

    private ListenThread listenThread = null;//上行、状态报告监听器

    @PostConstruct
    public void init() {
        try {
            Connection conn = new Connection("119.163.122.85", 8801);
            Session session = new Session(conn);
            if (listenThread == null) {
                listenThread = new ListenThread(session);
                listenThread.start();
            }
        } catch (Exception e){

        }
    }


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
