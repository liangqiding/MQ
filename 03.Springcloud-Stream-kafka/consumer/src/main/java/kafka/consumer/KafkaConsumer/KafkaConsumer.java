package kafka.consumer.KafkaConsumer;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
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
    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group}")
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

}
