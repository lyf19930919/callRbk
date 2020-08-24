package util;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.*;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static util.TrimUtil.byteArrayToHexString;

/**
 * modbus4j工具类，提供modbus对于数据的读写，包括读取线圈（可读写）的状态（read coils 01），读取开关量的状态（read Discrete inputs 02）
 * 读取保持寄存器（可读写）状态（read Holding Registers 03），读取输入寄存器状态（read Input Resister 04）；以及写入单个开关量数据(write single coil 05)
 * 批量写入开关量数据（write multiple coils 15)，写单个保持寄存器（write Single Register 06），批量写入保持寄存器（write Multiple Holding Register 16）
 * 注意：modbus4j采用的是short数据以匹配modbus的寄存器值，所以如果需要在显示的时候时候需要在modbus测试工具中采用有符号显示
 */
@Data
public class Modbus4jUtil {
    private static Logger logger = LoggerFactory.getLogger(Modbus4jUtil.class);
    /**
     * 工厂
     */
    private String slaveIP;
    private int port;
    private ModbusMaster modbusMaster;

    static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    public Modbus4jUtil(String slaveIP, int port) {
        this.port = port;
        this.slaveIP = slaveIP;
        IpParameters ipParameters = new IpParameters();
        ipParameters.setHost(slaveIP);
        ipParameters.setPort(port);
        ModbusMaster modbusMaster = modbusFactory.createTcpMaster(ipParameters, false);
//        设置超时连接的时间
        modbusMaster.setTimeout(500);
//        设置重复链接的次数
        modbusMaster.setRetries(2);
        try {
            modbusMaster.init();
        } catch (ModbusInitException e) {
            logger.error("modbusMaster init was errored & errorMsg is " + e.getMessage());
        }
        this.modbusMaster = modbusMaster;
    }


    /**
     * 批量读取coil状态[01 slave Coil Status 0x]
     *
     * @param offset 需要读取的线圈地址
     * @return 读取值
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public boolean[] readCoilStatus(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        verifyAddressAndLength(slaveID, offset, len);
        // 01 Coil Status
        ModbusMaster modbusMaster = getModbusMaster();
        ReadCoilsRequest readCoilsRequest = new ReadCoilsRequest(slaveID, offset, len);
        ReadCoilsResponse readCoilsResponse = (ReadCoilsResponse) modbusMaster.send(readCoilsRequest);
        if (readCoilsResponse.isException()) {
            logger.error("readCoilResponseException(01) message is" + readCoilsResponse.getExceptionMessage());
        }
        boolean[] coilStatus = readCoilsResponse.getBooleanData();
        logger.info("query all of offsetCoil(01) status is" + Arrays.toString(Arrays.copyOfRange(coilStatus, 0, len)));
        modbusMaster.destroy();
        return Arrays.copyOfRange(coilStatus, 0, len);
    }

    /**
     * 批量读取[02 Input Status 1x]类型 开关量数据(注意：需要注意返回的boolean数组是8的倍数，
     * 例如小于8为一个字节，大于8小于16为两个字节，16个bit位)
     *
     * @param slaveID
     * @param offset
     * @param len
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public boolean[] readInputStatus(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        logger.info("read discreteInput slaveID is " + slaveID + " offset is " + offset + " len is" + len);
        verifyAddressAndLength(slaveID, offset, len);
        // 02 Input Status
        ModbusMaster modbusMaster = getModbusMaster();
        ReadDiscreteInputsRequest readDiscreteInputsRequest = new ReadDiscreteInputsRequest(slaveID, offset, len);
        ReadDiscreteInputsResponse readDiscreteInputsResponse = (ReadDiscreteInputsResponse) modbusMaster
                .send(readDiscreteInputsRequest);
        if (readDiscreteInputsResponse.isException()) {
            logger.info("readDiscreteInputsException(02) Message is" + readDiscreteInputsResponse.getExceptionMessage());
        }
        boolean[] inputStatus = readDiscreteInputsResponse.getBooleanData();
        logger.info("discreteInputOffset status(02) is " + Arrays.toString(Arrays.copyOfRange(inputStatus, 0, len)));
        modbusMaster.destroy();
        return Arrays.copyOfRange(inputStatus, 0, len);
    }

    /**
     * 批量读取[03 Holding Register类型 2x]读取保持寄存器的数据
     *
     * @param slaveID
     * @param offset
     * @param len
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public short[] readHoldingRegister(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        verifyAddressAndLength(slaveID, offset, len);
        // 03 Holding Register类型数据读取
        ModbusMaster modbusMaster = getModbusMaster();
        ReadHoldingRegistersRequest readHoldingRegistersRequest = new ReadHoldingRegistersRequest(slaveID, offset, len);
        ReadHoldingRegistersResponse readHoldingRegistersResponse = (ReadHoldingRegistersResponse) modbusMaster.
                send(readHoldingRegistersRequest);
        if (readHoldingRegistersResponse.isException()) {
            logger.error("readHoldingRegisterException(03) Message is" + readHoldingRegistersResponse.getExceptionMessage());
        }
        short[] HoldingRegisterStatus = readHoldingRegistersResponse.getShortData();
        logger.info(Arrays.toString(HoldingRegisterStatus));
        logger.info(" HoldingRegisterOffsetStatus(03)  status is " + HoldingRegisterStatus[0]);
        modbusMaster.destroy();
        return HoldingRegisterStatus;
    }

    /**
     * 读取[04 Input Registers 3x]类型 模拟量数据
     *
     * @param slaveID slaveId
     * @param offset  位置
     * @param len     数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return 返回结果
     * @throws ModbusTransportException 异常
     * @throws ErrorResponseException   异常
     * @throws ModbusInitException      异常
     */
    public short[] readInputRegisters(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        verifyAddressAndLength(slaveID, offset, len);
        // 04 Input Registers类型数据读取
        ModbusMaster modbusMaster = getModbusMaster();
        ReadInputRegistersRequest readInputRegistersRequest = new ReadInputRegistersRequest(slaveID, offset, len);
        ReadInputRegistersResponse readInputRegistersResponse = (ReadInputRegistersResponse) modbusMaster.
                send(readInputRegistersRequest);
        if (readInputRegistersResponse.isException()) {
            logger.error("readDiscreteInputsException(04) Message is" + readInputRegistersResponse.getExceptionMessage());
        }
        short[] InputsRegisterStatus = readInputRegistersResponse.getShortData();
        logger.info(Arrays.toString(InputsRegisterStatus));
        logger.info(" readInputsRegisterStatus(04) status is " + InputsRegisterStatus[0]);
        modbusMaster.destroy();
        return InputsRegisterStatus;
    }

