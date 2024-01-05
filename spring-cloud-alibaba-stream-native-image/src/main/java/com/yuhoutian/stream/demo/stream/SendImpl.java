package com.yuhoutian.stream.demo.stream;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

public class SendImpl {
    private String topic;
    private StreamBridge streamBridge;

    protected SendImpl(String topic, StreamBridge streamBridge) {
        this.topic = topic;
        this.streamBridge = streamBridge;
    }

    public boolean send(Message msg) {
        return streamBridge.send(topic, msg);
    }
}
