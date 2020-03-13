package entity.protocolReq.seerReq.seerReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.TrimUtil;

/**
 * @description:
 * @ClassName:QueryLocationGuideReq
 * @Description:
 * @Date: Create in 21:27 2019/8/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryLocationGuideReq {
    public static final String TYPE_NUMBER = TrimUtil.toHexTypeNumber(Long.toHexString(1020));
    //    只获取简单的响应状态
    private boolean simple = true;
}