package com.yuhoutian.stream.demo.stream;


import com.yuhoutian.stream.demo.constants.EventType;
import com.yuhoutian.stream.demo.constants.StreamTopicEvent;
import com.yuhoutian.stream.demo.constants.StreamTopicHeader;
import com.yuhoutian.stream.demo.constants.StreamTopicTitle;
import com.yuhoutian.stream.demo.model.EventSimpleModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.function.Consumer;

@Configuration
//(1)按topic来命名
//(2)再按header来转发
public class StreamSink {


    @Bean(name = StreamTopicEvent.EVENT_SIMPLE + StreamTopicTitle.INPUT)
    public Consumer<Message<EventSimpleModel>> eventSimple() {
        return msg -> {
            //{event_type=ORDER_EVENT, ROCKET_MQ_FLAG=0, ROCKET_MQ_BORN_TIMESTAMP=1702372590229, ROCKET_MQ_MESSAGE_ID=7F0000011F1C63947C6B3C5AE6950004, source-type=kafka, ROCKET_MQ_TOPIC=PRAISE-TOPIC-01, ROCKET_MQ_BORN_HOST=192.168.0.110, id=5a097f92-8a2d-603d-74ea-212121543707, ROCKET_MQ_SYS_FLAG=0, contentType=application/json, ROCKET_MQ_QUEUE_ID=13, timestamp=1702374891367}
            MessageHeaders messageHeaders = msg.getHeaders();
            String topic = (String) messageHeaders.get("ROCKET_MQ_TOPIC");
            String eventType = (String) messageHeaders.get(StreamTopicHeader.EVENT_TYPE);
            assert (topic.equals(StreamTopicEvent.EVENT_SIMPLE));
            if (StringUtils.isNotEmpty(eventType)) {
                EventSimpleModel eventSimpleModel = msg.getPayload();
                if (eventType.equals(EventType.HELLO)) {
                    System.out.println("---------------get msg:" + eventSimpleModel);
                } else {
                    System.out.println("unsupport header:" + messageHeaders + ",msg:" + msg);
                }
            } else {
                System.out.println("unsupport header:" + messageHeaders);
            }
        };
    }
}
