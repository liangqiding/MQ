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

    public void send(String data,Integer count) {
        for (int i = 1; i <= count; i++) {
            data(data);
        }
    }

    @Async
    public void data(String data) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("data",1);
        jsonObject1.put("function_tag","SD103Emg");
        jsonArray.add(jsonObject1);
        jsonObject.put("payload",jsonArray);
        jsonObject.put("topic","i2dsp/Emg/0/SD103/MC0000000001LTK000000041002/thing/event/SD103Emg/post");
        try {
            kafkaTemplate.send("MessageLv"+data,  jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("出错！！！！！！！！！！！");
        }
    }

}
