package com.yuhoutian.json.demo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SinkPriorityRuleModel implements Serializable {
    private String spId;
    private String keyword;
    private Integer priority;
}
