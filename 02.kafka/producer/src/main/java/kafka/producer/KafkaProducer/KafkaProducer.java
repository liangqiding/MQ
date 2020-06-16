package kafka.producer.KafkaProducer;

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

    public void send(String data) {
        for (int i = 1; i < 10000; i++) {
            data(data + i);
        }
    }

    @Async
    public void data(String data) {
        try {
            kafkaTemplate.send("topic-test-01", "这是测试的数据==>" + data);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("出错！！！！！！！！！！！");
        }
    }

}
