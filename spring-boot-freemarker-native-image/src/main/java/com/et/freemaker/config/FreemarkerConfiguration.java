package com.et.freemaker.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import freemarker.template.TemplateModelException;


@Configuration
public class FreemarkerConfiguration {

    //Configuration
    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void configuration() throws TemplateModelException {
        // add globe variable
        this.configuration.setSharedVariable("app", "Spring Boot");
    }
}