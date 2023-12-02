package com.yuhoutian.demo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MyAspect {
    @Before(value = "execution(* com.space.aspect.Task.exec(..))")
    public void before() throws Throwable {
        System.out.println("前置打印：----------------------------------------");
    }

}
