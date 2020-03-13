package util;

/**
 * Seer和UR的标准socket通信端口
 */
public final class SocketPort {
    //    seerAgvPort 仙知官方提供的netProtocol端口号
    public static final int STATUS_PROTOCOL = 19204; //状态端口
    public static final int CONTROL_PROTOCOL = 19205;//控制端口
    public static final int NAVIGATION_PROTOCOL = 19206;//导航端口
    public static final int CONFIG_PROTOCOL = 19207;//配置端口
    public static final int OTHER_PROTOCOL = 19210;//其他端口

    //    modbusTcp 标准的modbus tcp端口
    public static final int MODBUS_TCP_PORT = 502;//标准的modbus tcp通讯端口

    /*urPort   官方提供的标准端口号 CB3系列（适配UR3,5,10）；300001与30002相同，数据格式也相同；30003区别于
    *          前两个在于数据获取的频率，前者为10HZ,后者为125HZ;数据格式也有相应的区别
    * */
    public static final int UR_QUICKSTART29999_PORT = 29999;//ur的快速启动端口
    public static final int UR_REALTIME30001_PORT = 30001;//ur实时状态获取端口号30001
    public static final int UR_REALTIME30002_PORT = 30002;//ur实时状态获取端口号30002
    public static final int UR_REALTIME30003_PORT = 30003;//ur实时状态获取端口号30003


}
