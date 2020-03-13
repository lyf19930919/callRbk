1.该项目基于seer robotics的单机控制协议（tcp/ip或者modbus tcp）实现的地盘控制；seer地盘控制参考如下链接：
  https://docs.seer-robotics.com/robokit_netprotocol/663328
    
    1.1.实现基本的导航控制，状态查询，导航任务下发，IO状态查询，批零控制pdo状态，以及开环控制等；

2.该项目的实物基础是seer的地盘+UR复合机器人+智能相机+夹爪的网络拓扑控制模式；

3.项目实现了UR的远程启动，线程定时获取UR状态信息（包括运动学和开机时间的向详细信息）；

4.项目的页面显示是通过Swing的方式编写，可通过第三方打包软件成exe执行；

5.项目在存储数据上还可以进行相关优化 ，例如可以使用对象链接池的方式获取；

6.项目实现了modbus tcp 以及tcp/ip的client方式进行数据的收发；可通过modbus tcp与第三方设备如PLC ,手臂等控制端设备进行通讯；
