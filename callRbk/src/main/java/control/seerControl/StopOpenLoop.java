package control.seerControl;

import entity.protocolReq.seerReq.seerReq.StopOpenLoopReq;
import entity.protocolRes.seerRes.GeneralRes;
import service.seerService.StopOpenLoopService;
import util.ObjectUtil;

import java.io.IOException;

public class StopOpenLoop {
    public static GeneralRes stopOpenLoop(String agvAddress, StopOpenLoopReq stopOpenLoopReq) {
        GeneralRes result = null ;
        try {
            if(ObjectUtil.isNull(stopOpenLoopReq)){
                return result;
            }
            result = StopOpenLoopService.stopOpenLoop(agvAddress,stopOpenLoopReq);
        }  catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
