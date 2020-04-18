package entity.protocolRes.seerRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: callRbk$
 * @Package: entity.protocolRes.seerRes$
 * @ClassName: QueryIOStatusDI$
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
public class QueryIOStatusDI {
    //di 的序号
    Integer id;
    // source: normal:普通 DI  virtual:虚拟 DI  modbus:modbus DI
    String source;
    //值
    Boolean status;
    //是否已经启用
    Boolean valid;
}
