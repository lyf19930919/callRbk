package control.seerControl;

import entity.protocolReq.seerReq.StopOpenLoopReq;
import entity.protocolRes.seerRes.GeneralRes;
import exception.MyException;
import service.seerService.StopOpenLoopService;
import util.ObjectUtil;
import util.SocketClient;

import java.io.IOException;

public class StopOpenLoop {
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    public static GeneralRes stopOpenLoop(SocketClient socketClient, StopOpenLoopReq stopOpenLoopReq) {
        GeneralRes result = null;
        try {
            if (ObjectUtil.isNull(stopOpenLoopReq) || ObjectUtil.isNull(socketClient)) {
                throw new MyException(localClassName, "seer Req or SocketClient is null");
            }
            result = StopOpenLoopService.stopOpenLoop(socketClient, stopOpenLoopReq);
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
