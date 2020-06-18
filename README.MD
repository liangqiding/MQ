### [01 activeMQ](#Springboot整合activeMQ)
### [02 kafka](#Springboot整合activeMQ)
### [03 Springcloud Stream整合kafka](#Springboot整合activeMQ)

# 1 Springboot整合activeMQ

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
## 1.6 开启线程池
```java
package kafka.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: 梁其定
 * @DateTime: 2020/4/10 0010 14:13
 * @Description: TODO 启动线程池执行
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("线程池执行-");
        /*
        todo 设置拒绝策略
         当抛出RejectedExecutionException异常时，会调rejectedExecution方法 调用者运行策略实现了一种调节机制，
         该策略既不会抛弃任务也不会爆出异常，而是将任务退回给调用者，从而降低新任务的流量
        */

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}

```


## 1.7编写测试类
```java
@SpringBootTest
class ProviderApplicationTests {
@Autowired
    TopicProduce topicProduce;
    @Test
    void contextLoads() {
        for (int i = 1; i <= 1000; i++) {
            topicProduce.produce_topic("消息速度测试>>"+i);
        }
    }
}
```
## 启动程序并测试发送及接收速度
实测idea下运行，发送速度1000条在瞬间发完，但消费速度偏慢，可能是程序优化问题。

# 2 Springboot整合kafka

## 2.1 起步依赖
```
 <!-- kafka -->
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-streams</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
```
## 2.1 编写生产者
```java
package kafka.producer.KafkaProducer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: QiDing
 * @DateTime: 2020/6/16 0016 14:08
 * @Description: TODO
 */

@Component
@AllArgsConstructor
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic,Integer count) {
        for (int i = 1; i <= count; i++) {
            data(topic);
        }
        logger.info("send success");
    }

    @Async
    public void data(String topic) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("data",1);
        jsonObject1.put("test","测试数据");
        jsonArray.add(jsonObject1);
        jsonObject.put("json",jsonArray);
        try {
            kafkaTemplate.send(topic,  jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("出错！！！！！！！！！！！");
        }
    }

}
```
## 2.2 编写消费者
```java
package kafka.consumer.KafkaConsumer;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: QiDing
 * @DateTime: 2020/6/16 0016 14:15
 * @Description: TODO
 */
@Component
@AllArgsConstructor
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    static int i = 1;

    public synchronized void setTime() {
        System.out.println(i++);
    }
    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group}")
    // @Async
    public void data(ConsumerRecord consumerRecord) {
        Object value = consumerRecord.value();
        if (logger.isInfoEnabled()) {
            logger.info("offset {}, value {}", consumerRecord.offset(), consumerRecord.value());
        }
        if (null == value) {
            logger.error("kafka消费数据为空");
        }
        logger.info((String) value);
    }



    @KafkaListener(topics = "testTopic", groupId = "${kafka.group}")
    // @Async
    public void kafkaConsumer(ConsumerRecord consumerRecord) {
//        System.out.println(i++);
        setTime();
        Object value = consumerRecord.value();
        if (logger.isInfoEnabled()) {
            logger.info("offset {}, value {}", consumerRecord.offset(), consumerRecord.value());
        }
        if (null == value) {
            logger.error("kafka消费数据为空");
        }
        logger.info((String) value);
    }

    @KafkaListener(topics = "MessageLv1", groupId = "${kafka.group}")
    // @Async
    public void kafkaConsumer_test(ConsumerRecord consumerRecord) {
//        System.out.println(i++);
        setTime();
        Object value = consumerRecord.value();
        if (logger.isInfoEnabled()) {
            logger.info("offset {}, value {}", consumerRecord.offset(), consumerRecord.value());
        }
        if (null == value) {
            logger.error("kafka消费数据为空");
        }
        logger.info((String) value);
    }

    @KafkaListener(topics = "MessageLv2", groupId = "${kafka.group}")
    // @Async
    public void kafkaConsumer_lv2(ConsumerRecord consumerRecord) {
//        System.out.println(i++);
        setTime();
        Object value = consumerRecord.value();
        if (logger.isInfoEnabled()) {
            logger.info("offset {}, value {}", consumerRecord.offset(), consumerRecord.value());
        }
        if (null == value) {
            logger.error("kafka消费数据为空");
        }
        logger.info((String) value);
    }

}

```
## 2.3编写配置文件
```
server:
  port: 9013
  servlet:
    context-path: /


spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      # 每次批量发送消息的数量
      batch-size: 16
      # 缓存容量
      buffer-memory: 33554432
      #设置大于0的值，则客户端会将发送失败的记录重新发送
      retries: 0
      # 指定消息key和消息体的编解码方式 UTF-8
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer

kafka:
  #订阅的主题
  topic: topic-test-01
  #主题消费分组
  group: group-test-01
```

## 2.4 编写测试接口
```java
package kafka.producer.controller;

import kafka.producer.KafkaProducer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: QiDing
 * @DateTime: 2020/6/16 0016 14:09
 * @Description: TODO
 */
@RestController
public class Index {
    @Autowired
    KafkaProducer kafkaProducer;

    @RequestMapping("/send")
    public String test(String topic,Integer count) {
        kafkaProducer.send(topic,count);
        return "success";
    }
}

```

## 2.5测试收发速度 kafka 版本：kafka_2.12-2.5.0
1. 浏览器或postman 输入：http://localhost:9012/send?topic=testTopic&&count=10000  
2. 参数说明：topic为消息topic，count为发送条数

## 2.6 开启线程池消费
1. 配置线程池
```java
/**
 * @Author: 梁其定
 * @DateTime: 2020/4/10 0010 14:13
 * @Description: TODO 启动线程池执行
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("线程池执行-");
        /*
        todo 设置拒绝策略
         当抛出RejectedExecutionException异常时，会调rejectedExecution方法 调用者运行策略实现了一种调节机制，
         该策略既不会抛弃任务也不会爆出异常，而是将任务退回给调用者，从而降低新任务的流量
        */

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
```
2. 配置kafka
```java
/**
 * @Author: QiDing
 * @DateTime: 2020/6/17 0017 9:25
 * @Description: TODO
 */
public class KafkaConfig {
    @Autowired
    Map<String, Object> consumerConfigs;
    @Bean
    KafkaListenerContainerFactory<?> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new
                ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs));
        factory.setBatchListener(true); // 开启批量监听
        return factory;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "aaa");
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset); //默认offset位置（当不存在offset时从哪里开始读取）
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100); //设置每次接收Message的数量 :最大poll数据时间间隔
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }
}

```
3. 消费者加上 @Async注解开启多线程
```java
 @KafkaListener(topics = "MessageLv1", groupId = "${kafka.group}")
     @Async
    public void kafkaConsumer_test(ConsumerRecord consumerRecord) {
//        System.out.println(i++);
        setTime();
        Object value = consumerRecord.value();
        if (logger.isInfoEnabled()) {
            logger.info("offset {}, value {}", consumerRecord.offset(), consumerRecord.value());
        }
        if (null == value) {
            logger.error("kafka消费数据为空");
        }
        logger.info((String) value);
    }
```
## 2.7 开启多线程后再次测试收发速度 kafka 版本：kafka_2.12-2.5.0
1. 浏览器或postman 输入：http://localhost:9012/send?topic=testTopic&&count=10000  
2. 参数说明：topic为消息topic，count为发送条数
3. 实测速度明显提升，消费速度快约一倍

## 2.8 kafka 开启分区存取
1. 再次测试，速度明显再次提升

# 3 kafka对比activemq 
Kafka的性能（吞吐量、tps）比activeMq要强，如果用来做大数据量的快速处理很有有优势的。