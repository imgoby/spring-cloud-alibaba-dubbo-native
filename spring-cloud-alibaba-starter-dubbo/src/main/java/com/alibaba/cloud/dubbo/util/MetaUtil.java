package com.alibaba.cloud.dubbo.util;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.consul.discovery.ConsulServiceInstance;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetaUtil {
    public static Map<String, String> parseMeta(ServiceInstance serviceInstance) {
        Map<String, String> metadata = serviceInstance.getMetadata();
        //小心使用getMetadata()的地方，都会出错。
        if (metadata.isEmpty() || metadata.size() == 1) {
            metadata = getMetadata(((ConsulServiceInstance) serviceInstance).getTags());//by goby
        }

        return metadata;
    }


    private static Map<String, String> getMetadata(List<String> tags) {
        LinkedHashMap<String, String> metadata = new LinkedHashMap<>();
        if (tags != null) {
            for (String tag : tags) {
                String[] parts = StringUtils.delimitedListToStringArray(tag, "=");
                switch (parts.length) {
                    case 0:
                        break;
                    case 1:
                        metadata.put(parts[0], parts[0]);
                        break;
                    case 2:
                        metadata.put(parts[0], parts[1]);
                        break;
                    default:
                        String[] end = Arrays.copyOfRange(parts, 1, parts.length);
                        metadata.put(parts[0], StringUtils.arrayToDelimitedString(end, "="));
                        break;
                }

            }
        }

        return metadata;
    }
}
