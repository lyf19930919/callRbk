package util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则表达式匹配正确的数字/字符等
 */
public class StringMatch {
    private static Logger logger = LoggerFactory.getLogger(StringMatch.class);

    public static Boolean matchIp4Address(String ip, String matchCon) {
        Boolean match = false;
//        正则匹配关系
        if (ip != null && StringUtils.isNotEmpty(ip)) {
            match = Pattern.matches(matchCon, ip);
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

    public static String urScriptRadToAnglePosition(String path) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        String stringBody = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        logger.info("this file text String is " + stringBody);
        String rule = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(rule);
        Matcher m = pattern.matcher(stringBody);
        logger.info("m length is " + m.groupCount());
        logger.info("MatSample Text numbers is " + m.find());
        List<String> stringList = new ArrayList<>();
        int i = 0;
        StringBuffer replaceString = new StringBuffer();
        while (m.find()) {
            logger.info("match[i] is " + m.group());
            String string = m.group().substring(1, m.group().length() - 1);
            String[] strings = string.split(",");
            logger.info("remove [] String is " + Arrays.toString(strings));
            float[] angle = new float[strings.length];
            for (int j = 0; j < strings.length; j++) {
                logger.info("one of rad appearance String is" + strings[j] + " index number is " + j);
                float f = Float.parseFloat(strings[j]);
                angle[j] = (float) (f / Math.PI) * 180;
                logger.info("one of Angle appearance String is " + angle[j]);
            }
            logger.info("this line from rad to angle String is" + Arrays.toString(angle));
            stringList.add(Arrays.toString(angle));
            m.appendReplacement(replaceString, Arrays.toString(angle));
        }
        logger.info("stringList number is  "+stringList.size());

        while (m.find()) {
            String first =  m.group(1);
            logger.info("first" + first);
            m.appendReplacement(replaceString, stringList.get(i++));
        }
        logger.info("result out is "+replaceString.toString());
        return null;
    }

    public static void main(String[] args) throws IOException {
        urScriptRadToAnglePosition("C:\\Users\\17517\\Desktop\\西门子\\UR_XL_Table_Program.script");
    }
}
