package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.StopOpenLoopReq;
import entity.protocolRes.seerRes.GeneralRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ObjectUtil;
import util.SocketClient;
import util.TrimUtil;

import java.io.IOException;

public class StopOpenLoopService {
    public static final Logger log = LoggerFactory.getLogger(StopOpenLoopService.class);
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();


    public static GeneralRes stopOpenLoop(SocketClient socketClient, StopOpenLoopReq stopOpenLoopReq) throws IllegalAccessException, IOException, NoSuchFieldException, InterruptedException {
        byte[] hexStopReq = TrimUtil.seerReqInByteArray(stopOpenLoopReq);
        socketClient.setReqMessage(hexStopReq);
        socketClient.GetConnect();
        Thread.sleep(200);
        log.info(localClassName+":socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.getSocketLongConnHaveReq();
        }
        byte[] seerRes = socketClient.getResMessage();
        if (ObjectUtil.isNotNull(seerRes)) {
            log.info(localClassName+"seerRes length is " + seerRes.length);
        }
        GeneralRes generalRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), GeneralRes.class);
        log.info(localClassName+"locationMarkRes ret_code is " + generalRes.getRet_code());
        return generalRes;
    }
}
