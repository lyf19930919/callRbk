package control.seerControl;

import entity.protocolReq.seerReq.StartOpenLoopReq;
import exception.MyException;
import service.seerService.StartOpenLoopService;
import util.ObjectUtil;
import util.SocketClient;

import java.io.IOException;

public class StartOpenLoop {
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    public static SocketClient startOpenLoop(SocketClient socketClient, StartOpenLoopReq startOpenLoopReq) {
        SocketClient result = null;
        try {
            if (ObjectUtil.isNull(startOpenLoopReq) || ObjectUtil.isNull(socketClient)) {
                throw new MyException(localClassName, "seer Req or SocketClient is null");
            }
            result = StartOpenLoopService.startOpenLoop(socketClient, startOpenLoopReq);
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}

