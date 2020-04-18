package service.UrService;

import entity.protocolRes.urRes.URDashBoardRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;

import java.io.IOException;

/**
 * @ProjectName: callRbk$
 * @Package: service.UrService$
 * @ClassName: URdashBoardService$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2020/4/15$ 12:57$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/4/15$ 12:57$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class URDashBoardService {
    public static final Logger log = LoggerFactory.getLogger(URDashBoardService.class);
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();
    public static URDashBoardRes getURRealTimeDate(SocketClient socketClient, String socketWord) throws InterruptedException, IOException, IOException {
        socketClient.setReqMessage(socketWord.getBytes());
        socketClient.getSocketLongConnHaveReq();
        String dashboardRES = new String(socketClient.getResMessage());
        log.info(localClassName+":dashboardRES is "+dashboardRES);
        return new URDashBoardRes(dashboardRES);
    }
}
