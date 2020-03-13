package control.seerControl;

import entity.protocolReq.seerReq.seerReq.BatchSetDoReq;
import entity.protocolRes.seerRes.GeneralRes;
import service.seerService.BatchSetDOService;
import util.ObjectUtil;

import java.io.IOException;

public class BatchSetDo {
    public static GeneralRes batchSetDoControl(String agvAddress, BatchSetDoReq[] batchSetDoReqs) {
        GeneralRes result = null ;
        try {
            if(ObjectUtil.isNull(batchSetDoReqs)){
                return result;
            }
            result = BatchSetDOService.batchSetDO(agvAddress, batchSetDoReqs);
        }  catch (NoSuchFieldException | IllegalAccessException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
