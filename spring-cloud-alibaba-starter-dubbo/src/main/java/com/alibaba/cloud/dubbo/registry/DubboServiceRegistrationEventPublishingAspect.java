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

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;

/**
 * Dubbo Service Registration Event-Publishing Aspect.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ServiceInstancePreRegisteredEvent
 * @see ServiceInstanceRegisteredEvent
 * @see ServiceInstancePreDeregisteredEvent
 */

@Configuration(proxyBeanMethods = false)
public class DubboServiceRegistrationEventPublishingAspect
		implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	public void beforeRegister(ServiceRegistry registry, Registration registration) {
		applicationEventPublisher.publishEvent(
				new ServiceInstancePreRegisteredEvent(registry, registration));
	}

	public void beforeDeregister(ServiceRegistry registry, Registration registration) {
		applicationEventPublisher.publishEvent(
				new ServiceInstancePreDeregisteredEvent(registry, registration));
	}

	public void afterRegister(ServiceRegistry registry, Registration registration) {
		applicationEventPublisher
				.publishEvent(new ServiceInstanceRegisteredEvent(registration));
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Configuration(proxyBeanMethods = false)
	@Aspect
	@ConditionalOnClass(name = "org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry")
	class ConsulConfiguration {
		public static final String REGISTER_POINTCUT_EXPRESSION = "execution(* org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry.register(*)) && target(registry) && args(registration)";
		public static final String DEREGISTER_POINTCUT_EXPRESSION = "execution(* org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry.deregister(*)) && target(registry) && args(registration)";

		@Before(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeRegister(registry,registration);
		}

		@Before(value = DEREGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeDeregister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeDeregister(registry,registration);
		}

		@After(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void afterRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.afterRegister(registry,registration);
		}
	}

	@Configuration(proxyBeanMethods = false)
	@Aspect
	@ConditionalOnClass(name = "org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry")
	class EurekaConfiguration {
		public static final String REGISTER_POINTCUT_EXPRESSION = "execution(* org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.register(*)) && target(registry) && args(registration)";
		public static final String DEREGISTER_POINTCUT_EXPRESSION = "execution(* org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.deregister(*)) && target(registry) && args(registration)";

		@Before(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeRegister(registry,registration);
		}

		@Before(value = DEREGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeDeregister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeDeregister(registry,registration);
		}

		@After(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void afterRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.afterRegister(registry,registration);
		}
	}

	@Configuration(proxyBeanMethods = false)
	@Aspect
	@ConditionalOnClass(name = "com.alibaba.cloud.nacos.registry.NacosServiceRegistry")
	class NacosConfiguration {
		public static final String REGISTER_POINTCUT_EXPRESSION = "execution(* com.alibaba.cloud.nacos.registry.NacosServiceRegistry.register(*)) && target(registry) && args(registration)";
		public static final String DEREGISTER_POINTCUT_EXPRESSION = "execution(* com.alibaba.cloud.nacos.registry.NacosServiceRegistry.deregister(*)) && target(registry) && args(registration)";

		@Before(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeRegister(registry,registration);
		}

		@Before(value = DEREGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeDeregister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeDeregister(registry,registration);
		}

		@After(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void afterRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.afterRegister(registry,registration);
		}
	}

	@Configuration(proxyBeanMethods = false)
	@Aspect
	@ConditionalOnClass(name = "org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry")
	class ZookeeperConfiguration {
		public static final String REGISTER_POINTCUT_EXPRESSION = "execution(* org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry.register(*)) && target(registry) && args(registration)";
		public static final String DEREGISTER_POINTCUT_EXPRESSION = "execution(* org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry.deregister(*)) && target(registry) && args(registration)";

		@Before(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeRegister(registry,registration);
		}

		@Before(value = DEREGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void beforeDeregister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.beforeDeregister(registry,registration);
		}

		@After(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
		public void afterRegister(ServiceRegistry registry, Registration registration) {
			DubboServiceRegistrationEventPublishingAspect.this.afterRegister(registry,registration);
		}
	}


}
