## 简介
  微服务近几年已经发展的炉火纯青了，那么在微服务中RPC协议起到了至关重要的作用。我们常用的Dubbo就是基于RPC协议的远程服务调用框架。那么Dubbo已经很成熟了，本项目只是作为研究RPC的一个小小的demo。
## 结构
  * consumer - 消费者程序
  * provider - 服务提供者程序
  * rpc-api - 所有远程服务接口的api，应该同时被consumer和provider依赖
  * core - rpc框架的核心代码
## 具体实现
  ![结构图](https://g-blog.oss-cn-beijing.aliyuncs.com/image/rpc01.png)
* zookeeper - 注册中心，记录服务提供者的地址、名称等信息。
* provider - 服务提供者
* consumer - 消费者
* rpc server - 会随服务提供者一起启动，顺带会启动一个netty服务器接受远程客户端的调用信息
* rpc client - 发生远程调用时，rpc server创建连接并进行通信，实现远程调用的效果
### Provider的启动
  1. provider启动时，会随之启动rpc server
  2. 扫描provider中所有有`@RpcService`注解的类并交由`BeanFactory`管理
  3. 启动一个netty server，最后向注册中心注册提供者信息。
### Consumer的启动
  1. consumer启动时，rpc框架会自动扫描consumer中所有有`@RpcConsumer`注解的属性
  2. 并且自动注入其远端的代理
### 调用过程
  1. 发生远程调用操作时，代理会通过netty客户端向对应netty服务器发送调用信息，包含类，方法名，参数，参数类型信息
  2. provider所启动的netty服务器收到调用信息，通过反射调用具体的方法，并返回结果给netty客户端
  3. netty客户端收到返回结果，并返回给consumer，完成整个调用过程。