package service.seerService;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.seerReq.StartOpenLoopReq;
import entity.protocolRes.seerRes.GeneralRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ObjectUtil;
import util.SocketClient;
import util.TrimUtil;

import java.io.IOException;

/**
 * @ProjectName: callRbk$
 * @Package: entity.protocolRes.seerRes$
 * @ClassName: QueryIOStatusDI$
 * @Description:开启开环控制
 * @Author: lyf
 * @CreateDate: 2020/4/14$ 17:51$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/4/14$ 17:51$
 * @UpdateRemark: 更新内容
 * @Version: 1.1
 */
public class StartOpenLoopService {
    public static final Logger log = LoggerFactory.getLogger(StartOpenLoopService.class);
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();


    public static SocketClient startOpenLoop( SocketClient socketClient, StartOpenLoopReq startOpenLoopReq) throws IllegalAccessException, IOException, NoSuchFieldException, InterruptedException {
        byte[] hexLocationReq = TrimUtil.seerReqInByteArray(startOpenLoopReq);
        socketClient.setReqMessage(hexLocationReq);
        Thread.sleep(120);
        log.info(localClassName+"socket connect status is " + socketClient.getIsDone());
        socketClient = socketClient.getSocketLongConnHaveReq();
        byte[] seerRes = socketClient.getResMessage();
        if (ObjectUtil.isNotNull(seerRes)) {
            log.info(localClassName+"seerRes length is " + seerRes.length);
        }
        GeneralRes generalRes = JSONObject.parseObject
                (TrimUtil.getSeerHexRes(seerRes), GeneralRes.class);
        log.info(localClassName+"startOpenLoopReq ret_code is " + generalRes.getRet_code());
        return socketClient;
    }
}
