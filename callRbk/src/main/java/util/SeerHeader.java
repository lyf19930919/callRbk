package util;

/**
 * Seer数据报文格式的请求头
 */
public class SeerHeader {
    //  1byte  协议头 SRC根据该字节数据判断是否应该是它该处理的信息
    public static final String MESSAGE_HEAD = "5A";
    //  1byte  协议主版本号
    public static final String PROTOCOL_VERSION = "01";
    //  2byte  协议的序号，此为用户自定义，用于区分响应与用户请求的数据是否一致
    public static final String SERIAL_NUMBER = "0001";
    //  4字节  数据区长度   可变值参数  当没有json数据区的时候，数据格式如下
    public static final String NO_DATA_LENGTH = "00000000";
    //  2字节  报文类型编号  可变值参数，但是在这里给出字段名称，以便利用反射获取类中定义的值
    public static final String TYPE_NUMBER_NAME = "TYPE_NUMBER";
    //  6字节  保留数据区为后续协议扩充使用目前数据区值为0
    public static final String RESERVED_DATA_AREA = "000000000000";

}
