package control.seerControl;

import entity.protocolReq.seerReq.seerReq.QueryLocationGuideReq;
import entity.protocolRes.seerRes.QueryLocationGuideRes;
import service.seerService.LocationMarkStatusServer;
import util.ObjectUtil;

import java.io.IOException;

public class QueryLmStatus {
    public static QueryLocationGuideRes queryLMStatusControl(String agvAddress, QueryLocationGuideReq queryLocationGuideReq) {
      /*  String AGVip = "192.168.1.68";
        QueryLocationGuideReq queryLocationGuideReq = new QueryLocationGuideReq(true);*/
        QueryLocationGuideRes result = null;
        try {
            if(ObjectUtil.isNull(queryLocationGuideReq)){
                return result;
            }
            result = LocationMarkStatusServer.queryLocationMarkStatus(agvAddress, queryLocationGuideReq);
        } catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        返回值为4的时候代表路径导航完成
        return result;
    }
}
