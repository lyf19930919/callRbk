package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.QueryLocationGuideReq;
import entity.protocolRes.seerRes.QueryLocationGuideRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.TrimUtil;

import java.io.IOException;

/**
 *
 */
public class LocationMarkStatusServer {
    public static final Logger log = LoggerFactory.getLogger(LocationMarkStatusServer.class);
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();
    /**
     *
     * @param socketClient
     * @param queryLocationGuideReq
     * @return 返回导航状态是4的时候代表导航完成
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InterruptedException
     */
    public static QueryLocationGuideRes queryLocationMarkStatus( SocketClient socketClient, QueryLocationGuideReq queryLocationGuideReq) throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        byte[] hexQueryLmStatus = TrimUtil.seerReqInByteArray(queryLocationGuideReq);
        socketClient.setReqMessage(hexQueryLmStatus);
        socketClient.GetConnect();
        log.info(localClassName+":hexLocationReq is " + hexQueryLmStatus);
        Thread.sleep(220);
        log.info(localClassName+":socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.getSocketLongConnHaveReq();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info(localClassName+":seerRes length is " + seerRes.length);
        QueryLocationGuideRes querylocationMarkRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), QueryLocationGuideRes.class);
        log.info(localClassName+":queryLocationMarkRes ret_code is "+querylocationMarkRes.task_status);
        return querylocationMarkRes;
    }
}
