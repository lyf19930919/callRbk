package service.UrService;

import entity.protocolRes.urRes.URRealTimeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.SocketPort;
import util.TrimUtil;

import java.io.IOException;
import java.util.Arrays;

/**
 * @ProjectName: callRbk$
 * @Package: service.UrService$
 * @ClassName: UrStatusService$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2020/3/5$ 17:42$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/3/5$ 17:42$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class URRealTimeService {
    public static final Logger log = LoggerFactory.getLogger(URRealTimeService.class);
    public static URRealTimeRes getURRealTimeDate(String agvAddress) throws InterruptedException, IOException {
        SocketClient socketClient = new SocketClient(agvAddress, SocketPort.UR_REALTIME30003_PORT);
        socketClient.GetConnect();
        Thread.sleep(10); //访问UR socket server时候该值不能过大，不然可能会造成数据冗余
        log.info("socket connect status is " + socketClient.getIsDone());
        if (socketClient.getIsDone()) {
            socketClient.getURRealTimeSocketMessage();
        }
        byte[] seerRes = socketClient.getResMessage();
        log.info("URRes length is " + seerRes.length);

        double[] doubles = new double[6];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = TrimUtil.bytesToDouble(TrimUtil.byteArraysReverse(Arrays.copyOfRange(seerRes, i * 8 + 252, i * 8 + 260)));
        }
        String[] sixAngle = TrimUtil.radToAngle(doubles);
        byte[] bytesLength = Arrays.copyOf(seerRes, 4);
        String byteLengthStr = TrimUtil.byteArrayToHexString(bytesLength);
        int reqLength = Integer.parseInt(byteLengthStr, 16);
        String URTime = TrimUtil.doubleToTime(TrimUtil.bytesToDouble(
                (TrimUtil.byteArraysReverse(Arrays.copyOfRange(seerRes, 4, 12)))));
        return new URRealTimeRes(reqLength, URTime, sixAngle);
    }
}
