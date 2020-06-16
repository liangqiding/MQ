package activemq.provider;

import activemq.provider.activeMQ.TopicProduce;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
