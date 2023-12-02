/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.dubbo.registry;

import com.alibaba.cloud.dubbo.registry.event.ServiceInstancePreDeregisteredEvent;
import com.alibaba.cloud.dubbo.registry.event.ServiceInstancePreRegisteredEvent;
import com.alibaba.cloud.dubbo.registry.event.ServiceInstanceRegisteredEvent;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Dubbo Service Registration Event-Publishing Aspect.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ServiceInstancePreRegisteredEvent
 * @see ServiceInstanceRegisteredEvent
 * @see ServiceInstancePreDeregisteredEvent
 */
@Aspect
public class DubboServiceRegistrationEventPublishingAspect
		implements ApplicationEventPublisherAware {

	/**
	 * The pointcut expression for {@link ServiceRegistry#register(Registration)}.
	 */
	public static final String REGISTER_POINTCUT_EXPRESSION_CONSUL = "execution(* org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry.register(*)) && target(registry) && args(registration)";
//	public static final String REGISTER_POINTCUT_EXPRESSION_EUREKA = "execution(* org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.register(*)) && target(registry) && args(registration)";
//	public static final String REGISTER_POINTCUT_EXPRESSION_NACOS = "execution(* com.alibaba.cloud.nacos.registry.NacosServiceRegistry.register(*)) && target(registry) && args(registration)";
//	public static final String REGISTER_POINTCUT_EXPRESSION_ZOOKEEPER = "execution(* org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry.register(*)) && target(registry) && args(registration)";

	/**
	 * The pointcut expression for {@link ServiceRegistry#deregister(Registration)}.
	 */
	public static final String DEREGISTER_POINTCUT_EXPRESSION_CONSUL = "execution(* org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry.deregister(*)) && target(registry) && args(registration)";
//	public static final String DEREGISTER_POINTCUT_EXPRESSION_EUREKA = "execution(* org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.deregister(*)) && target(registry) && args(registration)";
//	public static final String DEREGISTER_POINTCUT_EXPRESSION_NACOS = "execution(* com.alibaba.cloud.nacos.registry.NacosServiceRegistry.deregister(*)) && target(registry) && args(registration)";
//	public static final String DEREGISTER_POINTCUT_EXPRESSION_ZOOKEEPER = "execution(* org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry.deregister(*)) && target(registry) && args(registration)";

	private ApplicationEventPublisher applicationEventPublisher;

	@Before(value = REGISTER_POINTCUT_EXPRESSION_CONSUL, argNames = "registry, registration")
	public void beforeRegisterConsul(ServiceRegistry registry, Registration registration) {
		beforeRegister(registry,registration);
	}
//	@Before(value = REGISTER_POINTCUT_EXPRESSION_EUREKA, argNames = "registry, registration")
//	public void beforeRegisterEureka(ServiceRegistry registry, Registration registration) {
//		beforeRegister(registry,registration);
//	}
//	@Before(value = REGISTER_POINTCUT_EXPRESSION_NACOS, argNames = "registry, registration")
//	public void beforeRegisterNacos(ServiceRegistry registry, Registration registration) {
//		beforeRegister(registry,registration);
//	}
//	@Before(value = REGISTER_POINTCUT_EXPRESSION_ZOOKEEPER, argNames = "registry, registration")
//	public void beforeRegisterZookeeper(ServiceRegistry registry, Registration registration) {
//		beforeRegister(registry,registration);
//	}

	private void beforeRegister(ServiceRegistry registry, Registration registration) {
		applicationEventPublisher.publishEvent(
				new ServiceInstancePreRegisteredEvent(registry, registration));
	}

	@Before(value = DEREGISTER_POINTCUT_EXPRESSION_CONSUL, argNames = "registry, registration")
	public void beforeDeregisterConsul(ServiceRegistry registry, Registration registration) {
		beforeDeregister(registry,registration);
	}

//	@Before(value = DEREGISTER_POINTCUT_EXPRESSION_EUREKA, argNames = "registry, registration")
//	public void beforeDeregisterEureka(ServiceRegistry registry, Registration registration) {
//		beforeDeregister(registry,registration);
//	}
//
//	@Before(value = DEREGISTER_POINTCUT_EXPRESSION_NACOS, argNames = "registry, registration")
//	public void beforeDeregisterNacos(ServiceRegistry registry, Registration registration) {
//		beforeDeregister(registry,registration);
//	}
//
//	@Before(value = DEREGISTER_POINTCUT_EXPRESSION_ZOOKEEPER, argNames = "registry, registration")
//	public void beforeDeregisterZookeeper(ServiceRegistry registry, Registration registration) {
//		beforeDeregister(registry,registration);
//	}

	private void beforeDeregister(ServiceRegistry registry, Registration registration) {
		applicationEventPublisher.publishEvent(
				new ServiceInstancePreDeregisteredEvent(registry, registration));
	}

	@After(value = REGISTER_POINTCUT_EXPRESSION_CONSUL, argNames = "registry, registration")
	public void afterRegisterConsul(ServiceRegistry registry, Registration registration) {
		afterRegister(registry,registration);
	}

//	@After(value = REGISTER_POINTCUT_EXPRESSION_NACOS, argNames = "registry, registration")
//	public void afterRegisterNacos(ServiceRegistry registry, Registration registration) {
//		afterRegister(registry,registration);
//	}
//
//	@After(value = REGISTER_POINTCUT_EXPRESSION_EUREKA, argNames = "registry, registration")
//	public void afterRegisterEureka(ServiceRegistry registry, Registration registration) {
//		afterRegister(registry,registration);
//	}
//
//	@After(value = REGISTER_POINTCUT_EXPRESSION_ZOOKEEPER, argNames = "registry, registration")
//	public void afterRegisterZookeeper(ServiceRegistry registry, Registration registration) {
//		afterRegister(registry,registration);
//	}

	private void afterRegister(ServiceRegistry registry, Registration registration) {
		applicationEventPublisher
				.publishEvent(new ServiceInstanceRegisteredEvent(registration));
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

}
