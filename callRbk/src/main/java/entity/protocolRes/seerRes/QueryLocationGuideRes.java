package entity.protocolRes.seerRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @ClassName:QueryLocationGuideRes
 * @Description:
 * @Date: Create in 21:25 2019/8/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryLocationGuideRes {
//    注意返回值为4的时候才是路径导航至站点
    public int task_status;
    public Integer ret_code;
}