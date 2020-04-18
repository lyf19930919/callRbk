package service.UrService;

import entity.protocolRes.urRes.URRealTimeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.SocketPort;
import util.TrimUtil;

import java.io.IOException;
import java.net.Socket;
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
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();

    public static URRealTimeRes getURRealTimeDate(SocketClient socketClient) throws InterruptedException, IOException {
        Thread.sleep(150); //访问UR socket server时候该值不能过小，不然可能会造成数据冗余
        socketClient.GetConnect();
        socketClient.getSocketLongConnNoReq();
        byte[] URRealTimeDate = socketClient.getResMessage();
        log.info(localClassName + ":URRealTimeDate length " + URRealTimeDate);
        double[] doubles = new double[6];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = TrimUtil.bytesToDouble(TrimUtil.byteArraysReverse(Arrays.copyOfRange(URRealTimeDate, i * 8 + 252, i * 8 + 260)));
        }
        String[] sixAngle = TrimUtil.radToAngle(doubles);
        byte[] bytesLength = Arrays.copyOf(URRealTimeDate, 4);
        String byteLengthStr = TrimUtil.byteArrayToHexString(bytesLength);
        int reqLength = Integer.parseInt(byteLengthStr, 16);
        String URTime = TrimUtil.doubleToTime(TrimUtil.bytesToDouble(
                (TrimUtil.byteArraysReverse(Arrays.copyOfRange(URRealTimeDate, 4, 12)))));
        return new URRealTimeRes(reqLength, URTime, sixAngle);
    }
}
