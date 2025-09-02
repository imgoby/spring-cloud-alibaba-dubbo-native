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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author TrevorLink
 */
@RestController
@Slf4j
public class TestController {
    @GetMapping({"/write"})
    public String write() throws Exception {
        // 创建Person的实例并设置值
        PersonProto.Person person = PersonProto.Person.newBuilder()
                .setName("John Doe")
                .setId(1234)
                .setEmail("johndoe@example.com")
                .build();


        try (OutputStream os = new FileOutputStream("persion.obj")) {
            byte[] personBytes = person.toByteArray();
            log.info("Serialized person size: " + personBytes.length);
            os.write(personBytes);
        }

        return "OK";
    }

    //增加字段后
    @GetMapping({"/write2"})
    public String write2() throws Exception {
        // 创建Person的实例并设置值
        PersonProto.Person person = PersonProto.Person.newBuilder()
                .setName("John Doe")
                .setId(1234)
                .setEmail("johndoe@example.com")
                .build();


        try (OutputStream os = new FileOutputStream("persion2.obj")) {
            byte[] personBytes = person.toByteArray();
            log.info("Serialized person size: " + personBytes.length);
            os.write(personBytes);
        }

        return "OK";
    }

    @GetMapping({"/read"})
    public String read() throws Exception {
        InputStream is = new FileInputStream("persion.obj");
        // 反序列化字节数组回Person对象
        PersonProto.Person personDecoded = PersonProto.Person.parseFrom(is);
        log.info("Decoded person: " + personDecoded);
        log.info("Name: " + personDecoded.getName());
        log.info("ID: " + personDecoded.getId());
        log.info("Email: " + personDecoded.getEmail());
        return "OK";
    }

    @GetMapping({"/read2"})
    public String read2() throws Exception {
        InputStream is = new FileInputStream("persion2.obj");
        // 反序列化字节数组回Person对象
        PersonProto.Person personDecoded = PersonProto.Person.parseFrom(is);
        log.info("Decoded person: " + personDecoded);
        log.info("Name: " + personDecoded.getName());
        log.info("ID: " + personDecoded.getId());
        log.info("Email: " + personDecoded.getEmail());
        return "OK";
    }
}
