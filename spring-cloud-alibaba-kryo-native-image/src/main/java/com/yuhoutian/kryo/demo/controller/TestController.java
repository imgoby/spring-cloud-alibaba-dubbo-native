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

package com.yuhoutian.kryo.demo.controller;

import com.yuhoutian.kryo.demo.model.KryoModel;
import com.yuhoutian.kryo.demo.util.KryoUtil;
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
        KryoModel kryoModel = new KryoModel(); // 假设MyObject是你的一个类实例
        kryoModel.setId(System.currentTimeMillis());
        kryoModel.setName("candy");
        kryoModel.setData("candy".getBytes());
        byte[] serialized = KryoUtil.serialize(kryoModel); // 序列化对象

        try (OutputStream os = new FileOutputStream("/tmp/test.obj")) {
            log.info("Serialized person size: " + serialized.length);
            os.write(serialized);
        }

        return "OK";
    }

    @GetMapping({"/read"})
    public String read() throws Exception {
        InputStream is = new FileInputStream("/tmp/test.obj");
        byte[] bs = is.readAllBytes();

        KryoModel kryoModel = KryoUtil.deserialize(bs, KryoModel.class); // 反序列化对象

        // 反序列化字节数组回Person对象
        log.info("Name: " + kryoModel.getName());
        log.info("ID: " + kryoModel.getId());
        log.info("Data: " + kryoModel.getData());
        return "OK";
    }

}
