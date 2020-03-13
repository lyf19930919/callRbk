package entity.protocolRes.urRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @ProjectName: callRbk$
 * @Package: entity.protocolRes.urRes$
 * @ClassName: URStatusReq$
 * @Description:该相应是按照UR CB3系列 系统3.7来进行高频数据解析（realTime30003接口:因为该端口提供的数据最全面）
 * UR的字节存储为Big-Endian一般的编程语言中使用的都是Little-Endian，所以在解析出的报文中需要做倒序处理字节码
 * @Author: lyf
 * @CreateDate: 2020/3/9$ 11:27$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/3/9$ 11:27$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class URRealTimeRes {
    private int messageSize;           //相应包的长度
    private String nowDate;            //UR的系统时间,已经做过日期格式化的日期格式
    private String[] jointActual;      //UR的关节数据 单位:弧度，转角度需要format支持
}
