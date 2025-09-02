package com.yuhoutian.kryo.demo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class KryoModel implements Serializable {
    private long id;
    private String name;
    private byte[] data;
}
