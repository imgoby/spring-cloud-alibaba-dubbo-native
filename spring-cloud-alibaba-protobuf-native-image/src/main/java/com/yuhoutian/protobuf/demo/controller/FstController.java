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

import com.yuhoutian.fst.demo.model.FstModel;
import lombok.extern.slf4j.Slf4j;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TrevorLink
 */
@RestController
@Slf4j
public class FstController {
    @GetMapping({"/fst"})
    public String fst() {
        log.info("-----fst begin----");
        FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
        log.info("-----fst begin----");
        FstModel fstModel=new FstModel();
        fstModel.setId(System.currentTimeMillis());
        fstModel.setName("candy");
        fstModel.setData("candy".getBytes());
        log.info("-----fst before serilize----");
        byte[] bytes=conf.asByteArray(fstModel);
        log.info("-----fst after serilize----");
        FstModel original=(FstModel)conf.asObject(bytes);
        log.info("-----fst after deserilize----");
        log.info("original obj:"+original);
        log.info("original data:"+new String(original.getData()));
        return "OK";
    }
}
