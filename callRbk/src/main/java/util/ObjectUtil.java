package util;

/**
 *对象工具类
 */
public class ObjectUtil {

    public static boolean isNull(Object obj) {
        if(obj != null) {
            return false;
        }
        return true;
    }

    public static boolean isNotNull(Object obj) {
        if(obj != null) {
            return true;
        }
        return false;
    }
}
