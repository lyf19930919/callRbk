package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.seerReq.StartOpenLoopReq;
import entity.protocolRes.seerRes.GeneralRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketPort;
import util.SocketClient;
import util.TrimUtil;

import java.io.IOException;

public class StartOpenLoopService {
    public static final Logger log = LoggerFactory.getLogger(StartOpenLoopService.class);

    public static SocketClient startOpenLoop(String agvAddress, StartOpenLoopReq startOpenLoopReq) throws IllegalAccessException, IOException, NoSuchFieldException, InterruptedException {
        byte[] hexLocationReq = TrimUtil.seerHexReq(startOpenLoopReq);
        log.info("startOpenLoopReq is " + hexLocationReq);
        SocketClient socketClient = new SocketClient(hexLocationReq, agvAddress, SocketPort.CONTROL_PROTOCOL);
        socketClient.GetConnect();
        Thread.sleep(220);
        log.info("socket connect status is " + socketClient.getIsDone());
        socketClient = socketClient.sendLongSocketMessage();
        byte[] seerRes = socketClient.getResMessage();
        log.info("seerRes length is " + seerRes.length);
        GeneralRes generalRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), GeneralRes.class);
        log.info("startOpenLoopReq ret_code is " + generalRes.getRet_code());
        return socketClient;
    }
}
