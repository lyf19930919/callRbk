package entity.protocolReq.seerReq.seerReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.TrimUtil;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartOpenLoopReq {
    public static final String TYPE_NUMBER = TrimUtil.toHexTypeNumber(Long.toHexString(2010));
    private Double vx;
    private Double vy;
    private Double w;
}
