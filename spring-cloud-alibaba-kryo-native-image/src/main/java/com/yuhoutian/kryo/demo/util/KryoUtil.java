package com.yuhoutian.kryo.demo.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yuhoutian.kryo.demo.model.KryoModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoUtil {
    private static final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true); // 支持对象引用
        kryo.register(KryoModel.class); // 注册需要序列化的类
        return kryo;
    });

    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            kryos.get().writeClassAndObject(output, obj);
            output.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            T obj= (T)kryos.get().readClassAndObject(input);
            return obj;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}