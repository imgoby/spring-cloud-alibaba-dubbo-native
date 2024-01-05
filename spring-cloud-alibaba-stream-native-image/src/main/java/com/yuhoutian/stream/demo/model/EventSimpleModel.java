package com.yuhoutian.stream.demo.model;


import java.io.Serializable;
import java.util.Date;


public class EventSimpleModel implements Serializable {
    private Integer intId;
    private Long longId;
    private String stringId;
    private String content;
    private Date date;

    public Integer getIntId() {
        return intId;
    }

    public Long getLongId() {
        return longId;
    }

    public String getStringId() {
        return stringId;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public void setLongId(Long longId) {
        this.longId = longId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}