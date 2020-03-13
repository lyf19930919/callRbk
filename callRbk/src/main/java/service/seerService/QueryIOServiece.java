package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.seerReq.QueryIOStatusReq;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketPort;
import util.SocketClient;
import util.TrimUtil;
import java.io.IOException;

/**
 * @description:
 * @ClassName:QueryDIServiece
 * @Description:
 * @Date: Create in 21:13 2019/8/25
 */
public class QueryIOServiece {
    public static final Logger log = LoggerFactory.getLogger(QueryIOServiece.class);

    /**
     * 查询agv的IO状态结果，返回IO数据
     * @param ip agvserver的静态ip地址
     * @param
     * @return
     */
    public static QueryIOStatusRes queryIOStatus(String ip, QueryIOStatusReq queryIOStatusReq) throws IllegalAccessException, IOException, NoSuchFieldException, InterruptedException {
        byte[] hexQueryIOStatus = TrimUtil.seerHexReq(queryIOStatusReq);
        log.info("hexQueryIOStatus is " + hexQueryIOStatus);
        SocketClient socketClient = new SocketClient(hexQueryIOStatus, ip, SocketPort.STATUS_PROTOCOL);
        socketClient.GetConnect();
        Thread.sleep(260);
        log.info("socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.sendSocketMessage();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info("seerRes length is " + seerRes.length);
        QueryIOStatusRes queryIOStatusRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), QueryIOStatusRes.class);
        log.info("QueryIOStatusRes is "+queryIOStatusRes);
        return queryIOStatusRes;
    }
}