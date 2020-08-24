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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在库位模块的循环操作使能的情况下，使用该类去结合库位管理模块信息去上循环读取每个库位的状态
 */
public class LocationStatusInCycle implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(LocationStatusInCycle.class);
    // 防止并发修改异常
    private volatile boolean synchronizing = false;
    private AtomicInteger atomicInteger = null;

    public LocationStatusInCycle() {
        atomicInteger = new AtomicInteger(0);
    }

    @Override
    public void run() {
        try {
            if (synchronizing) {
                logger.warn("last timeTask is running ,this task is not generate");
                return;
            }
            synchronizing = true;
            String threadName = Thread.currentThread().getName();
            logger.info("read Location task is well be action in cycle && time && threadName is " + new Date() + ":" + threadName);
//          循环读取一次库位的状态
            HashMap<String, LocationMessage> locationMessageMap = JsonToObjection.getLocationMessages();
            HashMap<String, boolean[]> locationAreStatus = new HashMap<>();
            for (String key : locationMessageMap.keySet()) {
//                获取库区的信息 ip port 矩阵m*n di/do的起始地址等信息
                LocationMessage locationMessage = JSONObject.parseObject(String.valueOf(locationMessageMap.get(key)), LocationMessage.class);
                logger.info("locationMessage key is " + key + "entry locationMessage is "
                        + JSONObject.parseObject(String.valueOf(locationMessageMap.get(key)), LocationMessage.class));
//                做出是否使用矩阵库位管理的判断 如果使用的是矩阵管理的
                Modbus4jUtil modbus4jUtil = new Modbus4jUtil(locationMessage.getModbusServerAddress(),locationMessage.getModbusServerPort());
                logger.info("locationMessage of isUseMatrix value is  " + locationMessage.isUseMatrix());
                if (locationMessage.isUseMatrix()) {
                    boolean[] locationsDoStatus = modbus4jUtil.readCoilStatus(1,
                            locationMessage.getDoStartAddress(), locationMessage.getColumn());
//                读取每个库位的状态，把信息封装成一个map（库区名和一个库位状态的boolean Array）
                    for (boolean singleDoStatus : locationsDoStatus) {
                        if (singleDoStatus) {
                            logger.info("each singleDoStatus value is " + singleDoStatus);
                            boolean[] locationsDiStatus = modbus4jUtil.readInputStatus(1,
                                    locationMessage.getDiStartAddress(), locationMessage.getRow());
                            locationAreStatus.put(key, locationsDiStatus);
                            break;
                        }
                    }
                }
                if (!locationMessage.isUseMatrix()) {
                    boolean[] locationsDiStatus = modbus4jUtil.readInputStatus(1,
                            locationMessage.getDiStartAddress(), locationMessage.getRow());
                    locationAreStatus.put(key, locationsDiStatus);
                }
                synchronizing = false;
            }
            logger.info("locationAreStatus length is " + locationAreStatus.size());
            for (Map.Entry<String, boolean[]> entry : locationAreStatus.entrySet()) {
                logger.info("locations area name is " + entry.getKey() + "each location is " + Arrays.toString(entry.getValue()));
            }
            logger.info("read Location task was down && time && threadName is " + new Date() + ":" + threadName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new LocationStatusInCycle().run();
    }
}
