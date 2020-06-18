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
    public String test(String lv,Integer count) {
        kafkaProducer.send(lv,count);
        return "success";
    }
}
