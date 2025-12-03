package priv.ana.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "exam_test_topic", consumerGroup = "exam-consumer-group")
public class TestListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println("Received message: " + s);
    }
}
