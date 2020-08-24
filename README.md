1.该项目基于seer robotics的单机控制协议（tcp/ip或者modbus tcp）实现的底盘控制；seer底盘控制参考如下链接：
https://docs.seer-group.com/
    
    1.1.实现基本的导航控制，状态查询，导航任务下发，IO状态查询，批零控制pdo状态，以及开环控制等；

2.该项目的实物基础是seer的地盘+UR复合机器人+智能相机+夹爪的网络拓扑控制模式；

3.项目实现了UR的远程启动，线程定时获取UR状态信息（包括运动学和开机时间的向详细信息）；
  ur的数据报文解析参考UR官方支持文档

4.项目的页面显示是通过Swing的方式编写，可通过第三方打包软件成exe执行；

5.该项目采用log4j日志处理和maven依赖的方式做本地jar的依赖，方便jar管理和引用；

6.项目在存储数据上还可以进行相关优化 ，例如可以使用对象链接池的方式获取；

7.项目实现了modbus tcp 以及tcp/ip的client方式进行数据的收发；可通过modbus tcp与第三方设备如PLC ,手臂等控制端设备进行通讯；

8.可通过该套代码实现与Seer Robotics的标准Modbus Tcp/Tcp IP协议通讯（mosbus tcp实现了seerRobotics定义的float数据的读写）

9.添加了库位管理的矩阵操作流程；