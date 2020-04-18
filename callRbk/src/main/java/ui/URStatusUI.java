package ui;

import control.seerControl.QueryIOstatus;
import control.urControl.URDashBoardQS;
import control.urControl.URRealTimeStatus;
import entity.protocolReq.seerReq.seerReq.QueryIOStatusReq;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import entity.protocolRes.urRes.URRealTimeRes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.sql.Time;

/**
 * @ProjectName: callRbk$
 * @Package: ui$
 * @ClassName: UrStatusUI$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2020/3/5$ 17:06$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/3/5$ 17:06$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
@Data
public class URStatusUI extends JFrame {
    public static final Logger log = LoggerFactory.getLogger(URStatusUI.class);

    private JPanel contentPane1;
    private JTextField urRobotIp;
    private JButton sure;
    private Timer timer;
    private SocketClient URDashBoard;
    private SocketClient URRealTime;
    private SocketClient URSpeedControl;

    public URStatusUI() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowsWedth = 900;
        int windowsHeight = 600;
        setBounds((width - windowsWedth) / 2, (height - windowsHeight) / 2, windowsWedth, windowsHeight);
        contentPane1 = new JPanel();
        contentPane1.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane1);
        contentPane1.setLayout(null);
        this.setEnabled(true);//窗口被新的窗口占用后，窗口不可用
        this.setVisible(true);
        Font f1 = new Font("宋体", Font.PLAIN, 28);
        Font f2 = new Font("宋体", Font.PLAIN, 16);
        Font f3 = new Font("宋体", Font.PLAIN, 18);


        JLabel tabIp = new JLabel("URRobotIp:");
        tabIp.setFont(f1);
        tabIp.setForeground(Color.PINK);
        tabIp.setBounds(200, 15, 180, 50);
        contentPane1.add(tabIp);

        urRobotIp = new JTextField();//      文本框输入
        urRobotIp.setFont(f3);
        urRobotIp.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {//文本失去焦点时候的状态处理
                if (!StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(urRobotIp, "请输入正确的IP(输入)", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {

            }
        });
        urRobotIp.setBounds(360, 30, 180, 26);
        contentPane1.add(urRobotIp);


        JLabel JointAngle = new JLabel("UR关节角度（rad）");
        JointAngle.setFont(f1);
        JointAngle.setForeground(Color.green);
        JointAngle.setBounds(75, 80, 260, 50);
        contentPane1.add(JointAngle);

        JLabel urTime = new JLabel("UR开机运行时间");
        urTime.setFont(f1);
        urTime.setForeground(Color.green);
        urTime.setBounds(350, 80, 220, 50);
        contentPane1.add(urTime);

        JLabel urSpeedContorl = new JLabel("UR速度控制");
        urSpeedContorl.setFont(f1);
        urSpeedContorl.setForeground(Color.green);
        urSpeedContorl.setBounds(350, 250, 220, 50);
        contentPane1.add(urSpeedContorl);


        JLabel urControl = new JLabel("URQuickStart");
        urControl.setFont(f1);
        urControl.setForeground(Color.green);
        urControl.setBounds(610, 80, 220, 50);
        contentPane1.add(urControl);

        final JLabel base = new JLabel("Base:");
        base.setFont(f1);
        base.setForeground(Color.BLACK);
        base.setBounds(80, 135, 80, 50);
        contentPane1.add(base);
        final JTextField baseAngle = new JTextField("0.00");
        baseAngle.setBounds(170, 150, 80, 26);
        contentPane1.add(baseAngle);

        JLabel shoulder = new JLabel("Shoulder:");
        shoulder.setFont(f1);
        shoulder.setForeground(Color.BLACK);
        shoulder.setBounds(45, 170, 130, 50);
        contentPane1.add(shoulder);
        final JTextField shoulderAngle = new JTextField("0.00");
        shoulderAngle.setBounds(170, 185, 80, 26);
        contentPane1.add(shoulderAngle);

        JLabel elbow = new JLabel("Elbow:");
        elbow.setFont(f1);
        elbow.setForeground(Color.BLACK);
        elbow.setBounds(80, 205, 130, 50);
        contentPane1.add(elbow);
        final JTextField elbowAngle = new JTextField("0.00");
        elbowAngle.setBounds(170, 220, 80, 26);
        contentPane1.add(elbowAngle);

        JLabel wrist1 = new JLabel("Wrist1:");
        wrist1.setFont(f1);
        wrist1.setForeground(Color.BLACK);
        wrist1.setBounds(70, 239, 145, 50);
        contentPane1.add(wrist1);
        final JTextField wrist1Angle = new JTextField("0.00");
        wrist1Angle.setBounds(170, 254, 80, 26);
        contentPane1.add(wrist1Angle);

        JLabel wrist2 = new JLabel("Wrist2:");
        wrist2.setFont(f1);
        wrist2.setForeground(Color.BLACK);
        wrist2.setBounds(70, 275, 145, 50);
        contentPane1.add(wrist2);
        final JTextField wrist2Angle = new JTextField("0.00");
        wrist2Angle.setBounds(170, 290, 80, 26);
        contentPane1.add(wrist2Angle);

        JLabel wrist3 = new JLabel("Wrist3:");
        wrist3.setFont(f1);
        wrist3.setForeground(Color.BLACK);
        wrist3.setBounds(70, 310, 145, 50);
        contentPane1.add(wrist3);
        final JTextField wrist3Angle = new JTextField("0.00");
        wrist3Angle.setBounds(170, 325, 80, 26);
        contentPane1.add(wrist3Angle);

        final JTextField urNowTime = new JTextField("00:00:00");
        urNowTime.setFont(f3);
        urNowTime.setBounds(398, 180, 90, 38);
        contentPane1.add(urNowTime);


        JButton startUR = new JButton("startUR");//       ur程序启动按钮
        startUR.setFocusPainted(false);//设置按钮文字无边框
        startUR.setBounds(630, 190, 80, 26);
        startUR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URDashBoard = new SocketClient(urRobotIp.getText(), SocketPort.UR_QUICKSTART29999_PORT);
                URDashBoard.GetConnect();
                if (!StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
                        || !URDashBoard.getIsDone()) {
                    JOptionPane.showMessageDialog(urRobotIp, "请检查ip地址（start）", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.HEART_BEAT);
                while (true){
                    URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.START_UR);
                    String result =  URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.RUNNING_STATUS_UR).getSocketWord();
                    if(result.contains(SocketWord.RESPONSE_RUNNING_TRUE)){
                        break;
                    }
                }
            }
        });
        contentPane1.add(startUR);

        JButton pauseUR = new JButton("pauseUR");//       ur程序暂停按钮
        pauseUR.setFocusPainted(false);//设置按钮文字无边框
        pauseUR.setBounds(628, 250, 85, 26);
        pauseUR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URDashBoard = new SocketClient(urRobotIp.getText(), SocketPort.UR_QUICKSTART29999_PORT);
                URDashBoard.GetConnect();
                if (!StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
                        || !URDashBoard.getIsDone()) {
                    JOptionPane.showMessageDialog(urRobotIp, "请检查ip地址（pause）", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.HEART_BEAT);
                while (true){
                    URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.PAUSE_UR);
                    String result =  URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.RUNNING_STATUS_UR).getSocketWord();
                    if(result.contains(SocketWord.RESPONSE_RUNNING_FALSE)){
                        break;
                    }
                }

            }
        });
        contentPane1.add(pauseUR);

        JButton powerON = new JButton("powerON");//       ur本体上电按钮
        powerON.setFocusPainted(false);//设置按钮文字无边框
        powerON.setBounds(622, 310, 99, 26);
        powerON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URDashBoard = new SocketClient(urRobotIp.getText(), SocketPort.UR_QUICKSTART29999_PORT);
                URDashBoard.GetConnect();
                if (!StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
                        || !URDashBoard.getIsDone()) {
                    JOptionPane.showMessageDialog(urRobotIp, "请检查ip地址（pause）", "提示", JOptionPane.INFORMATION_MESSAGE);
                }

                URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.CLOSE_POPUP_UR);
                String result = URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.BREAK_RELEASE_UR).getSocketWord();
                while (result.contains(SocketWord.POWER_ON_TURE)) {
                    result = URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.POWER_ON_UR).getSocketWord();
                }
                while (!result.contains(SocketWord.RESPONSE_RUNNING_TRUE)) {
                    URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.RUNNING_STATUS_UR);
                    result = URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.START_UR).getSocketWord();
                }
            }
        });
        contentPane1.add(powerON);

        JButton powerOFF = new JButton("powerOFF");//       ur本体断电按钮
        powerOFF.setFocusPainted(false);//设置按钮文字无边框
        powerOFF.setBounds(622, 370, 99, 26);
        URDashBoard = new SocketClient(urRobotIp.getText(), SocketPort.UR_QUICKSTART29999_PORT);
        URDashBoard.GetConnect();
        powerOFF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(urRobotIp, "请检查ip地址（pause）", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.PAUSE_UR);
                URDashBoardQS.getURDashBoardQuickStart(URDashBoard, SocketWord.POWER_OFF_UR);
            }
        });
        contentPane1.add(powerOFF);


        final JButton cancle = new JButton("取消");//       断开socket连接
        cancle.setFocusPainted(false);//设置按钮文字无边框
        cancle.setBounds(680, 30, 60, 26);
        cancle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ObjectUtil.isNull(URRealTime)) {
                    JOptionPane.showMessageDialog(contentPane1, "socket未连接或者连接错误", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                timer.stop();
                try {
                    URRealTime.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane1.add(cancle);

        sure = new JButton("确定");//       开启socket(realTime)按钮链接
        sure.setFocusPainted(false);//设置按钮文字无边框
        sure.setBounds(600, 30, 60, 26);
        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URRealTime = new SocketClient(urRobotIp.getText(), SocketPort.UR_REALTIME30003_PORT);
                URRealTime.GetConnect();
                int delay = 100;
                log.info("connection to ur(30003) status is " + URRealTime.getIsDone());

                if (StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
                        && URRealTime.getIsDone() && !cancle.isSelected()) {
                    ActionListener taskPerformer = new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            timer.setDelay(500);  //500ms刷新一次参数
                            int reConnTime = 0;
                            URRealTime = new SocketClient(urRobotIp.getText(), SocketPort.UR_REALTIME30003_PORT);
                            URRealTimeRes urRealTimeRes = URRealTimeStatus.getURRealTimeDate(URRealTime);
                            while (!URRealTime.getIsDone()) {
                                URRealTime = new SocketClient(urRobotIp.getText(), SocketPort.UR_REALTIME30003_PORT);
                                URRealTime.GetConnect();
                                reConnTime = ++reConnTime;
                                urRealTimeRes = URRealTimeStatus.getURRealTimeDate(URRealTime);
                                if (reConnTime > 5) {
                                    JOptionPane.showMessageDialog(urRobotIp, "无法重新连接至UR DashBoard Server " + " time: "
                                            + reConnTime, "提示", JOptionPane.INFORMATION_MESSAGE);
                                    break;
                                }
                            }
                            log.info("URRealTime Socket is " + URRealTime.getSocket()
                                    + " URRealTimeRes is" + urRealTimeRes.toString());
                            baseAngle.setText(urRealTimeRes.getJointActual()[0]);
                            shoulderAngle.setText(urRealTimeRes.getJointActual()[1]);
                            elbowAngle.setText(urRealTimeRes.getJointActual()[2]);
                            wrist1Angle.setText(urRealTimeRes.getJointActual()[3]);
                            wrist2Angle.setText(urRealTimeRes.getJointActual()[4]);
                            wrist3Angle.setText(urRealTimeRes.getJointActual()[5]);
                            urNowTime.setText(urRealTimeRes.getNowDate());
                        }
                    };
                    timer = new Timer(delay, taskPerformer);
                    timer.start();
                } else {
                    JOptionPane.showMessageDialog(urRobotIp, "请检查ip地址的正确性(sure)", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    URRealTime.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane1.add(sure);

        final JSlider slider = new JSlider(0, 100, 100);
        slider.setBounds(350, 300, 220, 50);
        slider.setSnapToTicks(false);
        // 添加刻度改变监听器
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    URSpeedControl = new SocketClient(urRobotIp.getText(), SocketPort.UR_REALTIME30002_PORT);
                    URSpeedControl.GetConnect();
                    URSpeedControl.setReqMessage(SocketWord.formatURSpeedControl(slider.getValue()).getBytes());
                    URSpeedControl.getSocketLongConnHaveReq();
                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        });
        contentPane1.setVisible(true);
        contentPane1.add(slider);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    URStatusUI frame = new URStatusUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
