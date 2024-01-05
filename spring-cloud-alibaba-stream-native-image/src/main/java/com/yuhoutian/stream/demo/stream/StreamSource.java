package com.yuhoutian.stream.demo.stream;

import com.yuhoutian.stream.demo.constants.StreamTopicEvent;
import com.yuhoutian.stream.demo.constants.StreamTopicTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class StreamSource {
    @Autowired
    private StreamBridge streamBridge;

    private SendImpl eventSimple;


    public StreamSource(StreamBridge streamBridge) {
        eventSimple = new SendImpl(StreamTopicEvent.EVENT_SIMPLE + StreamTopicTitle.OUTPUT, streamBridge);
    }

    //@Output(StreamTopicEvent.EVENT_SIMPLE + StreamTopicTitle.OUTPUT)
    public SendImpl eventSimple() {
        return eventSimple;
    }


}
