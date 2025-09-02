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

package com.yuhoutian.protobuf.demo.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yuhoutian.protobuf.demo.model.PersonProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TrevorLink
 */
@RestController
@Slf4j
public class TestController {
    @GetMapping({"/test"})
    public String test() throws InvalidProtocolBufferException {
        // 创建Person的实例并设置值
        PersonProto.Person person = PersonProto.Person.newBuilder()
                .setName("John Doe")
                .setId(1234)
                .setEmail("johndoe@example.com")
                .build();

        // 序列化到字节数组
        byte[] personBytes = person.toByteArray();
        System.out.println("Serialized person size: " + personBytes.length);

        // 反序列化字节数组回Person对象
        PersonProto.Person personDecoded = PersonProto.Person.parseFrom(personBytes);
        System.out.println("Decoded person: " + personDecoded);
        System.out.println("Name: " + personDecoded.getName());
        System.out.println("ID: " + personDecoded.getId());
        System.out.println("Email: " + personDecoded.getEmail());
        return "OK";
    }
}
