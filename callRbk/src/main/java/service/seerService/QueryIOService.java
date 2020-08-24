package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.QueryIOStatusReq;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.TrimUtil;
import java.io.IOException;

/**
 * @description: 查询IO状态
 * @ClassName:QueryDIServiece
 * @Description:
 * @Date: Create in 21:13 2019/8/25
 */
public class QueryIOService {
    public static final Logger log = LoggerFactory.getLogger(QueryIOService.class);
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();
    /**
     * 查询agv的IO状态结果，返回IO数据
     * @param socketClient
     * @param queryIOStatusReq
     * @return
     */
    public static QueryIOStatusRes queryIOStatus(SocketClient socketClient, QueryIOStatusReq queryIOStatusReq) throws IllegalAccessException, IOException, NoSuchFieldException, InterruptedException {
        byte[] hexQueryIOStatus = TrimUtil.seerReqInByteArray(queryIOStatusReq);
        socketClient.setReqMessage(hexQueryIOStatus);
        socketClient.GetConnect();
        log.info(localClassName+":hexQueryIOStatus is " + hexQueryIOStatus);
        Thread.sleep(260);
        log.info(localClassName+":socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.getSocketLongConnHaveReq();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info(localClassName+":seerRes length is " + seerRes.length);
        QueryIOStatusRes queryIOStatusRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), QueryIOStatusRes.class);
        log.info(localClassName+":queryIOStatusRes is "+queryIOStatusRes);
        return queryIOStatusRes;
    }
}