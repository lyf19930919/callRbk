package entity.protocolRes.seerRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryIOStatusRes {
    //    di状态
    private List<QueryIOStatusDI> DI;
    //    do状态
    private List<QueryIOStatusDO> DO;
    //    响应码
    public Integer ret_code;

}
