package util;

/**
 * @ProjectName: callRbk$
 * @Package: util$
 * @ClassName: SocketWord$
 * @Description:使用一些socket字命令获取相应数据的方式 基于CB3系列的3.7版本
 * @Author: lyf
 * @CreateDate: 2020/3/9$ 9:34$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/3/9$ 9:34$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class SocketWord {
    /*
    * UR socket字通讯（dashBoard相关）
    * */
    public static final String START_UR = "play\n";                               //ur的快速启动
    public static final String PAUSE_UR = "pause\n";                              //ur的快速暂停
    public static final String RUNNING_STATUS_UR = "runnin\n";                    //ur的运动查询
    public static final String RESPONSE_START_UR = "Starting program\n";          //启动成功之后的相应
    public static final String RESPONSE_PAUSE_UR = "Pausing program\n";           //暂停成功之后的相应
    public static final String RESPONSE_RUNNING_TRUE = "Program running: true\n"; //运行状态
    public static final String RESPONSE_RUNNING_FALSE = "Program running: false\n";//暂停或者错误状态导致的非运行状态
}
