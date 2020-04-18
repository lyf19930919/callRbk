package control.seerControl;

import entity.protocolReq.seerReq.seerReq.LocationMarkReq;
import entity.protocolRes.seerRes.GeneralRes;
import exception.MyException;
import service.seerService.LocationGuideService;
import util.ObjectUtil;
import util.SocketClient;

import java.io.IOException;

public class LocationMark {
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    public static GeneralRes locationMarkControl(SocketClient socketClient, LocationMarkReq locationMarkReq) {
        GeneralRes result = null;
        try {
            if (ObjectUtil.isNull(locationMarkReq)) {
                throw new MyException(localClassName, "seer Req or SocketClient is null");
            }
            result = LocationGuideService.locationGuideControl(socketClient, locationMarkReq);
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

}
