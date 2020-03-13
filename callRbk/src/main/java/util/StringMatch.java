package util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 使用正则表达式匹配正确的数字/字符等
 */
public class StringMatch {

    public static Boolean matchIp4Address(String ip,String matchCon) {
        Boolean match = false;
//        正则匹配关系
        if (ip != null && StringUtils.isNotEmpty(ip)) {
           match =  Pattern.matches(matchCon, ip);
        }
        if ("localhost".equals(ip)) {
            match = true;
        }
        return match;
    }

    //    判断输入的字符是否全是数字的字符
    public static boolean isNumeric(String waitTime) {
        boolean isnum = false;
        if (!waitTime.isEmpty()) {
            for (int i = 0; i < waitTime.length(); i++) {
                if (Character.isDigit(waitTime.charAt(i))) {
                    isnum = true;
                    return isnum;
                }
            }
        }
        return isnum;
    }

}
