package control.seerControl;

import entity.protocolReq.seerReq.BatchSetDoReq;
import entity.protocolRes.seerRes.GeneralRes;
import exception.MyException;
import service.seerService.BatchSetDOService;
import util.ObjectUtil;
import util.SocketClient;

import java.io.IOException;

public class BatchSetDo {
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    public static GeneralRes batchSetDoControl(SocketClient socketClient, BatchSetDoReq[] batchSetDoReqs) {
        GeneralRes result = null;
        try {
            if (ObjectUtil.isNull(batchSetDoReqs) || ObjectUtil.isNull(socketClient)) {
                throw new MyException(localClassName, "seer Req or SocketClient is null");
            }
            result = BatchSetDOService.batchSetDO(socketClient, batchSetDoReqs);
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
