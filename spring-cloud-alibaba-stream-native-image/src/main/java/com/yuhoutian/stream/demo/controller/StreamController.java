/*
 * Copyright 2013-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuhoutian.stream.demo.controller;

import com.yuhoutian.stream.demo.constants.EventType;
import com.yuhoutian.stream.demo.constants.StreamTopicEvent;
import com.yuhoutian.stream.demo.model.EventSimpleModel;
import com.yuhoutian.stream.demo.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TrevorLink
 */
@RestController

public class StreamController {
    private static final String BINDING_NAME = "praise-output";
    @Autowired
    private StreamSource streamSource;

    @GetMapping({"/stream/send"})
    public boolean send() {
        EventSimpleModel streamModel = new EventSimpleModel();
        streamModel.setIntId(1);

        Message<EventSimpleModel> message = MessageBuilder.withPayload(streamModel).setHeader(StreamTopicEvent.EVENT_SIMPLE, EventType.HELLO).build();
        return streamSource.eventSimple().send(message);
    }

}
