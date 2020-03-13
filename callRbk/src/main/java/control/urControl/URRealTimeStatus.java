package control.urControl;

import entity.protocolRes.urRes.URRealTimeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UrService.URRealTimeService;

import java.io.IOException;

/**
 * @ProjectName: callRbk$
 * @Package: control.urControl$
 * @ClassName: URJointStatus$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2020/3/5$ 17:37$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/3/5$ 17:37$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class URRealTimeStatus {
   public static Logger log = LoggerFactory.getLogger(URRealTimeStatus.class);

    public static URRealTimeRes getURRealTimeDate(String agvAddress) {
        URRealTimeRes urRealTimeRes = null;
        try {
            urRealTimeRes = URRealTimeService.getURRealTimeDate(agvAddress);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return urRealTimeRes;
    }
}
