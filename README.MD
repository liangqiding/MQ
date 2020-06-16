### 01 activeMQ
### 02 kafka
### 03 Springcloud Stream整合kafka

# 1 Springboot整合 activeMQ
## 1.1 起步依赖
```
<!--		activeMQ-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>
        <dependency>
            <groupId>org.messaginghub</groupId>
            <artifactId>pooled-jms</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.49</version>
        </dependency>
```
## 1.2 编写生产者
```java
 
@Component
public class TopicProduce {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;
    /**
     * @return
     * @Author QiDing
     * @Description //TODO 开启多线程测试发送速度
     * @Date 11:06 2020/6/16 0016
     * @Param
     **/
//    @Scheduled(fixedDelay = 3000)
//    @Async
    public void produce_topic(String msg) {
        jmsMessagingTemplate.convertAndSend(topic, msg);
    }	
	 /**
     * @return
     * @Author QiDing
     * @Description //TODO 开启多线程测试发送速度
     * @Date 11:05 2020/6/16 0016
     * @Param
     **/
    @Async
    public void produce_queue_test(String msg) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue("queueName");
    }
	
	 @Bean
    public Topic topic() {
        return new ActiveMQTopic("activemq_test");
    }
	
}
```
## 1.3 编写消费者
```java
@Component
public class Topic_Consumer {

    @JmsListener(destination = "activemq_test")
    public void receive_queue(TextMessage textMessage) throws Exception {
        System.out.println("消费者受到订阅的主题消息：" + textMessage.getText());
    }

    @JmsListener(destination = "queueName")
    public void queueName(String textMessage) throws Exception {
        System.out.println("消费者受到队列的主题消息：" + textMessage);
    }
}

```
## 1.4 修改activeMQ 配置 添加nio模式
```xml
<transportConnectors>
            <!-- DOS protection, limit concurrent connections to 1000 and frame size to 100MB -->
            <!--  添加nio模式 -->
			 <transportConnector name="nio" uri="nio://0.0.0.0:61618?trace=true"/>
			 <!--  添加nio模式 -->
            <transportConnector name="openwire" uri="tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600?jms.useAsyncSend=true"/>
            <transportConnector name="amqp" uri="amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            <transportConnector name="stomp" uri="stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            <transportConnector name="mqtt" uri="mqtt://0.0.0.0:1884?create=false"/> 
            <transportConnector name="ws" uri="ws://0.0.0.0:61614?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
        </transportConnectors>
```
## 1.5 配置application.properties
```
server.port=9006
#activeMQ
spring.activemq.broker-url=nio://localhost:61618
spring.activemq.password=admin
spring.activemq.user=admin
spring.activemq.in-memory=true
#默认队列的形式
spring.jms.pub-sub-domain=false
#true表示使用连接池
spring.activemq.pool.enabled=true
#连接池最大连接数
spring.activemq.pool.max-connections=5
#空闲的连接过期时间，默认为30秒
spring.activemq.pool.idle-timeout=30000
#强制的连接过期时间，与idleTimeout的区别在于：idleTimeout是在连接空闲一段时间失效，而expiryTimeout不管当前连接的情况，只要达到指定时间就失效。默认为0，never
#spring.activemq.pool.expiry-timeout=0

activeMQ.TOPIC=queue

myQueue= queueName

```

## 1.6编写测试类
```java
@SpringBootTest
class ProviderApplicationTests {
@Autowired
    TopicProduce topicProduce;
    @Test
    void contextLoads() {
        for (int i = 1; i <= 10000; i++) {
            topicProduce.produce_topic("消息速度测试>>"+i);
        }
    }
}
```
## 启动程序并测试发送及接收速度