    /**
     * 写入单个开关量数据(write single coil 05)
     *
     * @param slaveID
     * @param offset
     * @param writeValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public Boolean writeSingleCoil(int slaveID, int offset, Boolean writeValue)
            throws ModbusTransportException, ModbusInitException {
        Boolean single = false;
        verifyAddressAndLength(slaveID, offset, 1);
        ModbusMaster modbusMaster = getModbusMaster();
        WriteCoilRequest writeCoilRequest = new WriteCoilRequest(slaveID, offset, writeValue);
        WriteCoilResponse writeCoilResponse = (WriteCoilResponse) modbusMaster.send(writeCoilRequest);
        if (writeCoilResponse.isException()) {
            logger.error("writeCoilResponseException(05) Message is" + writeCoilResponse.getExceptionMessage());
        }
        single = true;
        modbusMaster.destroy();
        return single;
    }

    /**
     * 写单个保持寄存器（write Single Register 06）
     *
     * @param slaveID
     * @param offset
     * @param registerValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public Boolean writeSingleRegister(int slaveID, int offset, int registerValue)
            throws ModbusTransportException, ModbusInitException {
        Boolean single = false;
        verifyAddressAndLength(slaveID, offset, 1);
        ModbusMaster modbusMaster = getModbusMaster();
        WriteRegisterRequest request = new WriteRegisterRequest(slaveID, offset, registerValue);
        WriteRegisterResponse response = (WriteRegisterResponse) modbusMaster.send(request);
        if (response.isException()) {
            logger.error("WriteRegisterResponseException message is(06): " + response.getExceptionMessage());
        }
        single = true;
        modbusMaster.destroy();
        return single;
    }

    /**
     * 批量写入开关量数据（write multiple coils 15)
     *
     * @param slaveID
     * @param offset
     * @param coilsValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public Boolean writeMultipleCoils(int slaveID, int offset, boolean[] coilsValue)
            throws ModbusTransportException, ModbusInitException {
        Boolean single = false;
        verifyAddressAndLength(slaveID, offset, coilsValue.length);
        ModbusMaster modbusMaster = getModbusMaster();
        WriteCoilsRequest writeCoilsRequest = new WriteCoilsRequest(slaveID, offset, coilsValue);
        WriteCoilsResponse writeCoilsResponse = (WriteCoilsResponse) modbusMaster.send(writeCoilsRequest);
        if (writeCoilsResponse.isException()) {
            logger.error("writeCoilsResponseException(15) Message is" + writeCoilsResponse.getExceptionMessage());
        }
        single = true;
        modbusMaster.destroy();
        return single;
    }

    /**
     * 批量写入寄存器数据（write multiple registers 16)
     *
     * @param slaveID
     * @param offset
     * @param registersValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public boolean writeMultipleRegisters(int slaveID, int offset, short[] registersValue)
            throws ModbusTransportException, ModbusInitException {
        boolean single = false;
        verifyAddressAndLength(slaveID, offset, registersValue.length);
        ModbusMaster modbusMaster = getModbusMaster();
        WriteRegistersRequest writeRegistersRequest = new WriteRegistersRequest(slaveID, offset, registersValue);
        WriteRegistersResponse writeRegistersResponse = (WriteRegistersResponse) modbusMaster.send(writeRegistersRequest);
        if (writeRegistersResponse.isException()) {
            logger.error("writeRegistersResponseException(16) Message is" + writeRegistersResponse.getExceptionMessage());
        }
        single = true;
        modbusMaster.destroy();
        return single;
    }

    /**
     * 检验地址和长度的是否满足
     *
     * @param slaveID
     * @param address
     * @param length
     */
    public void verifyAddressAndLength(int slaveID, int address, int length) {
        if (address < 0 || length <= 0 || slaveID < 0) {
            logger.error("slaveID | length | slaveID is illegal");
        }
        logger.info("slaveID & length & slaveID is legal");
    }

