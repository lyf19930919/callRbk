package entity.protocolReq.seerReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.TrimUtil;

@Data
@AllArgsConstructor
public class QueryIOStatusReq {
    public static final String TYPE_NUMBER = TrimUtil.toHexTypeNumber(Long.toHexString(1013));
//无json数据区
}
