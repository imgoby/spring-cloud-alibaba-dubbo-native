package com.yuhoutian.protobuf.demo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FstModel implements Serializable {
    private long id;
    private String name;
    private byte[] data;
}
