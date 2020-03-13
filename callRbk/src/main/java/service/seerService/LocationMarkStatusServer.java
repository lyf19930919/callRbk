package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.seerReq.QueryLocationGuideReq;
import entity.protocolRes.seerRes.QueryLocationGuideRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketPort;
import util.SocketClient;
import util.TrimUtil;

import java.io.IOException;

public class LocationMarkStatusServer {
    public static final Logger log = LoggerFactory.getLogger(LocationMarkStatusServer.class);

    /**
     *
     * @param ip
     * @param queryLocationGuideReq
     * @return 返回导航状态是4的时候代表导航完成
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InterruptedException
     */
    public static QueryLocationGuideRes queryLocationMarkStatus(String ip, QueryLocationGuideReq queryLocationGuideReq) throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        byte[] hexQueryLmStatus = TrimUtil.seerHexReq(queryLocationGuideReq);
        log.info("hexLocationReq is " + hexQueryLmStatus);
        SocketClient socketClient = new SocketClient(hexQueryLmStatus, ip, SocketPort.STATUS_PROTOCOL);
        socketClient.GetConnect();
        Thread.sleep(220);
        log.info("socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.sendSocketMessage();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info("seerRes length is " + seerRes.length);
        QueryLocationGuideRes querylocationMarkRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), QueryLocationGuideRes.class);
        log.info("querylocationMarkRes ret_code is "+querylocationMarkRes.task_status);
        return querylocationMarkRes;
    }
}
