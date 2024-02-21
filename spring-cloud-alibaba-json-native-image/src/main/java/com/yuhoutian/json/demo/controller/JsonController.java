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

package com.yuhoutian.json.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.yuhoutian.json.demo.model.SinkPriorityRuleModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TrevorLink
 */
@RestController

public class JsonController {
    @GetMapping({"/json/test"})
    public List<SinkPriorityRuleModel> test() {
        String rules = "[{\"spId\":\"100000\",\"keyword\":\"邮政\",\"priority\":\"4\"}]";
        List<SinkPriorityRuleModel> ruleList = new ArrayList<>();
        ruleList = JSON.parseArray(rules, SinkPriorityRuleModel.class);
        return ruleList;
    }
}
