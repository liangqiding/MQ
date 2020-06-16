package activemq.consumer.activeMQ;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

/**
 * @Author: 梁其定
 * @DateTime: 2020/5/7 0007 14:29
 * @Description: TODO
 */
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
