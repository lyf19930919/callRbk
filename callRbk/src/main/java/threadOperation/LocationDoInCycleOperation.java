package threadOperation;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.otherEquipment.LocationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JsonToObjection;
import util.Modbus4jUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * 对库位管理模块的do进行循环操作
 */
public class LocationDoInCycleOperation implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(LocationStatusInCycle.class);
    // 防止并发修改异常
    private volatile boolean synchronizing = false;

    @Override
    public void run() {
        try {
            if (synchronizing) {
                logger.warn("last timeTask is running ,this task is not generate");
                return;
            }
            synchronizing = true;
            String threadName = Thread.currentThread().getName();
            logger.info("operation LocationDO task is well be action in cycle && time && threadName is " + new Date() + ":" + threadName);
            HashMap<String, LocationMessage> locationMessageMap = JsonToObjection.getLocationMessages();
            HashMap<String, boolean[]> locationAreStatus = new HashMap<>();
            for (String key : locationMessageMap.keySet()) {
//                获取库区的信息 ip port 矩阵m*n di/do的起始地址等信息
                LocationMessage locationMessage = JSONObject.parseObject(String.valueOf(locationMessageMap.get(key)), LocationMessage.class);
                logger.info("locationMessage key is " + key + "entry locationMessage is "
                        + JSONObject.parseObject(String.valueOf(locationMessageMap.get(key)), LocationMessage.class));
                Modbus4jUtil modbus4jUtil = new Modbus4jUtil(locationMessage.getModbusServerAddress(), locationMessage.getModbusServerPort());
//               判断库位是否使用矩阵库位管理 将矩阵的do项全部复位
                if (locationMessage.isUseMatrix()) {
                    boolean[] resetDO = new boolean[locationMessage.getColumn()];
                    for (int i = 0; i < resetDO.length; i++) {
                        resetDO[i] = false;
                    }
                    modbus4jUtil.writeMultipleCoils(1, locationMessage.getDoStartAddress(), resetDO);
//                    定时轮训循环将set置位
                    for (int j = 0; j < resetDO.length; j++) {
                        if (j == 0) {
                            resetDO[j] = true;
                            logger.info("if resetDO[0] now resetDO to modbusUtil is" + Arrays.toString(resetDO));
                            modbus4jUtil.writeMultipleCoils(1, locationMessage.getDoStartAddress(), resetDO);
                            Thread.sleep(locationMessage.getDOSetTime());
                        } else {
                            resetDO[j - 1] = false;
                            resetDO[j] = true;
                            logger.info("if no equal to resetDO[0] now resetDO to modbusUtil is" + Arrays.toString(resetDO));
                        }
                        modbus4jUtil.writeMultipleCoils(1, locationMessage.getDoStartAddress(), resetDO);
                        Thread.sleep(locationMessage.getDOSetTime());
                        if (j == resetDO.length - 1) {
                            resetDO[j] = false;
                            logger.info("if resetDO[resetDO.length] now resetDO to modbusUtil is" + Arrays.toString(resetDO));
                            modbus4jUtil.writeMultipleCoils(1, locationMessage.getDoStartAddress(), resetDO);
                        }
                    }
                }
            }
            synchronizing = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new LocationDoInCycleOperation().run();
    }
}
