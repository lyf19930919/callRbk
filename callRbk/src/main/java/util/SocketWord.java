package util;

/**
 * @ProjectName: callRbk$
 * @Package: util$
 * @ClassName: SocketWord$
 * @Description:使用一些socket字命令获取相应数据的方式 基于CB3系列的3.7版本, 关节使能上电和断电的对于复合机器人的电流影响在1.5A左右（ur5s）
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
    public static final String RESPONSE_START_UR = "Starting program";          //启动play成功之后的相应
    public static final String RESPONSE_PAUSE_UR = "Pausing program";           //暂停pause成功之后的相应
    public static final String RUNNING_STATUS_UR = "running\n";                    //ur的运动查询
    public static final String RESPONSE_RUNNING_TRUE = "Program running: true"; //运行状态running执行正确的反馈
    public static final String RESPONSE_RUNNING_FALSE = "Program running: false";//运行running暂停或者错误状态导致的非运行状态
    public static final String POWER_ON_UR = "power on\n";                         //UR关节使能上电(非示教器主板安全板上电)
    public static final String POWER_ON_TURE = "Powering on";                         //UR关节使能上电(非示教器主板安全板上电)正确反馈
    public static final String POWER_OFF_UR = "power off\n";                       //UR关节失去使能断电
    public static final String POWER_OFF_TURE = "Powering off";                       //UR关节失去使能断电正确反馈
    public static final String BREAK_RELEASE_UR = "brake release\n";               //UR解除关节伺服驱动器抱闸，当被Power off关闭之后，需要power on 和 break release
    public static final String BREAK_RELEASE_TURE = "Brake releasing";               //UR解除关节伺服驱动器抱闸正确反馈
    public static final String CLOSE_POPUP_UR = "close popup\n";                   //关闭UR的错误弹窗
    public static final String CLOSE_POPUP_TURE = "closing popup";                   //关闭UR的错误弹窗
    public static final String HEART_BEAT = "Heartbeat\n";                           //链接心跳检测

    /**
     *
     * @param speedPrecent
     * @return
     */
    public static String formatURSpeedControl(int speedPrecent) {
        float urSpeedPrecent =(float)speedPrecent / 100;
        String speedStr = String.valueOf(urSpeedPrecent);
        String speedControl = String.format("set speed %s \n", speedStr);
        System.out.println(speedControl);
        return speedControl;
    }

}
