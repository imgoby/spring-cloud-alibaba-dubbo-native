package com.yuhoutian.demo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect  // 使用@Aspect注解声明一个切面
@Component
public class MyAspect {
    @Before(value = "execution(* com.yuhoutian.demo.aspect.Task.exec(..))")
    public void before() throws Throwable {
        System.out.println("前置打印：----------------------------------------");
    }

}
