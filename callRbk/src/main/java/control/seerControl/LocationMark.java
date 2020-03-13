package control.seerControl;

import entity.protocolReq.seerReq.seerReq.LocationMarkReq;
import entity.protocolRes.seerRes.GeneralRes;
import service.seerService.LocationGuideService;
import util.ObjectUtil;

import java.io.IOException;

public class LocationMark {
    public static GeneralRes locationMarkcontrol(String agvAddress, LocationMarkReq locationMarkReq) {
        GeneralRes result = null;
        try {
            if (ObjectUtil.isNull(locationMarkReq)) {
                return result;
            }
            result = LocationGuideService.locationGuideControl(agvAddress, locationMarkReq);
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

}
