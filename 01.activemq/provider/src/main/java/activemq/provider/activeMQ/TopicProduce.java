package activemq.provider.activeMQ;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;


@Component
public class TopicProduce {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;
    @Autowired
    private Queue productActiveMQQueue;

    //    @Scheduled(fixedDelay = 3000)
    public void produce_queue(String msg) {

        jmsMessagingTemplate.convertAndSend(
                productActiveMQQueue, msg);
    }

    /**
     * @return
     * @Author QiDing
     * @Description //TODO 开启多线程测试队列发送速度
     * @Date 11:05 2020/6/16 0016
     * @Param
     **/
    @Async
    public void produce_queue_test(String msg) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue("queueName");
    }

    final int J = 5000;

    /**
     * @return
     * @Author QiDing
     * @Description //TODO 开启多线程测试topic发送速度
     * @Date 11:06 2020/6/16 0016
     * @Param
     **/
//    @Scheduled(fixedDelay = 3000)
//    @Async
    public void produce_topic(String msg) {
        jmsMessagingTemplate.convertAndSend(topic, msg);
    }

}
