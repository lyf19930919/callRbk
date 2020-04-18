package control.seerControl;

import entity.protocolReq.seerReq.seerReq.QueryLocationGuideReq;
import entity.protocolRes.seerRes.QueryLocationGuideRes;
import exception.MyException;
import service.seerService.LocationMarkStatusServer;
import util.ObjectUtil;
import util.SocketClient;

import java.io.IOException;

public class QueryLmStatus {
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    public static QueryLocationGuideRes queryLMStatusControl(SocketClient socketClient, QueryLocationGuideReq queryLocationGuideReq) {
        QueryLocationGuideRes result = null;
        try {
            if (ObjectUtil.isNull(queryLocationGuideReq) || ObjectUtil.isNull(socketClient)) {
                throw new MyException(localClassName, "seer Req or SocketClient is null");
            }
            result = LocationMarkStatusServer.queryLocationMarkStatus(socketClient, queryLocationGuideReq);
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        返回值为4的时候代表路径导航完成
        return result;
    }
}
