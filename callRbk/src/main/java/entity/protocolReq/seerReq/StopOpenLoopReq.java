package entity.protocolReq.seerReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.TrimUtil;
@Data
@NoArgsConstructor
public class StopOpenLoopReq {
    public static final String TYPE_NUMBER = TrimUtil.toHexTypeNumber(Long.toHexString(2000));
//无json数据区
}
