### 项目参考说明

#### 项目技术选型
* 项目主要以spring cloud alibaba解决方案为主。
* 本架构仅仅为基础架构的一部分。 很多地方还有待改进。
  * 比如： 微服务内部通讯。  个人觉得采用rpc框架可能要好些（处于性能考虑， 当然现在使用rest也有优点）。 比如 dubbo， grpc.
  * 微服务对限流。 进行友好返回没有做。 后面应该要加上去。 
  * alibaba那个sentinel-dashboard配置。 一样难尽啊。  如果有条件。  可以自己写sentinel的配置。
  * 我对sentinel-dashboard对源码进行过改造。 让他适配 nacos注册中心。  但是还是有很多问题。  这里的该改造代码。 网上有很多参考（官方也有文档）。  有条件的话。 最好还是自己写吧。 

#### 项目的基础组件和中间件介绍
* 注册中心    Nacos
* 配置中心    Nacos
* 服务保护    sentinel
* 网关       spring cloud gateway(SCG)
* 微服务调用  spring-cloud-openfeign
* 服务调用负载均衡   spring-cloud-loadbalancer
* 服务监控    spring-boot-admin
* 链路追踪    skywalking
* 安全认证    spring-cloud-oauth2

#### 一些中间件地址
`这些地址都为云服务地址。 服务器也不是很好。 大家玩玩没问题。  请各个大哥不要攻击。  搭建服务还是很难的。 `

`这些服务可能随时会关， 大家尽量不要依赖这些服务。 如果因为服务挂掉对你造成影响。 非常抱歉。`
* nacos （集群模式）
  * http://162.14.116.134:8848/nacos  （nacos/nacos）
  
* redis (集群模式)
  - 101.43.40.2:6379
  - 101.43.40.2:26379
  - 101.43.52.190:6379
  - 101.43.52.190:26379
  - 162.14.116.134:6379
  - 162.14.116.134:26379 
  密码： password: In&2LnzeSV
  
* mysql （单机， 主要是没这么多资源）
  * 119.3.143.23:3306/test1
  * test1
  * test1
  
* skywalking (单机， 之所以没有做集群。 主要是服务器资源不够。 这哥儿对机器要求太高了。 哎)
  * http://8.143.4.60:8080/
  
* es(集群模式)
  * 101.43.40.2:9200, 101.43.52.190:9200

* kibana （这大哥对服务器资源要求太高了。 我没有启动。 我一般都是本地启动的。 ）
  * 如果你想使用。 可以在本地启动kibana. 将本地文件替换掉。  文件参考（./readme_dir/kibana.yml）

* zookeeper (集群模式)
  * 119.3.143.23:2181, 101.43.52.190:2181, 162.14.116.134:2181

* RocketMQ (集群)
  * name-server: 101.43.52.190:9876;162.14.116.134:9876
  * broker: dashboard中cluster
  * dashboard： http://101.43.52.190:8081/#/

#### 借鉴的项目
* 在gateway和oauth进行整合时。 主要是借鉴大哥的文章。  不过这个大哥确实挺厉害的。 
* https://github.com/chenjiabing666/JavaFamily/blob/master/README.md

#### 版本介绍

* https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E

  | Spring Cloud Alibaba Version | Spring Cloud Version  | Spring Boot Version |
  | ---------------------------- | --------------------- | ------------------- |
  | 2021.1                       | Spring Cloud 2020.0.1 | 2.4.2               |

#### 一些注意事项
* 在common-util 使用了雪花id。 这个雪花id有两个参数。 需要通过环境变量或者启动参数指定。 不然会导致， 分布式id存在重复的问题。 当然你也可以使用你们系统的分布式id生成规则。 
```java
        final String workerIdProperty = System.getProperty("common.snowflake.workerId");
        final String datacenterIdProperty = System.getProperty("common.snowflake.datacenterId");
```

* 项目使用的链路跟踪为 skywalking。  因此你需要下载skywalking的agent包。 具体版本要根据你使用的skywalking 下载。 下面链接仅做参考。
  * https://www.apache.org/dyn/closer.cgi/skywalking/java-agent/8.9.0/apache-skywalking-java-agent-8.9.0.tgz

* skywalking 默认不支持gateway. 要支持需要将一些jar包复制到plugins目录。 具体做法。 可以参考网上资料。 

* skywalking 的参考配置。 
```java
启动参数：  -javaagent:H:/skywalking/skywalking-agent/skywalking-agent.jar
        
环境变量： SW_AGENT_COLLECTOR_BACKEND_SERVICES=8.143.4.60:11800;SW_AGENT_NAME=provider
```