package entity.protocolRes.seerRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryIOStatusRes {
//    DI的状态
    private List<Boolean> DI;
//    DI是否可用（true是可用，反之无效）
    private List<Boolean> DI_valid;
//    DO的状态
    private List<Boolean> DO;

}
