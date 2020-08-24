package ui;

import control.seerControl.*;
import entity.protocolReq.seerReq.*;
import entity.protocolRes.seerRes.GeneralRes;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import entity.protocolRes.seerRes.QueryLocationGuideRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.SocketPort;
import util.StringMatch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * 该项目是通过SeerRobotics的复合机器人来实现的，UR通过UR的DO给到继电器，继电器的常开触点接入到SRC知道UR的状态完成
 * （目前可用modbus tcp协议获取状态（因为这复合机器人当初是SRC1100的主控制器）），
 */
class OperationUI extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(OperationUI.class);
    private JPanel contentPane;
    private JTextArea text;
    private JTextField agvServerIp;
    private JButton btnConnect;
    private JComboBox<String> action;
    private JButton beffer;
    private JButton after;
    private JButton left;
    private JButton right;
    private JButton ioStatus;
    private JButton URStatus;
    private SocketClient socketClient;
    /**
     * Create the frame.
     */
    public OperationUI() {
        this.setTitle("UR复合机器人测试");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowsWedth = 900;
        int windowsHeight = 600;
        setBounds((width - windowsWedth) / 2, (height - windowsHeight) / 2, windowsWedth, windowsHeight);
        contentPane = new JPanel();
//        contentPane.setFocusable(true);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setVisible(true);
        this.setFocusable(true);


        JLabel tabIp = new JLabel("agvServerIp:");
        Font f1 = new Font("宋体", Font.PLAIN, 28);
        tabIp.setFont(f1);
        tabIp.setForeground(Color.PINK);
        tabIp.setBounds(260, 15, 180, 50);
        contentPane.add(tabIp);

        agvServerIp = new JTextField();//      文本框输入
        agvServerIp.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {//文本失去焦点时候的状态处理
                //初始化Do状态给UR
                BatchSetDoReq setDoEntity2 = new BatchSetDoReq(2, false);
                BatchSetDoReq setDoEntity3 = new BatchSetDoReq(3, false);
                BatchSetDoReq[] batchSetDos = new BatchSetDoReq[]{setDoEntity2, setDoEntity3};
                socketClient = new SocketClient(agvServerIp.getText(), SocketPort.OTHER_PROTOCOL);
                socketClient.GetConnect();
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                BatchSetDo.batchSetDoControl(socketClient, batchSetDos);
                try {
                    socketClient.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void focusGained(FocusEvent e) {

            }
        });
        agvServerIp.setBounds(430, 30, 120, 26);
        contentPane.add(agvServerIp);
        action = new JComboBox<String>();//       复选框
        action.addItem("取料");
        action.addItem("放料");
        action.setBounds(390, 120, 80, 26);
        contentPane.add(action);


        btnConnect = new JButton("执行");//       执行按钮
        btnConnect.setFocusPainted(false);//设置按钮文字无边框
        btnConnect.setBounds(399, 210, 60, 26);
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String agvAddresIp = agvServerIp.getText();
                String goAction = action.getSelectedItem().toString();
                QueryIOStatusReq queryIOStatusReq = new QueryIOStatusReq();
                socketClient = new SocketClient(agvServerIp.getText(), SocketPort.STATUS_PROTOCOL);
                socketClient.GetConnect();
                if (!StringMatch.matchIp4Address(agvAddresIp, "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                QueryIOStatusRes queryIOStatusRes = QueryIOstatus.queryIOStatusControl(socketClient, queryIOStatusReq);
                try {
                    socketClient.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (goAction.equals("取料")) {
                    socketClient = new SocketClient(agvServerIp.getText(), SocketPort.NAVIGATION_PROTOCOL);
                    socketClient.GetConnect();
                    LocationMarkReq locationMarkReq = new LocationMarkReq("LM1");
                    GeneralRes result = LocationMark.locationMarkControl(socketClient, locationMarkReq);//路径导航至LM1
//                   关闭导航socket
                    try {
                        socketClient.closeSocketConnStatus();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    if (result.ret_code == 0) {//到LM1路径导航数据下发成功
                        socketClient = new SocketClient(agvServerIp.getText(), SocketPort.STATUS_PROTOCOL);
                        socketClient.GetConnect();
                        while (true) {
                            log.info("动作执行action is " + goAction);
                            try {
                                Thread.sleep(200);//循环发送socket时间间隔在200ms以上
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            QueryLocationGuideReq queryLocationGuideReq = new QueryLocationGuideReq(true);
                            QueryLocationGuideRes result1 = QueryLmStatus.queryLMStatusControl(socketClient, queryLocationGuideReq);
                            if (result1.task_status == 4) {//路径导航结束 发送ur执行所需要的信号
                                try {
                                    socketClient.closeSocketConnStatus();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                }
                socketClient = new SocketClient(agvServerIp.getText(), SocketPort.STATUS_PROTOCOL);
                socketClient.GetConnect();
                BatchSetDoReq setDoEntity1 = new BatchSetDoReq(14, true);
                BatchSetDoReq setDoEntity2 = new BatchSetDoReq(15, false);
                BatchSetDoReq[] batchSetDos01 = new BatchSetDoReq[]{setDoEntity1, setDoEntity2};
                BatchSetDo.batchSetDoControl(socketClient, batchSetDos01);//设置do状态给UR手臂动作
                try {
                    socketClient.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                log.info("执行的是" + goAction + "已经完成");
//                    判断DI状态，证明ur已经完成抓取动作
                if (goAction.equals("放料")) {
                    socketClient = new SocketClient(agvServerIp.getText(), SocketPort.NAVIGATION_PROTOCOL);
                    socketClient.GetConnect();
                    LocationMarkReq locationMarkReq2 = new LocationMarkReq("LM2");
                    GeneralRes result2 = LocationMark.locationMarkControl(socketClient, locationMarkReq2);//路径导航至LM2
                    if (result2.ret_code == 0) {//到LM2的路径导航数据下发成功
                        try {
                            socketClient.closeSocketConnStatus();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        socketClient = new SocketClient(agvServerIp.getText(), SocketPort.STATUS_PROTOCOL);
                        socketClient.GetConnect();
                        while (true) {
                            try {
                                Thread.sleep(200);//循环发送socket时间间隔在200ms以上
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            QueryLocationGuideReq queryLocationGuideReq2 = new QueryLocationGuideReq(true);
                            QueryLocationGuideRes QLresult2 = QueryLmStatus.queryLMStatusControl(socketClient, queryLocationGuideReq2);
                            if (QLresult2.task_status == 4) {//路径导航结束 发送ur执行所需要的信号
                                try {
                                    socketClient.closeSocketConnStatus();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                break;
                            }
                        }
                        socketClient = new SocketClient(agvServerIp.getText(), SocketPort.OTHER_PROTOCOL);
                        socketClient.GetConnect();
                        BatchSetDoReq setDoEntity3 = new BatchSetDoReq(14, false);
                        BatchSetDoReq setDoEntity4 = new BatchSetDoReq(15, true);
                        BatchSetDoReq[] batchSetDos02 = new BatchSetDoReq[]{setDoEntity3, setDoEntity4};
                        BatchSetDo.batchSetDoControl(socketClient, batchSetDos02);//设置do状态给UR手臂动作
                        try {
                            socketClient.closeSocketConnStatus();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            }
        });
        contentPane.add(btnConnect);


        //开环控制前进按钮
        Font f2 = new Font("宋体", Font.ITALIC, 40);
        beffer = new JButton("W");
        beffer.setFocusPainted(false);
        beffer.setFont(f2);
        beffer.setBackground(Color.WHITE);
        beffer.setBounds(120, 50, 66, 66);
        beffer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                log.info("鼠标点击W过程中");
                beffer.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(0.5);
                socketClient = new SocketClient(agvServerIp.getText(), SocketPort.CONTROL_PROTOCOL);
                socketClient.GetConnect();
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                StartOpenLoop.startOpenLoop(socketClient, startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                beffer.setBackground(Color.WHITE);
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(socketClient, StopOpenLoopReq);
                try {
                    socketClient.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        contentPane.add(beffer);


//        开环控制后退按钮
        after = new JButton("S");
        after.setFocusPainted(false);
        after.setFont(f2);
        after.setBackground(Color.WHITE);
        after.setBounds(120, 122, 66, 66);
        after.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                log.info("鼠标点击S过程中");
                after.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(-0.5);
                socketClient = new SocketClient(agvServerIp.getText(), SocketPort.CONTROL_PROTOCOL);
                socketClient.GetConnect();
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                StartOpenLoop.startOpenLoop(socketClient, startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                after.setBackground(Color.WHITE);
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(socketClient, StopOpenLoopReq);
                try {
                    socketClient.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        contentPane.add(after);

//      开环控制逆时针转动
        left = new JButton("A");
        left.setFocusPainted(false);
        left.setFont(f2);
        left.setBackground(Color.WHITE);
        left.setBounds(50, 122, 66, 66);
        left.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                log.info("鼠标点击A过程中");
                left.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(0.0);
                startOpenLoopReq.setW(5.0);
                socketClient = new SocketClient(agvServerIp.getText(), SocketPort.CONTROL_PROTOCOL);
                socketClient.GetConnect();
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                StartOpenLoop.startOpenLoop(socketClient, startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                left.setBackground(Color.WHITE);
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(socketClient, StopOpenLoopReq);
                try {
                    socketClient.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        contentPane.add(left);

//        开环控制顺时针旋转
        right = new JButton("D");
        right.setFocusPainted(false);
        right.setFont(f2);
        right.setBackground(Color.WHITE);
        right.setBounds(190, 122, 66, 66);
        right.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                log.info("鼠标点击D过程中");
                right.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(0.0);
                startOpenLoopReq.setW(-5.0);
                socketClient = new SocketClient(agvServerIp.getText(), SocketPort.CONTROL_PROTOCOL);
                socketClient.GetConnect();
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                StartOpenLoop.startOpenLoop(socketClient, startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                right.setBackground(Color.WHITE);
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(socketClient, StopOpenLoopReq);
                try {
                    socketClient.closeSocketConnStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        contentPane.add(right);

//        IO状态显示界面
        ioStatus = new JButton("IO状态显示");
        ioStatus.setFocusPainted(false);
        ioStatus.setBounds(666, 120, 120, 26);
        ioStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                IOStatusUI ioStatusUI = new IOStatusUI(agvServerIp.getText());
                ioStatusUI.setVisible(true);
                ioStatusUI.setTitle("IO状态显示");
            }
        });
        contentPane.add(ioStatus);

//        UR的QKStart和状态显示
        URStatus = new JButton("UR快速启动和状态显示");
        URStatus.setFocusPainted(false);
        URStatus.setBounds(666, 150, 120, 26);
        URStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URStatusUI urStatusUI = new URStatusUI();
                urStatusUI.setVisible(true);
                urStatusUI.setTitle("UR控制/显示界面");
            }
        });
        contentPane.add(URStatus);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OperationUI frame = new OperationUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}