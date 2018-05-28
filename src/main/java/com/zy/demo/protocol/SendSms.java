package com.zy.demo.protocol;

import com.zy.demo.protocol.sgip.Session;
import com.zy.demo.protocol.sgip.conn.Connection;
import com.zy.demo.protocol.sgip.msg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class SendSms {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSms.class);

    public static void sendSms(String mobile, String content ,String extNo, String identifier) throws Exception {
        Connection conn = new Connection("119.163.122.85", 8801);

        Bind bind = new Bind(1, "106550530008", "106550530008", 3053191173L);
        LOGGER.info("-----------登陆中-----------");
        Session session = new Session(conn) {

            @Override
            public void onReport(Report report) {
                LOGGER.info("----------------状态报告-------------");
                LOGGER.info("发送状态:{},序列号;{}",report.getResult(),report.getSubmitSeq());
            }

            @Override
            public void onMessage(Deliver deliver) {
                LOGGER.info("----------------上行回复---------------");
                //System.out.println("收到短信");
                try {
                    LOGGER.info("手机:{},短信内容:{},标识:{}", deliver.getUserNumber(), new String(deliver.getContent(), "GBK"), deliver.getReserve());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTerminate() {
                LOGGER.info("------------断开连接--------------");
            }
        };
        //本地用户帐号密码（网关访问本地参数）
        //session.setLocalUser("106550530008");
        //session.setLocalPass("106550530008");
        session.setLocalPort(11913);

        BindResp resp = session.open(bind);

        LOGGER.info("登录状态;{}",resp.getResult());


        String SPNumber = "106550530008" +extNo; //"001";//之后为附加码
        String ChargeNumber = "106550530008";
        String[] UserNumber = mobile.split(",");//"8618660776642".split(",");//拆分手机号码
        String CorpId = "91173";
        String ServiceType = "9917001262";
        int FeeType = 2;
        int FeeValue = 0;
        int GivenValue = 0;
        int AgentFlag = 1;
        int MorelatetoMTFlag = 3;//
        int Priority = 0;
        String ExpireTime = null;//短消息寿命的终止时间
        String ScheduleTime = null;//短消息定时发送的时间
        int ReportFlag = 1;//2 模拟上行，1下行有状态报告。（根据实际情况而定）
        int TP_pid = 0;
        int TP_udhi = 0;
        int MessageCoding = 15;
        int MessageType = 0;
        byte[] MessageContent = null;
        try {
            MessageContent = content.getBytes("GBK");//"您本次的验证码是：1234".getBytes("GBK");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        int MessageLen = MessageContent.length;
        String reserve = "0";
        //通讯节点
        long nodeid = (3000000000L + Long.parseLong("0531") * 100000L + Long.parseLong(CorpId));

        Submit s = new Submit(TP_pid, TP_udhi, SPNumber, ChargeNumber, UserNumber, CorpId,
                nodeid, ServiceType, FeeType, FeeValue, GivenValue, AgentFlag,
                MorelatetoMTFlag, Priority, ExpireTime, ScheduleTime,
                ReportFlag, MessageCoding, MessageType, MessageContent,
                MessageLen, reserve, identifier);

        SubmitResp sresp = (SubmitResp) session.sendSubmit(s);

        LOGGER.info("提交状态;{},序列号:{}", sresp.getResult(), sresp.getSequence());
        LOGGER.info("提交状态：" + sresp.getResult() + " 序列号：" + sresp.getSequence());
        //System.out.println("提交状态：" + sresp.getResult() + " 序列号：" + sresp.getSequence());

        session.closeNotException();

    }

    public static void main(String[] args) throws Exception {
        //测试下行和状态报告
        //testMT();


        //测试上行
        String mobile ="8618660776642";
        String content = "您本次的验证码是：1234";
        String extNo = "";
        String identifier = "";
        sendSms(mobile,content,extNo,identifier);
    }

}
