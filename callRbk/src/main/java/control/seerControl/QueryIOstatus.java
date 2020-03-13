package control.seerControl;

import entity.protocolReq.seerReq.seerReq.QueryIOStatusReq;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import service.seerService.QueryIOServiece;
import util.ObjectUtil;

import java.io.IOException;

public class QueryIOstatus {
    public static QueryIOStatusRes queryIOStatusControl(String agvAddress,QueryIOStatusReq queryIOStatusReq) {
     /*   String AGVip = "192.168.1.68";
        QueryIOStatusReq queryLocationGuideReq = new QueryIOStatusReq();*/
        QueryIOStatusRes result = null;
        try {
            if(ObjectUtil.isNull(agvAddress) || ObjectUtil.isNull(queryIOStatusReq)){
                return null;
            }
            result = QueryIOServiece.queryIOStatus(agvAddress, queryIOStatusReq);
        } catch (IllegalAccessException|IOException|NoSuchFieldException|InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
            String AGVip = "192.168.1.68";
        QueryIOStatusReq queryLocationGuideReq = new QueryIOStatusReq();
        QueryIOStatusRes result = null;
        try {
            result = QueryIOServiece.queryIOStatus(AGVip, queryLocationGuideReq);
        } catch (IllegalAccessException|IOException|NoSuchFieldException|InterruptedException e) {
            e.printStackTrace();
        }

    }
}
