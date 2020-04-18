package entity.protocolRes.seerRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: callRbk$
 * @Package: entity.protocolRes.seerRes$
 * @ClassName: QueryIOStatusDo$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2020/4/14$ 17:51$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/4/14$ 17:51$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryIOStatusDO {
    //di 的序号
    Integer id;
    // source: normal普通 DO   modbus modbus DO
    String source;
    //值
    Boolean status;
}
