package util;

import exception.MyException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
    public static String localClassName = new SecurityManager() {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }.getClassName();
    private Socket socket;// socket对象
    private InputStream inputStream;//输入流
    private OutputStream outputStream;//输出流
    private Boolean isDone=false;//连接状态
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
                    log.info(localClassName + ":will to connecting socketServerIP is  " + ip + " and port is " + port);
                    socket = new Socket(ip, port);// 创建连接到服务器的socket
                    socket.setSoTimeout(1000);
                    isDone = true;
                    log.info(localClassName + ":socket create success"+"and connection Status is "+isDone);
                } catch (Exception e) {
                    log.error(localClassName + ":could not connected to service socket ");
                }
            }
        });
        thread.start();
        try {
            thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

/*    *//**
     * 接受数据后销毁对象
     *//*
    public void sendSocketMessage() throws IOException {
        try {
            log.info(localClassName + ":when getOutputStream/getInputStream with socket,socket is " + this.socket);
            outputStream = this.socket.getOutputStream();//绑定输出流到socket
            inputStream = this.socket.getInputStream();
            if (isDone) {
                log.info(localClassName + ":send socket service requestData is " + reqMessage);
                outputStream.write(reqMessage);//发送信息
                outputStream.flush();//刷新缓存
                while (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                    log.info(localClassName + ":socketRes dataLength is " + n + " bytes");
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

    *//**
     * socket长链接不关闭socket
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     *//*
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
            }
        } catch (IOException e) {
            isDone = false;
            e.printStackTrace();
            log.error("could not connected to  Seer service socket(sendLongSocketMessage)");
        }
        return this;
    }*/

    /**
     * * 获取长链接的socket（无socket字或者数据区的发送情况），
     * UR的RealTime接口30003的实时数据信息 UR的该接口只需要创建连接就会获取数据，数据长度为1108
     * 个字节，需要按照UR官方提供的数据解析方式进行解析
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws MyException
     */
    public SocketClient getSocketLongConnNoReq() throws IOException, InterruptedException, MyException {
        inputStream = this.socket.getInputStream();
        log.info(localClassName + ":NoReq longSocket is connection");
        if (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
            resMessage = output.toByteArray();
        }
        log.info(localClassName + ":NoReq longSocket  responseData  byteLength is  " + resMessage.length);
        this.setReqMessage(resMessage);
        output.flush();
        if (this.getSocket().isClosed()) {
            isDone = false;
            throw new MyException(localClassName, ":NoReq service socket was closed abnormally");
        }
        return this;
    }

    /**
     * 获取长链接的socket（有socket字或者数据区的发送情况），
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws MyException
     */
    public SocketClient getSocketLongConnHaveReq() throws IOException, InterruptedException, MyException {
        outputStream = this.socket.getOutputStream();//绑定输出流到socket
        inputStream = this.socket.getInputStream();
        log.info(localClassName + "HaveReq longSocket is connection");
        outputStream.write(reqMessage);//发送信息
        outputStream.flush();//刷新缓存
        if (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
            resMessage = output.toByteArray();
            log.info(localClassName + ":HaveReq longSocket  responseData  byteLength is  " + resMessage.length);
        }
        if (this.getSocket().isClosed()) {
            isDone = false;
            throw new MyException(localClassName, "HaveReq service socket was closed abnormally");
        }
        return this;
    }

    /**
     * 关闭socket链接状态
     */
    public void closeSocketConnStatus() throws IOException, MyException {
        Socket socket = this.getSocket();
        socket.close();
        socket.getOutputStream().close();
        socket.getOutputStream().close();
        if (!socket.isClosed()) {
            throw new MyException(localClassName, "close Socket occur error");
        }
    }
}
