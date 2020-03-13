package util;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * modbus4j工具类，提供modbus对于数据的读写，包括读取线圈（可读写）的状态（read coils 01），读取开关量的状态（read Discrete inputs 02）
 * 读取保持寄存器（可读写）状态（read Holding Registers 03），读取输入寄存器状态（read Input Resister 04）；以及写入单个开关量数据(write single coil 05)
 * 批量写入开关量数据（write multiple coils 15)，写单个保持寄存器（write Single Register 06），批量写入保持寄存器（write Multiple Holding Register 16）
 */
public class Modbus4jUtil {
    private Logger logger = LoggerFactory.getLogger(Modbus4jUtil.class);
    /**
     * 工厂
     */
    private String SlaveIP;
    static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    public Modbus4jUtil(String slaveIP) {
        SlaveIP = slaveIP;
    }

    /**
     * 获取modbus tcp方式的本地matster对象 ,端口采用modbus的标砖通讯端口502
     * modbusFactory.createRtuMaster(wapper); //RTU 协议
     * modbusFactory.createUdpMaster(params);//UDP 协议
     * modbusFactory.createAsciiMaster(wrapper);//ASCII 协议
     *
     * @return
     * @throws ModbusInitException
     */
    private ModbusMaster getModbusTCPMaster() throws ModbusInitException {
        IpParameters params = new IpParameters();
        params.setHost(this.SlaveIP);
        params.setPort(SocketPort.MODBUS_TCP_PORT);
        ModbusMaster master = modbusFactory.createTcpMaster(params, false);
//        设置超时连接的时间
        master.setTimeout(500);
//        设置重复链接的次数
        master.setRetries(2);
        master.init();
        return master;
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
    private boolean[] readCoilStatus(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        verificatAddressAndLength(slaveID, offset, len);
        // 01 Coil Status
        ModbusMaster modbusMaster = getModbusTCPMaster();
        ReadCoilsRequest readCoilsRequest = new ReadCoilsRequest(slaveID, offset, len);
        ReadCoilsResponse readCoilsResponse = (ReadCoilsResponse) modbusMaster.send(readCoilsRequest);
        if (readCoilsResponse.isException()) {
            logger.error("readCoilResponseException(01) message is" + readCoilsResponse.getExceptionMessage());
        }
        logger.info(Arrays.toString(readCoilsResponse.getBooleanData()));
        boolean[] coilStatus = readCoilsResponse.getBooleanData();
        logger.info("offsetCoil(01) status is" + coilStatus[0]);
        modbusMaster.destroy();
        return coilStatus;
    }

    /**
     * 批量读取[02 Input Status 1x]类型 开关量数据
     *
     * @param slaveID
     * @param offset
     * @param len
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    private boolean[] readInputStatus(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        verificatAddressAndLength(slaveID, offset, len);
        // 02 Input Status
        ModbusMaster modbusMaster = getModbusTCPMaster();
        ReadDiscreteInputsRequest readDiscreteInputsRequest = new ReadDiscreteInputsRequest(slaveID, offset, len);
        ReadDiscreteInputsResponse readDiscreteInputsResponse = (ReadDiscreteInputsResponse) modbusMaster
                .send(readDiscreteInputsRequest);
        if (readDiscreteInputsResponse.isException()) {
            logger.info("readDiscreteInputsException(02) Message is" + readDiscreteInputsResponse.getExceptionMessage());
        }
        boolean[] inputStatus = readDiscreteInputsResponse.getBooleanData();
        logger.info(Arrays.toString(inputStatus));
        logger.info("discreteInputOffset status(02) is " + inputStatus[offset]);
        modbusMaster.destroy();
        return inputStatus;
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
    private short[] readHoldingRegister(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        verificatAddressAndLength(slaveID, offset, len);
        // 03 Holding Register类型数据读取
        ModbusMaster modbusMaster = getModbusTCPMaster();
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
    private short[] readInputRegisters(int slaveID, int offset, int len)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        verificatAddressAndLength(slaveID, offset, len);
        // 04 Input Registers类型数据读取
        ModbusMaster modbusMaster = getModbusTCPMaster();
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
    private Boolean writeSingleCoil(int slaveID, int offset, Boolean writeValue)
            throws ModbusTransportException, ModbusInitException {
        Boolean single = false;
        verificatAddressAndLength(slaveID, offset, 1);
        ModbusMaster modbusMaster = getModbusTCPMaster();
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
    private Boolean writeSingleRegister(int slaveID, int offset, int registerValue)
            throws ModbusTransportException, ModbusInitException {
        Boolean single = false;
        verificatAddressAndLength(slaveID, offset, 1);
        ModbusMaster modbusMaster = getModbusTCPMaster();
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
    private Boolean writeMultipleCoils(int slaveID, int offset, boolean[] coilsValue)
            throws ModbusTransportException, ModbusInitException {
        Boolean single = false;
        verificatAddressAndLength(slaveID, offset, coilsValue.length);
        ModbusMaster modbusMaster = getModbusTCPMaster();
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
     * 批量写入开关量数据（write multiple registers 16)
     *
     * @param slaveID
     * @param offset
     * @param registersValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    private boolean writeMultipleRegisters(int slaveID, int offset, short[] registersValue)
            throws ModbusTransportException, ModbusInitException {
        boolean single = false;
        verificatAddressAndLength(slaveID, offset, registersValue.length);
        ModbusMaster modbusMaster = getModbusTCPMaster();
        WriteRegistersRequest writeRegistersRequest = new WriteRegistersRequest(slaveID, offset, registersValue);
        WriteRegistersResponse writeRegistersResponse = (WriteRegistersResponse) modbusMaster.send(writeRegistersRequest);
        if (writeRegistersResponse.isException()) {
            logger.error("writeRegistersResponseException(16) Message is" + writeRegistersResponse.getExceptionMessage());
        }
        single = true;
        modbusMaster.destroy();
        return single;
    }

    private void verificatAddressAndLength(int slaveID, int address, int length) {
        if (address < 0 || length <= 0 || slaveID < 0) {
            logger.error("address | length | slaveID is illegal");
        }
        logger.info("address & length & slaveID is legal");
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Modbus4jUtil modbus4jUtil = new Modbus4jUtil("localhost");
            short[] shorts = {(short) 100, (short) 0, (short) 0, (short) 0, (short) 0};
            modbus4jUtil.readHoldingRegister(1,140,5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
