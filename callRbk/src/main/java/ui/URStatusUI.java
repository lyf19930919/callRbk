package ui;

import control.seerControl.QueryIOstatus;
import control.urControl.URRealTimeStatus;
import entity.protocolReq.seerReq.seerReq.QueryIOStatusReq;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import entity.protocolRes.urRes.URRealTimeRes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.SocketPort;
import util.SocketWord;
import util.StringMatch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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
    private final SocketClient socketClient = new SocketClient();

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
                byte[] quickStartUr = SocketWord.START_UR.getBytes();
                SocketClient socketClient = new SocketClient(quickStartUr, urRobotIp.getText(), SocketPort.UR_QUICKSTART29999_PORT);
                socketClient.GetConnect();
                log.info("启动socket链接状态" + socketClient.getIsDone());
                if (!StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
                        || !socketClient.getIsDone()) {
                    JOptionPane.showMessageDialog(urRobotIp, "请检查ip地址（start）", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    socketClient.sendLongSocketMessage();
                    Thread.sleep(100);
                    socketClient.sendLongSocketMessage();
                    log.info(new String(socketClient.getResMessage()));
                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane1.add(startUR);

        JButton pauseUR = new JButton("pauseUR");//       ur程序启动按钮
        pauseUR.setFocusPainted(false);//设置按钮文字无边框
        pauseUR.setBounds(628, 250, 85, 26);
        pauseUR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byte[] pauseUr = SocketWord.PAUSE_UR.getBytes();
                SocketClient socketClient = new SocketClient(pauseUr, urRobotIp.getText(), SocketPort.UR_QUICKSTART29999_PORT);
                socketClient.GetConnect();
                log.info("启动socket链接状态" + socketClient.getIsDone());
                if (!StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
                        || !socketClient.getIsDone()) {
                    JOptionPane.showMessageDialog(urRobotIp, "请检查ip地址（pause）", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    socketClient.sendLongSocketMessage();
                    Thread.sleep(100);
                    socketClient.sendLongSocketMessage();
                    log.info(new String(socketClient.getResMessage()));
                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane1.add(pauseUR);

        sure = new

                JButton("确定");//       确定按钮
        sure.setFocusPainted(false);//设置按钮文字无边框
        sure.setBounds(600, 30, 60, 26);
        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int delay = 100;
                if (!socketClient.getIsDone()) {
                    socketClient.setIp(urRobotIp.getText());
                    socketClient.setPort(SocketPort.UR_REALTIME30003_PORT);
                }
                socketClient.GetConnect();
                log.info("connection to ur(30003) status is " + socketClient.getIsDone());
                if (StringMatch.matchIp4Address(urRobotIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
                        && socketClient.getIsDone()) {
                    ActionListener taskPerformer = new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            timer.setDelay(200);  //200ms刷新一次参数
                            URRealTimeRes urRealTimeRes = URRealTimeStatus.getURRealTimeDate(urRobotIp.getText()); //此处为了方便使用了new来创建对象，可以自己更新成对象链接池来优化内存存储
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
            }
        });
        contentPane1.add(sure);
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
