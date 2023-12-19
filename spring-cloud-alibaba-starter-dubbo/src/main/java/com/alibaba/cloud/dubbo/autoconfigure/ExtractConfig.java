package com.alibaba.cloud.dubbo.autoconfigure;

import com.alibaba.cloud.dubbo.metadata.DubboProtocolConfigSupplier;
import com.alibaba.cloud.dubbo.metadata.resolver.DubboServiceBeanMetadataResolver;
import com.alibaba.cloud.dubbo.metadata.resolver.MetadataResolver;
import feign.Contract;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.function.Supplier;

@Configuration(proxyBeanMethods = false)
public class ExtractConfig {
    //yuhou.todo
    //无计可施，增加applicationModel
    @Bean
    public Supplier<ProtocolConfig> dubboProtocolConfigSupplier(
            ObjectProvider<Collection<ProtocolConfig>> protocols, ApplicationModel applicationModel) {
        return new DubboProtocolConfigSupplier(protocols, applicationModel);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataResolver metadataJsonResolver(ObjectProvider<Contract> contract) {
        return new DubboServiceBeanMetadataResolver(contract);
    }
}
