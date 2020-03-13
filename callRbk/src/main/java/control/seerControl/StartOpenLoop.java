package control.seerControl;

import entity.protocolReq.seerReq.seerReq.StartOpenLoopReq;
import service.seerService.StartOpenLoopService;
import util.ObjectUtil;
import util.SocketClient;

import java.io.IOException;

public class StartOpenLoop {
    public static SocketClient startOpenLoop(String agvAddress, StartOpenLoopReq startOpenLoopReq) {
        SocketClient result = null ;
        try {
            if(ObjectUtil.isNull(startOpenLoopReq)){
                return result;
            }
            result = StartOpenLoopService.startOpenLoop(agvAddress,startOpenLoopReq);
        }  catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}

