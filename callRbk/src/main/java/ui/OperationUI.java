package ui;

import control.seerControl.*;
import entity.protocolReq.seerReq.seerReq.*;
import entity.protocolRes.seerRes.GeneralRes;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import entity.protocolRes.seerRes.QueryLocationGuideRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.StringMatch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


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
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                BatchSetDoReq setDoEntity2 = new BatchSetDoReq(2, false);
                BatchSetDoReq setDoEntity3 = new BatchSetDoReq(3, false);
                BatchSetDoReq[] batchSetDos = new BatchSetDoReq[]{setDoEntity2, setDoEntity3};
                BatchSetDo.batchSetDoControl(agvServerIp.getText(), batchSetDos);//设置do状态给UR手臂动作
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

        btnConnect = new JButton("执行");//       执行按钮
        btnConnect.setFocusPainted(false);//设置按钮文字无边框
        btnConnect.setBounds(399, 210, 60, 26);
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String agvAddresIp = agvServerIp.getText();
                String goAction = action.getSelectedItem().toString();
                if (!StringMatch.matchIp4Address(agvAddresIp, "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                QueryIOStatusReq queryIOStatusReq = new QueryIOStatusReq();
                QueryIOStatusRes queryIOStatusRes = QueryIOstatus.queryIOStatusControl(agvAddresIp, queryIOStatusReq);
                if (!queryIOStatusRes.getDI().get(12)) {
                    log.info("复位交互信号");
                    BatchSetDoReq setDoEntity01 = new BatchSetDoReq(14, false);//复位交互信号
                    BatchSetDoReq setDoEntity02 = new BatchSetDoReq(15, false);
                    BatchSetDoReq[] batchSetDos00 = new BatchSetDoReq[]{setDoEntity01, setDoEntity02};
                    BatchSetDo.batchSetDoControl(agvAddresIp, batchSetDos00);
                }
                if ((!queryIOStatusRes.getDI().get(12) && !queryIOStatusRes.getDO().get(14) && !queryIOStatusRes.getDO().get(15)) &&
                        goAction.equals("取料")) {
                    LocationMarkReq locationMarkReq = new LocationMarkReq("LM1");
                    GeneralRes result = LocationMark.locationMarkcontrol(agvAddresIp, locationMarkReq);//路径导航至LM1
                    if (result.ret_code == 0) {//到LM1路径导航数据下发成功
                        while (true) {
                            log.info("动作执行action is " + goAction);
                            try {
                                Thread.sleep(200);//循环发送socket时间间隔在200ms以上
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            QueryLocationGuideReq queryLocationGuideReq = new QueryLocationGuideReq(true);
                            QueryLocationGuideRes result1 = QueryLmStatus.queryLMStatusControl(agvAddresIp, queryLocationGuideReq);
                            if (result1.task_status == 4) {//路径导航结束 发送ur执行所需要的信号
                                BatchSetDoReq setDoEntity1 = new BatchSetDoReq(14, true);
                                BatchSetDoReq setDoEntity2 = new BatchSetDoReq(15, false);
                                BatchSetDoReq[] batchSetDos01 = new BatchSetDoReq[]{setDoEntity1, setDoEntity2};
                                BatchSetDo.batchSetDoControl(agvAddresIp, batchSetDos01);//设置do状态给UR手臂动作
                                break;
                            }
                        }
                    }
                }
                log.info("执行的是" + goAction + "已经完成");
//                    判断DI状态，证明ur已经完成抓取动作
                QueryIOStatusReq queryIOStatusReq2 = new QueryIOStatusReq();
                QueryIOStatusRes queryIOStatusRes2 = QueryIOstatus.queryIOStatusControl(agvAddresIp, queryIOStatusReq2);
                if ((queryIOStatusRes2.getDI().get(12) && queryIOStatusRes2.getDO().get(14) && !queryIOStatusRes2.getDO().get(15))
                        && goAction.equals("放料")) {
                    LocationMarkReq locationMarkReq2 = new LocationMarkReq("LM2");
                    GeneralRes result2 = LocationMark.locationMarkcontrol(agvAddresIp, locationMarkReq2);//路径导航至LM2
                    if (result2.ret_code == 0) {//到LM2的路径导航数据下发成功
                        while (true) {
                            try {
                                Thread.sleep(200);//循环发送socket时间间隔在200ms以上
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            QueryLocationGuideReq queryLocationGuideReq2 = new QueryLocationGuideReq(true);
                            QueryLocationGuideRes QLresult2 = QueryLmStatus.queryLMStatusControl(agvAddresIp, queryLocationGuideReq2);
                            if (QLresult2.task_status == 4) {//路径导航结束 发送ur执行所需要的信号
                                BatchSetDoReq setDoEntity2 = new BatchSetDoReq(14, false);
                                BatchSetDoReq setDoEntity3 = new BatchSetDoReq(15, true);
                                BatchSetDoReq[] batchSetDos02 = new BatchSetDoReq[]{setDoEntity2, setDoEntity3};
                                BatchSetDo.batchSetDoControl(agvAddresIp, batchSetDos02);//设置do状态给UR手臂动作
                                break;
                            }
                        }
                    }

                }
            }
        });
        contentPane.add(btnConnect);


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
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                log.info("鼠标点击过程中");
                beffer.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(0.5);
                socketClient = StartOpenLoop.startOpenLoop(agvServerIp.getText(), startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                beffer.setBackground(Color.WHITE);
                try {
                    socketClient.getSocket().close();
                    socketClient.getInputStream().close();
                    socketClient.getOutputStream().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(agvServerIp.getText(), StopOpenLoopReq);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        contentPane.add(beffer);


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
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                log.info("鼠标点击过程中");
                after.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(-0.5);
                socketClient = StartOpenLoop.startOpenLoop(agvServerIp.getText(), startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                after.setBackground(Color.WHITE);
                try {
                    socketClient.getSocket().close();
                    socketClient.getInputStream().close();
                    socketClient.getOutputStream().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(agvServerIp.getText(), StopOpenLoopReq);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        contentPane.add(after);


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
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                log.info("鼠标点击过程中");
                left.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(0.0);
                startOpenLoopReq.setW(5.0);
                socketClient = StartOpenLoop.startOpenLoop(agvServerIp.getText(), startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                left.setBackground(Color.WHITE);
                try {
                    socketClient.getSocket().close();
                    socketClient.getInputStream().close();
                    socketClient.getOutputStream().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(agvServerIp.getText(), StopOpenLoopReq);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        contentPane.add(left);

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
                if (!StringMatch.matchIp4Address(agvServerIp.getText(), "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")) {
                    JOptionPane.showMessageDialog(contentPane, "请输入正确的IP地址", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                log.info("鼠标点击过程中");
                right.setBackground(Color.GREEN);
                StartOpenLoopReq startOpenLoopReq = new StartOpenLoopReq();
                startOpenLoopReq.setVx(0.0);
                startOpenLoopReq.setW(-5.0);
                socketClient = StartOpenLoop.startOpenLoop(agvServerIp.getText(), startOpenLoopReq);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("鼠标释放");
                right.setBackground(Color.WHITE);
                try {
                    socketClient.getSocket().close();
                    socketClient.getInputStream().close();
                    socketClient.getOutputStream().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                StopOpenLoopReq StopOpenLoopReq = new StopOpenLoopReq();
                StopOpenLoop.stopOpenLoop(agvServerIp.getText(), StopOpenLoopReq);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        contentPane.add(right);
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