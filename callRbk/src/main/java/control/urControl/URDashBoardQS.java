package control.urControl;

import entity.protocolRes.urRes.URDashBoardRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UrService.URDashBoardService;
import util.SocketClient;
import util.SocketPort;
import util.SocketWord;

import java.io.IOException;

/**
 * @ProjectName: callRbk$
 * @Package: control.urControl$
 * @ClassName: URDashBoardQS$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2020/4/15$ 13:06$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/4/15$ 13:06$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class URDashBoardQS {
    public static Logger log = LoggerFactory.getLogger(URDashBoardQS.class);


    public static URDashBoardRes getURDashBoardQuickStart(SocketClient socketClient, String socketWord) {
        URDashBoardRes urDashBoardRes = null;
        try {
            urDashBoardRes = URDashBoardService.getURRealTimeDate(socketClient, socketWord);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return urDashBoardRes;
    }
}
