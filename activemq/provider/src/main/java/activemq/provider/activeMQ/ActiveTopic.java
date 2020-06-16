package activemq.provider.activeMQ;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @Author: 梁其定
 * @DateTime: 2020/5/7 0007 14:10
 * @Description: TODO
 */
@Component
public class ActiveTopic {

    @Value("${activeMQ.TOPIC}")
    private String  topicName ;


    private String  queueName ="mqtt.queue";

    @Bean
    public Topic topic() {
        return new ActiveMQTopic("activemq_test");
    }


    @Bean
    public Queue productActiveMQQueue(){
        return new ActiveMQQueue(topicName);
    }

}
