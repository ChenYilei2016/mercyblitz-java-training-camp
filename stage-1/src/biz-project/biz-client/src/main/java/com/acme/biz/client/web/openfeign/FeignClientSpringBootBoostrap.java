/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.biz.client.web.openfeign;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import com.acme.biz.client.loadbalancer.UserServiceLoadBalancerConfiguration;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * Feign 客户端引导类
 *
 * H 以前的sc版本 , 依靠的是 {@link org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration}
 *
 *
 * H及以后, sc想替代 Netflix , 有了 {@link org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration} 去做负载均衡
 * {@link  FeignLoadBalancerAutoConfiguration}
 *
 * 在{@link org.springframework.cloud.openfeign.FeignClientsRegistrar#registerFeignClient(BeanDefinitionRegistry, AnnotationMetadata, Map)} 注册feign client
 * 拿到FeignContext, 他继承了当前spring容器, 因此在 {@link FeignClientFactoryBean#getTarget()}中 , get Client.class, 得到了   FeignLoadBalancerAutoConfiguration里import的 feign client
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@EnableAutoConfiguration
@EnableFeignClients(clients = UserRegistrationService.class)
@LoadBalancerClient(name = "user-service", configuration = UserServiceLoadBalancerConfiguration.class)
public class FeignClientSpringBootBoostrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(FeignClientSpringBootBoostrap.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);

        UserRegistrationService userRegistrationService = context.getBean(UserRegistrationService.class);

        User user = new User();
        user.setId(1L);
        user.setName("ABC");
        System.out.println("userRegistrationService.registerUser : "+userRegistrationService.registerUser(user));

        context.close();
    }
}
