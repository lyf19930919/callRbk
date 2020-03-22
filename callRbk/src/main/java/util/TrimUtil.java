package util;

import com.alibaba.fastjson.JSONObject;
import com.sun.imageio.plugins.common.I18N;
import exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * seer和UR封装数据的工具类
 */
public class TrimUtil {
    private static final Logger log = LoggerFactory.getLogger(TrimUtil.class);

    /**
     * 拼接16进制字符转成2进制数组的request body内容并返回
     *
     * @param operationReq 数据区对象
     * @return
     */
    public static byte[] seerHexReq(Object operationReq) throws NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
        StringBuffer stringReqHex = new StringBuffer("");
        String stringTexReq = null;
        Field field = operationReq.getClass().getDeclaredField(SeerHeader.TYPE_NUMBER_NAME);
        String TypeNumber = field.get(null).toString();
        Field[] fields = operationReq.getClass().getDeclaredFields();
        field.setAccessible(true);
        /*利用reflect中的属性个数判断是否有json的数据化区
        (个数为1的时候无数据区，个数大于1的时候为有数据区)
        * */
        stringReqHex = stringReqHex.append(SeerHeader.MESSAGE_HEAD).
                append(SeerHeader.PROTOCOL_VERSION).append(SeerHeader.SERIAL_NUMBER);
        if (ObjectUtil.isNotNull(operationReq) && fields.length > 1) {
            log.info("have json dataEarth");
//            封装数据长度到请求报文中
            stringTexReq = JSONObject.toJSONString(operationReq);
            log.info("request dataEar is " + stringTexReq);
            stringReqHex.append(toHexDataLength(stringTexReq.getBytes().length));
            log.info("HexDataLength is " + (JSONObject.toJSONString(operationReq)).getBytes().length + " after HexDataLength is   " + stringReqHex.toString());
//            封装报文编号到数据头当中
            stringReqHex.append(TypeNumber);
//           封装保留数据区
            stringReqHex.append(SeerHeader.RESERVED_DATA_AREA);
//            封装json序列化后的数据区
            log.info("request dataEar jsonToHEX is " + stringToHexStr(stringTexReq));
            stringReqHex.append(stringToHexStr(stringTexReq));
        } else if (fields.length == 1) {
            log.info("no json dataEar");
//        无json数据区长度
            stringReqHex.append(SeerHeader.NO_DATA_LENGTH);
            stringReqHex.append(TypeNumber);
//        封装保留数据区
            stringReqHex.append(SeerHeader.RESERVED_DATA_AREA);
        }
        return hexStringToByteArray(stringReqHex.toString());
    }

    /**
     * 数据区是对象数组的情况
     *
     * @param operationReq
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    public static byte[] seerHexReqWithList(Object[] operationReq) throws NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
        StringBuffer stringReqHex = new StringBuffer("");
        String stringTexReq = null;
        Field field = operationReq[0].getClass().getDeclaredField(SeerHeader.TYPE_NUMBER_NAME);
        String TypeNumber = field.get(null).toString();
        Field[] fields = operationReq[0].getClass().getDeclaredFields();
        field.setAccessible(true);
        /*利用reflect中的属性个数判断是否有json的数据化区
        (个数为1的时候无数据区，个数大于1的时候为有数据区)
        * */
        stringReqHex = stringReqHex.append(SeerHeader.MESSAGE_HEAD).
                append(SeerHeader.PROTOCOL_VERSION).append(SeerHeader.SERIAL_NUMBER);
        if (ObjectUtil.isNotNull(operationReq[0]) && fields.length > 1) {
            log.info("have json dataEarth");
//            封装数据长度到请求报文中
            stringTexReq = JSONObject.toJSONString(operationReq);
            log.info("request dataEar(with Arrays) is " + stringTexReq);
            stringReqHex.append(toHexDataLength(stringTexReq.getBytes().length));
            log.info("HexDataLength is " + (JSONObject.toJSONString(operationReq)).getBytes().length + " after HexDataLength is   " + stringReqHex.toString());
//            封装报文编号到数据头当中
            stringReqHex.append(TypeNumber);
//           封装保留数据区
            stringReqHex.append(SeerHeader.RESERVED_DATA_AREA);
//            封装json序列化后的数据区
            log.info("request dataEar jsonToHEX is " + stringToHexStr(stringTexReq));
            stringReqHex.append(stringToHexStr(stringTexReq));
        } else if (fields.length == 1) {
            log.info("no json(with Arrays) dataEar");
//        无json数据区长度
            stringReqHex.append(SeerHeader.NO_DATA_LENGTH);
            stringReqHex.append(TypeNumber);
//        封装保留数据区
            stringReqHex.append(SeerHeader.RESERVED_DATA_AREA);
        }
        return hexStringToByteArray(stringReqHex.toString());
    }

    /**
     * 按照seer Robotics的报文格式(16进制的字节数组),本文实例按照16进制表示的字符转成16进制数组的表示方式
     * 后面将报文请求体统一封装之后放入dataArea中
     *
     * @param operationReq
     * @return
     */
    public static byte[] seerRoboticsFormatReqBody(Object operationReq)
            throws NoSuchFieldException, IllegalAccessException {
        if (ObjectUtil.isNull(operationReq)) {
            log.error("operationReq is null");
            return null;
        }
        StringBuffer stringReqHex = new StringBuffer("");
        String stringTexReq = null;
        Field field = null;
        String TypeNumber = null;
        Field[] fields = null;
        if (operationReq.getClass().isArray()) {
            Object operationReqObj = Array.get(operationReq, 0);
            field = operationReqObj.getClass().getDeclaredField(SeerHeader.TYPE_NUMBER_NAME);
            TypeNumber = field.get(null).toString();
            fields = operationReqObj.getClass().getDeclaredFields();
            field.setAccessible(true);
        } else {
            field = operationReq.getClass().getDeclaredField(SeerHeader.TYPE_NUMBER_NAME);
            TypeNumber = field.get(null).toString();
            fields = operationReq.getClass().getDeclaredFields();
            field.setAccessible(true);
        }
        /*利用reflect中的属性个数判断是否有json的数据化区(个数为1的时候无数据区，个数大于1的时候为有数据区)
        * */
        stringReqHex = stringReqHex.append(SeerHeader.MESSAGE_HEAD).
                append(SeerHeader.PROTOCOL_VERSION).append(SeerHeader.SERIAL_NUMBER);
        if (ObjectUtil.isNotNull(fields) && fields.length > 1) {
            log.info("have json dataArea");
//            封装数据长度到请求报文中
            stringTexReq = JSONObject.toJSONString(operationReq);
            log.info("request dataEar(with Arrays) is " + stringTexReq);
            stringReqHex.append(toHexDataLength(stringTexReq.getBytes().length));
            log.info("HexDataLength is " + (JSONObject.toJSONString(operationReq)).getBytes().length + " after HexDataLength is   " + stringReqHex.toString());
//            封装报文编号到数据头当中
            stringReqHex.append(TypeNumber);
//           封装保留数据区
            stringReqHex.append(SeerHeader.RESERVED_DATA_AREA);
//            封装json序列化后的数据区
            log.info("request dataEar jsonToHEX is " + stringToHexStr(stringTexReq));
            stringReqHex.append(stringToHexStr(stringTexReq));
        } else if (ObjectUtil.isNotNull(fields) && fields.length == 1) {
            log.info("no json dataArea");
//        无json数据区长度
            stringReqHex.append(SeerHeader.NO_DATA_LENGTH);
            stringReqHex.append(TypeNumber);
//        封装保留数据区
            stringReqHex.append(SeerHeader.RESERVED_DATA_AREA);
        }
        return hexStringToByteArray(stringReqHex.toString());
    }

    /**
     * @param integer 请求参数为json序列化之后的数据长度
     * @return 返回为满足seer的protocol的数据长度（要求是4字节，不满足高位补0）
     */
    public static String toHexDataLength(Integer integer) {
        StringBuffer dataLength = new StringBuffer("");
        switch (Integer.toHexString(integer).length()) {
            case 1:
                dataLength.append("0000000").append(Long.toHexString(integer));
                break;
            case 2:
                dataLength.append("000000").append(Integer.toHexString(integer));
                break;
            case 3:
                dataLength.append("00000").append(Integer.toHexString(integer));
                break;
            case 4:
                dataLength.append("0000").append(Integer.toHexString(integer));
                break;
            case 5:
                dataLength.append("000").append(Integer.toHexString(integer));
                break;
            case 6:
                dataLength.append("00").append(Integer.toHexString(integer));
                break;
            case 7:
                dataLength.append("0").append(Integer.toHexString(integer));
                break;
            default:
                break;
        }
        return dataLength.toString().toUpperCase();
    }

    /**
     * 将net protocol获取的响应截取响应头之后剩下的部分获取
     *
     * @param seerHexRes
     * @return
     */
    public static String getSeerHexRes(byte[] seerHexRes) {
        byte[] couldUseBody = null;
        log.info("seerHexRes is " + seerHexRes);
        if (ObjectUtil.isNotNull(seerHexRes)) {
            couldUseBody = Arrays.copyOfRange(seerHexRes, 16, seerHexRes.length);
            log.info("could used hexResLength is " + couldUseBody.length);
        }
        if (couldUseBody.length == 0) {
//            无可解析的数据区
            return null;
        }
        return new String(couldUseBody);
    }
    /**
     * 如果请求的类型编号不满足4字节需要高位补“0”字节数不满足4字节的
     * 该方法至针对于目前编号转16进制字符之后字节数为3的情况，可根据传参自行补充该方法；
     */
    public static String toHexTypeNumber(String hexTypeNunber) {
        StringBuffer typeNumber = new StringBuffer("0");
        if (ObjectUtil.isNotNull(hexTypeNunber) && hexTypeNunber.getBytes().length == 3) {
            return typeNumber.append(hexTypeNunber.toUpperCase()).toString();
        }
        log.info("hexTypeNumber is " + typeNumber);
        return hexTypeNunber.toUpperCase().trim();
    }

    /**
     * 移除16进制字符串中的空格
     *
     * @param hexString
     * @return
     */
    public static String removeSpace(String hexString) {
        StringBuffer stringBuffer = new StringBuffer("");
        char[] chars = hexString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ' && chars[i] != '\n') {
                stringBuffer.append(chars[i]);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * String字符串转换成为16进制字符串
     *
     * @param str
     * @return
     */
    public static String stringToHexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder stringBuilder = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            stringBuilder.append(chars[bit]);
            bit = bs[i] & 0x0f;
            stringBuilder.append(chars[bit]);
        }
        return stringBuilder.toString().trim().toUpperCase();
    }

    /**
     * 字节数组转16进制表示的字符串
     *
     * @param bytes
     * @return
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 16进制表示的字符串转换成字节数组(无需Unicode解码)
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStringToByteArray(String hexStr) {
        hexStr = hexStr.toUpperCase();
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return bytes;
    }

    /**
     * 16进制表示的字符串转字节数组（与上方法相同）
     *
     * @param hex
     * @return
     */
    public static byte[] hexStrToByte(String hex) {
        ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
        for (int i = 0; i < hex.length(); i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            bf.put(b);
        }
        return bf.array();
    }


    /**
     * 将一个8byte的byte[]转成一个doubule数据
     *
     * @param
     * @return doubule数据
     */
    public static double bytesToDouble(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        double angle = Double.longBitsToDouble(value);
        log.info("bytes to double result is " + angle);
        return angle;
    }

    /**
     * 将一个字节数组转成一个float整形数据
     * @param arr 字节数组
     * @return
     */
    public static float byteArrayToFloat(byte[] arr) {
        int f = (0xff000000 	& (arr[0] << 24))  |
                (0x00ff0000 	& (arr[1] << 16))  |
                (0x0000ff00 	& (arr[2] << 8))   |
                (0x000000ff 	&  arr[3]);
        return Float.intBitsToFloat(f);
    }


    /**
     * 将一个字节数组转成一个短整形数组
     *
     * @param data
     * @return
     */
    public static short byteArrayToShort(byte[] data) {
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        for (int i = 0; i < 2; i++) {
            result <<= 8;
            temp = data[i] & mask;
            result |= temp;
        }
        return (short) result;
    }

    /**
     * 将16位的short转换成byte数组
     *
     * @param s
     * @return byte[] 长度为2
     */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 将一个32位的float装成4字节数组
     *
     * @param doubleRegister
     * @return
     */
    public static byte[] floatToByteArray(float doubleRegister) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(doubleRegister);
        String hexString = Integer.toHexString(fbit);
        log.info("float2 to HexString is " + byteArrayToHexString(hexStringToByteArray(hexString)));
        return hexStringToByteArray(hexString);
    }


    /**
     * 将一个字节数组倒序输出
     * 有很多方法可实现字节数组倒序，此处只是想试用一下util的Collections工具类
     *
     * @param bytes
     * @return
     */
    public static byte[] byteArraysReverse(byte[] bytes) {
        if (!ObjectUtil.isNotNull(bytes) || bytes.length == 0) {
            log.error(" Reverse bytes error because:bytes is null");
        }
        log.info("Reverse before bytes Hex String is " + byteArrayToHexString(bytes));
        int length = bytes.length;
        byte[] newBytes = new byte[length];
        ArrayList<Byte> arrayList = new ArrayList<Byte>();
        for (Byte b : bytes) {
            arrayList.add(b);
        }
        for (int i = length; i > 0; i--) {
            newBytes[length - i] = arrayList.get(i - 1);
        }
        log.info("Reverse after newBytes Hex String is " + byteArrayToHexString(newBytes));
        return newBytes;
    }

    /**
     * 将double类型转成字符串类型的时间
     *
     * @param time
     * @return
     */
    public static String doubleToTime(double time) {
//        byte[] tb = hexStringToByteArray("105839B4E8E8CE40");
//        Double d = bytesToDouble(tb);
        log.info("UR double Time is " + time);
        Double d = time;
        Calendar base = Calendar.getInstance();
        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
        //Delphi的日期类型从1899-12-30开始
        Date date = new Date(d.longValue() * 1000);
        log.info("UR now Date is " + outFormat.format(date));
        return outFormat.format(date);
    }

    /**
     * 将弧度数据转成角度数据
     *
     * @param rad
     * @return
     */
    public static String[] radToAngle(double[] rad) {
        if (!ObjectUtil.isNotNull(rad) || rad.length == 0) {
            log.error(" Reverse bytes error because:bytes is null");
        }
        String[] sixAngle = new String[rad.length];
        DecimalFormat df = new DecimalFormat("#0.000");
        for (int i = 0; i < rad.length; i++) {
            double angle = Math.toDegrees(rad[i]);
            sixAngle[i] = df.format(angle);
            log.info("joint " + i + "angle is " + sixAngle[i]);
        }
        return sixAngle;
    }

    /**
     * 检查当前jvm运行的大小端判断
     *
     * @return
     * @throws IllegalAccessException
     */
    public static Integer checkIsBigEndian() throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        long a = unsafe.allocateMemory(8);
        unsafe.putLong(a, 0x0102030405060708L);
        byte b = unsafe.getByte(a);
        ByteOrder byteOrder;
        Integer result = 0;
        switch (b) {
            case 0x01:   //最高位是0x01的话判断的时候大端
                byteOrder = ByteOrder.BIG_ENDIAN;
                result = 2;
                break;
            case 0x08:    //最高位是0x08的话判断的时候大端
                byteOrder = ByteOrder.LITTLE_ENDIAN;
                result = 1;
                break;
            default:
                byteOrder = null;
        }
        return result;
    }

    public static void main(String[] args) {
        float f = 0.2f;
        byte[] bytes = floatToByteArray(f);
        log.info(byteArrayToHexString(bytes) + "\n");
        byte[] lowWord = Arrays.copyOfRange(bytes, 2, 4);
        byte[] hightWord = Arrays.copyOfRange(bytes, 0, 2);
        try {
            throw new MyException("500", "s");
        } catch (MyException e) {
            e.printStackTrace();
        }
        System.out.println("ss ---");
        log.info("lower Word is " + byteArrayToHexString(lowWord) + "\n");
        log.info("Hight Word is " + byteArrayToHexString(hightWord) + "\n");
        log.info("lower Word  int is " + (short) Integer.parseInt(byteArrayToHexString(lowWord), 16) + "\n");

    }

}

