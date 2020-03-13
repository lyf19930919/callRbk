package entity.protocolReq.seerReq.seerReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.TrimUtil;

/**
 * @description:
 * @ClassName:LocationMark 到达所需要站点的信息，一般情况下只需要站点编号
 * @Description:
 * @Date: Create in 21:18 2019/8/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationMarkReq {
    public static final String TYPE_NUMBER = TrimUtil.toHexTypeNumber(Long.toHexString(3051));
    private String id;
}