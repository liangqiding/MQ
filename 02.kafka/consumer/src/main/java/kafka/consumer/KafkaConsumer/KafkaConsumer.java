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
    @Async
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
    @Async
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

    @KafkaListener(topics = "MessageLv2", groupId = "${kafka.group}")
    @Async
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
