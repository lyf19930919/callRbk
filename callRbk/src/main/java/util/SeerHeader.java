package util;

/**
 * Seer数据报文格式的请求头
 */
public class SeerHeader {
    //    协议头
    public static final String MESSAGE_HEAD = "5A";
    //    协议版本号
    public static final String PROTOCOL_VERSION = "01";
    //    协议序号，此为用户自定义，响应与用户请求时保持一致
    public static final String SERIAL_NUMBER = "0001";
    //    数据区长度      4字节  可变参数  当没有json数据区的时候，数据格式如下
    public static final String NO_DATA_LENGTH = "00000000";
    //    报文类型编号    2字节  可变参数，但是在这里给出字段名称，以便反射机制获取各类下的数据场
    public static final String TYPE_NUMBER_NAME = "TYPE_NUMBER";
    //保留数据区
    public static final String RESERVED_DATA_AREA = "000000000000";


}
