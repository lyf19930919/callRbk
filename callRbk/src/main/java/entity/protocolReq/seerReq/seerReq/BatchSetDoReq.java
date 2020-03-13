package entity.protocolReq.seerReq.seerReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.TrimUtil;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchSetDoReq {
    public static final String TYPE_NUMBER = TrimUtil.toHexTypeNumber(Long.toHexString(6002));
    private Integer id;
    private boolean status;
}
