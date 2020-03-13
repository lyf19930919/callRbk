package util;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Seer和UR的socket客户端的多线程实现
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class SocketClient {
    private static final Logger log = LoggerFactory.getLogger(SocketClient.class);
    private Socket socket;// socket对象
    private InputStream inputStream;//输入流
    private OutputStream outputStream;//输出流
    private Boolean isDone = false;//连接状态
    private byte[] reqMessage;
    private byte[] resMessage;
    @NonNull
    private String ip;
    @NonNull
    private Integer port;
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024 * 4];
    int n = 0;

    public SocketClient(byte[] reqMessage, String ip, Integer port) {
        this.reqMessage = reqMessage;
        this.ip = ip;
        this.port = port;
    }

    /**
     * 创建连接
     */
    public void GetConnect() {
       Thread thread = new Thread(new Runnable() {// 创建连接线程
            public void run() {
                try {
                    socket = new Socket(ip, port);// 创建连接到服务器的socket
                    log.info("socket connection success and socket is " + socket);
                    isDone = true;
                } catch (Exception e) {
                    log.error("could not connected to service socket ");
                }
            }
        });
       thread.start();
        try {
            thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * socket短链接，接受数据后销毁对象
     */
    public void sendSocketMessage() throws IOException {
        try {
            log.info("when getOutputStream/getInputStream with socket,socket is " + this.socket);
            outputStream = this.socket.getOutputStream();//绑定输出流到socket
            inputStream = this.socket.getInputStream();
            if (isDone) {
                log.info("send socket service requestData is " + reqMessage);
                outputStream.write(reqMessage);//发送信息
                outputStream.flush();//刷新缓存
                while (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                    log.info("socketRes data is " + n + " bytes");
                    resMessage = output.toByteArray();
//                    log.info("get socket service responseData  is " + new String(Arrays.copyOfRange(reqMessage, 16, reqMessage.length)));
                    return;
                }
            }
        } catch (IOException e) {
            isDone = false;
            log.error("could not connected to service socket");
        } finally {
            this.socket.close();
            this.inputStream.close();
            this.outputStream.close();
            log.info("socket&stream is closed");
        }
    }

    /**
     * socket长链接，不关闭socket
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public SocketClient sendLongSocketMessage() throws IOException, InterruptedException {
        try {
            log.info("when Seer getOutputStream/getInputStream with longSocket,longSocket is " + this.socket);
            outputStream = this.socket.getOutputStream();//绑定输出流到socket
            inputStream = this.socket.getInputStream();
            if (getIsDone()) {
                log.info("send longSocket service requestData is " + reqMessage);
                outputStream.write(reqMessage);//发送信息
                outputStream.flush();//刷新缓存
                if (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                    log.info("longSocket seerRes data is " + n + " bytes");
                    resMessage = output.toByteArray();
//                    log.info("get longSocket service responseData  is " + new String(Arrays.copyOfRange(reqMessage, 16, reqMessage.length)));
                }
                return this;
            }
        } catch (IOException e) {
            isDone = false;
            e.printStackTrace();
            log.error("could not connected to  Seer service socket(sendLongSocketMessage)");
        }
        return this;
    }

    /**
     * 获取UR的RealTime接口30003的实时数据信息 UR的该接口只需要创建连接就会获取数据，数据长度为1108
     * 个字节，需要按照UR官方提供的数据解析方式进行解析
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public SocketClient getURRealTimeSocketMessage() throws IOException, InterruptedException {
        try {
            log.info("when UR getOutputStream/getInputStream with longSocket,longSocket is " + this.socket);
            inputStream = this.socket.getInputStream();
            if (getIsDone()) {
                log.info("no should send UR longSocket service requestData");
                if (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                    log.info("UR longSocket seerRes data is " + n + " bytes");
                    resMessage = output.toByteArray();
                    log.info("get longSocket service responseData  is " + resMessage.length);
                }
                return this;
            }
        } catch (IOException e) {
            isDone = false;
            e.printStackTrace();
            log.error("could not connected to UR service socket(sendLongSocketMessage)");
        }
        return this;
    }
}
