package ui;

import control.seerControl.QueryIOstatus;
import entity.protocolReq.seerReq.QueryIOStatusReq;
import entity.protocolRes.seerRes.QueryIOStatusRes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SocketClient;
import util.SocketPort;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

@Data
@NoArgsConstructor
public class IOStatusUI extends JFrame {

    public static final Logger log = LoggerFactory.getLogger(IOStatusUI.class);

    private String agvAddress;
    private JPanel contentPane1;
    private Timer timer;
    private JButton di12;
    private JButton di3;
    private JButton do14;
    private JButton do15;
    SocketClient socketClient;

    public IOStatusUI(String agvAddress) {
        this.agvAddress = agvAddress;
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


        //        di数据展示
        JLabel SRCUsingDI = new JLabel("应用DI显示区");
        SRCUsingDI.setFont(f1);
        SRCUsingDI.setForeground(Color.GREEN);
        SRCUsingDI.setBounds(200, 200, 180, 50);
        contentPane1.add(SRCUsingDI);

        di12 = new JButton("DI12");
        di12.setBackground(Color.red);
        di12.setBounds(210, 280, 66, 30);
        di12.setFocusPainted(false);
        contentPane1.add(di12);

        di3 = new JButton("DI(未启用，可扩展)");
        di3.setBackground(Color.red);
        di3.setBounds(270, 280, 66, 30);
        di3.setFocusPainted(false);
        contentPane1.add(di3);

//        do数据展示
        //        di数据展示
        JLabel SRCUsingD0 = new JLabel("应用D0显示区");
        SRCUsingD0.setFont(f1);
        SRCUsingD0.setForeground(Color.green);
        SRCUsingD0.setBounds(500, 200, 180, 50);
        contentPane1.add(SRCUsingD0);

        do14 = new JButton("DO14");
        do14.setBackground(Color.red);
        do14.setBounds(520, 280, 66, 30);
        do14.setFocusPainted(false);
        contentPane1.add(do14);

        do15 = new JButton("DO15");
        do15.setBackground(Color.red);
        do15.setBounds(580, 280, 66, 30);
        do15.setFocusPainted(false);
        contentPane1.add(do15);
        socketClient = new SocketClient(agvAddress, SocketPort.CONTROL_PROTOCOL);
        socketClient.GetConnect();

        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                int delay = 100;
                ActionListener taskPerformer = new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        timer.setDelay(100);
                        QueryIOStatusReq queryIOStatusReq = new QueryIOStatusReq();
                        QueryIOStatusRes queryIOStatusRes = QueryIOstatus.queryIOStatusControl(socketClient, queryIOStatusReq);
                        log.info("do2：" + queryIOStatusRes.getDO().get(2) + " do3: " + queryIOStatusRes.getDO().get(3));
                       /* if (queryIOStatusRes.getDO().get(14)) {
                            do14.setBackground(Color.GREEN);
                        } else {
                            do14.setBackground(Color.RED);
                        }
                        if (queryIOStatusRes.getDO().get(15)) {
                            do15.setBackground(Color.GREEN);
                        } else {
                            do15.setBackground(Color.RED);
                        }
                        if (queryIOStatusRes.getDI().get(12)) {
                            di12.setBackground(Color.GREEN);
                        } else {
                            di12.setBackground(Color.RED);
                        }*/
                    }
                };
                timer = new Timer(delay, taskPerformer);
                timer.start();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                timer.stop();
            }
        });


    }
}
