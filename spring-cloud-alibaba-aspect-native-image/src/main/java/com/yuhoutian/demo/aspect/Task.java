package com.yuhoutian.demo.aspect;

import org.springframework.stereotype.Component;

@Component
public class Task {
    public void exec(String name){
        System.out.println("my name is:"+name);
    }
}
