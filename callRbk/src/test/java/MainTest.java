import control.seerControl.BatchSetDo;
import entity.protocolReq.seerReq.BatchSetDoReq;
import threadOperation.LocationDoInCycleOperation;
import threadOperation.LocationStatusInCycle;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: callRbk$
 * @Package: PACKAGE_NAME$
 * @ClassName: MainTest$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2019/11/25$ 13:56$
 * @UpdateUser: lyf
 * @UpdateDate: 2019/11/25$ 13:56$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class MainTest {


    public static void main(String[] args) {
        /**使用Executors工具快速构建对象*/
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();
        System.out.println("3秒后开始执行计划线程池服务..." + new Date());
        /**每间隔4秒执行一次任务*/
        scheduledExecutorService.scheduleAtFixedRate(new LocationDoInCycleOperation(),
                3, 4, TimeUnit.SECONDS);
    }


}
