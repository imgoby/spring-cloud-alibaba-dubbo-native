package com.yuhoutian.demo.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    private Task task;

    @GetMapping("/before")
    public String before() {
        task.exec("before..");
        return "before";
    }
}
