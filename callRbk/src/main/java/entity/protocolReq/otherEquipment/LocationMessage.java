package entity.protocolReq.otherEquipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LocationMessage {
    private int row;                            //矩阵行
    private int column;                         //矩阵列
    private boolean useMatrix;                  //是否使用矩阵的库位管理
    private String writeDOFunctionMa;           //写入do时的功能码
    private String readDiFunctionMa;            //读取di时的功能码
    private int diStartAddress;                 //di起始地址
    private int doStartAddress;                 //do起始地址
    private String modbusServerAddress;         //模块ip
    private int modbusServerPort;               //模块端口号
    private int redLightAddress;                //三色灯红灯地址
    private int yellowLightAddress;             //三色灯黄灯地址
    private int greenLightAddress;              //三色灯绿灯地址
    private int buzzer;                         //蜂鸣器灯地址
    private int dOSetTime;                      //do置位保持时间
}
