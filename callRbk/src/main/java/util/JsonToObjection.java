package util;

import com.alibaba.fastjson.JSONObject;
import entity.protocolReq.otherEquipment.LocationMessage;
import exception.MyException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 将多库位的信息转成json文件序列化至一个map对象，key为库区名称，value为改库区的信息包括ip，port，库区管理的modbus的矩阵大小等信息
 */
@SuppressWarnings("unchecked")
public class JsonToObjection {
    private static Logger logger = LoggerFactory.getLogger(JsonToObjection.class);

    public static HashMap<String, LocationMessage> getLocationMessages() throws IOException {
        HashMap<String, LocationMessage> locationsMessageMap = new HashMap<String, LocationMessage>();
        InputStream inputStream = JsonToObjection.class.getResourceAsStream("/locationManagement");
        String locationMess = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        logger.info("json String is exist? " + locationMess);
        locationsMessageMap = (HashMap<String, LocationMessage>) JSONObject.parseObject(locationMess, Map.class);
        if (ObjectUtil.isNull(locationsMessageMap)) {
            throw new MyException("class name is JsonToObjection ", "no parse json text");
        }
        return locationsMessageMap;
    }

    public static void main(String[] args) {
        try {
            logger.info("cahgndu "+JsonToObjection.getLocationMessages().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
