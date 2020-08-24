package control.seerControl;

import entity.protocolReq.seerReq.QueryIOStatusReq;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import exception.MyException;
import service.seerService.QueryIOService;
import util.ObjectUtil;
import util.SocketClient;

import java.io.IOException;

public class QueryIOstatus {
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    public static QueryIOStatusRes queryIOStatusControl(SocketClient socketClient, QueryIOStatusReq queryIOStatusReq) {
        QueryIOStatusRes result = null;
        try {
            if (ObjectUtil.isNull(socketClient) || ObjectUtil.isNull(queryIOStatusReq)) {
                throw new MyException(localClassName, "seer Req or SocketClient is null");
            }
            result = QueryIOService.queryIOStatus(socketClient, queryIOStatusReq);
        } catch (IllegalAccessException | IOException | NoSuchFieldException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
