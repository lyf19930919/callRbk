package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.seerReq.StopOpenLoopReq;
import entity.protocolRes.seerRes.GeneralRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketPort;
import util.SocketClient;
import util.TrimUtil;

import java.io.IOException;

public class StopOpenLoopService {
    public static final Logger log = LoggerFactory.getLogger(StopOpenLoopService.class);

    public static GeneralRes stopOpenLoop(String agvAddress, StopOpenLoopReq stopOpenLoopReq) throws IllegalAccessException, IOException, NoSuchFieldException, InterruptedException {
        byte[] hexstopReq = TrimUtil.seerHexReq(stopOpenLoopReq);
        log.info("stopOpenLoopReq is " + hexstopReq);
        SocketClient socketClient = new SocketClient(hexstopReq, agvAddress, SocketPort.CONTROL_PROTOCOL);
        socketClient.GetConnect();
        Thread.sleep(220);
        log.info("socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.sendSocketMessage();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info("seerRes length is " + seerRes.length);
        GeneralRes generalRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), GeneralRes.class);
        log.info("LocationMarkRes ret_code is " + generalRes.getRet_code());
        return generalRes;
    }

    public static void main(String[] args) {
        String agvAddress = "192.168.1.68";
        StopOpenLoopReq stopOpenLoopReq = new StopOpenLoopReq();
        try {
            stopOpenLoop(agvAddress, stopOpenLoopReq);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
