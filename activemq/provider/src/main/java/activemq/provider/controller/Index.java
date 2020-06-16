package activemq.provider.controller;


import activemq.provider.activeMQ.TopicProduce;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: QiDing
 * @DateTime: 2020/6/9 0009 13:52
 * @Description: TODO
 */
@Controller
public class Index {
    @Autowired
    TopicProduce topicProduce;
    static int i = 0;
    /**
     * @Author QiDing
     * @Description //TODO topic 模式
     * @Date 15:08 2020/6/9 0009
     * @Param
     * @return
     **/



    @RequestMapping("/send")
    @ResponseBody
    public String test(String msg) {
        topicProduce.produce_topic(msg);
        return "success:" + i++;
    }

    @RequestMapping("/send/queue")
    @ResponseBody
    public String queue(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("num", i);
        jsonObject.put("test", "test");
        topicProduce.produce_queue(jsonObject.toJSONString());
        return "success:" + i++;
    }

    @RequestMapping("/")
    public String test() {

        return "index";
    }

    static int s = 1;

    @RequestMapping("/test")
    @ResponseBody
    public void A() {


        topicProduce.produce_queue_test("消息:" + s++);

//        topic_produce.produce_queue_test("123456789");

    }
}