    /**
     * * 将本地cpu存储的一个float数据转成2个modbus的双字节数以应对seerRobotics的float处理方式
     * 机器人部分数据是 32 位的整数，但 Modbus 的 Holding register 或 Input register 只有16位，
     * 所以在表示整形寄存器的时候，采用两个 Holding register （Input register）来表示一个 32 位的整数。
     * 两个 Holding register 采用小端字节交换方式（Little Endian byte swap）排列。在其他需要表示 32 位整形数字的地方，
     * 也采用此方法。对于 16 位的整数，则直接使用一个寄存器表示。
     * 具体参考 https://docs.seer-robotics.com/seer_modbus/694368
     *
     * @param seerFloat 满足seer对于modbus的4字节定义范围的float数
     * @param slaveID   modbus从站地址
     * @param offset    修改modbus双寄存器的开始字节（即低字节在modbus中的开始存储地址）
     * @return 是否成功将一个float数写入两个modbus的register的地址中
     */
    public boolean writeFloatToDoubleRegister(float seerFloat, int slaveID, int offset) throws ModbusInitException, ModbusTransportException {
        byte[] seerFloatByte = TrimUtil.floatToByteArray(seerFloat);
        byte[] lowWord = Arrays.copyOfRange(seerFloatByte, 2, 4);
        byte[] hightWord = Arrays.copyOfRange(seerFloatByte, 0, 2);
        short[] register = new short[2];
        register[0] = (short) Integer.parseInt(TrimUtil.byteArrayToHexString(lowWord), 16);
        register[1] = (short) Integer.parseInt(TrimUtil.byteArrayToHexString(hightWord), 16);
        return writeMultipleRegisters(slaveID, offset, register);
    }

    /**
     * 将modbus 中的双寄存器存储数据解析成一个浮点数类型；
     *
     * @param slave
     * @param offset
     * @return
     */
    public float readDoubleRegisterBeFloat(int slave, int offset) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        short[] shorts = readInputRegisters(slave, offset, 2);
        byte[] lowBytes = TrimUtil.shortToByteArray(shorts[0]);
        byte[] hightBytes = TrimUtil.shortToByteArray(shorts[1]);
        byte[] newBytes = ArrayUtils.addAll(hightBytes, lowBytes);
        return TrimUtil.byteArrayToFloat(newBytes);
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
          /*  Modbus4jUtil modbus4jUtil = new Modbus4jUtil("localhost");
//            short[] shorts = {(short) 100, (short) 555, (short) 0, (short) 0, (short) 0};
//            modbus4jUtil.writeMultipleRegisters(1, 1, shorts);shorts
            modbus4jUtil.writeFloatToDoubleRegister(1.0f, 1, 1);
            logger.info("float:"+modbus4jUtil.readDoubleRegisterBeFloat(1, 1));*/
            Modbus4jUtil modbus4jUtil = new Modbus4jUtil("localhost", SocketPort.MODBUS_TCP_PORT);
            System.out.println(modbus4jUtil.port + modbus4jUtil.slaveIP+modbus4jUtil.getModbusMaster().getTimeout());
            modbus4jUtil.readInputStatus(1, 0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}