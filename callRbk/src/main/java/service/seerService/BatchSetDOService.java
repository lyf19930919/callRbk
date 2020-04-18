package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.seerReq.BatchSetDoReq;
import entity.protocolRes.seerRes.GeneralRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketPort;
import util.SocketClient;
import util.TrimUtil;

import java.io.IOException;

/**
 * @description: 批量设置DO
 * @ClassName:SetDOService
 * @Description:
 * @Date: Create in 21:13 2019/8/25
 */
public class BatchSetDOService {
    public static final Logger log = LoggerFactory.getLogger(BatchSetDOService.class);
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    /**
     *
     * @param socketClient
     * @param batchSetDoReq
     * @return 返回路径导航下发正确与否的标志，结果为0时就代表路径导航数据下发成功
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InterruptedException
     */
    public static GeneralRes batchSetDO(SocketClient socketClient, BatchSetDoReq[] batchSetDoReq) throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        byte[] batchSetDOReq = TrimUtil.seerReqInByteArray(batchSetDoReq);
        socketClient.setReqMessage(batchSetDOReq);
        socketClient.GetConnect();
        log.info(localClassName+":batchSetDOReq is " + batchSetDOReq);
        Thread.sleep(260);
        log.info(localClassName+":socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.getSocketLongConnHaveReq();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info(localClassName+":seerRes length is " + seerRes.length);
        GeneralRes generalRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), GeneralRes.class);
        log.info(localClassName+":batchSetDORes ret_code is "+generalRes.getRet_code());
        return generalRes;
    }
}