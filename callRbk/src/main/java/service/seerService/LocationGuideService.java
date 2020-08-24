package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.LocationMarkReq;
import entity.protocolRes.seerRes.GeneralRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TrimUtil;
import util.SocketClient;

import java.io.*;

/**
 * @description:路径导航
 * @ClassName:LocationGuide
 * @Description:
 * @Date: Create in 21:09 2019/8/25
 */
public class LocationGuideService {
    public static final Logger log = LoggerFactory.getLogger(LocationGuideService.class);
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();
    /**
     * @param socketClient
     * @param locationMarkReq
     * @return 返回路径导航下发正确与否的标志，结果为0时就代表路径导航数据下发成功
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InterruptedException
     */
    public static GeneralRes locationGuideControl(SocketClient socketClient, LocationMarkReq locationMarkReq) throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        byte[] hexLocationReq = TrimUtil.seerReqInByteArray(locationMarkReq);
        socketClient.setReqMessage(hexLocationReq);
        socketClient.GetConnect();
        log.info(localClassName+":hexLocationReq is " + hexLocationReq);
        Thread.sleep(260);
        log.info(localClassName+":socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.getSocketLongConnHaveReq();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info(localClassName+":seerRes length is " + seerRes.length);
        GeneralRes generalRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), GeneralRes.class);
        log.info(localClassName+":locationGuideRes ret_code is " + generalRes.getRet_code());
        return generalRes;
    }
}