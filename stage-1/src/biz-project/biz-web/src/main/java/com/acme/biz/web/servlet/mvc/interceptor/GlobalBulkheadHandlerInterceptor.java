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
package com.acme.biz.web.servlet.mvc.interceptor;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.internal.SemaphoreBulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 全局 Spring Web MVC 限流
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see Bulkhead
 * @see Semaphore
 * @see SemaphoreBulkhead
 * @since 1.0.0
 */
public class GlobalBulkheadHandlerInterceptor implements HandlerInterceptor, InitializingBean, DisposableBean, ApplicationListener<EnvironmentChangeEvent>, EnvironmentAware {

    private Bulkhead bulkhead;

    private Environment environment;

    private ServerProperties origin;

    private ServerProperties serverProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否需要限流
        // 正常执行 postHandle 方法
        // 异常执行 afterCompletion 方法
        bulkhead.acquirePermission();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 记录
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除状态
        bulkhead.releasePermission();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BulkheadConfig config = BulkheadConfig.custom().build();
        bulkhead = Bulkhead.of("globalBulkheadHandlerInterceptor", config);
        //自身支持修改
        bulkhead.changeConfig(config);


        Binder binder = new Binder(ConfigurationPropertySources.get(environment));
        origin = binder.bind("临时占用; 应该写自己配置限流的配置前缀", ServerProperties.class).get();

        //无支持动态变更配置
        CircuitBreaker circuitBreaker = CircuitBreaker.of("test1", CircuitBreakerConfig.custom().build());
        CircuitBreakerConfig circuitBreakerConfig = circuitBreaker.getCircuitBreakerConfig();
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        Set<String> changeKeys = event.getKeys();

        //判断自身配置有无变更
        if (Objects.equals(origin.getPort(), serverProperties.getPort())) {
            bulkhead.changeConfig(null);//修改配置

            //修改绑定
            Binder binder = new Binder(ConfigurationPropertySources.get(environment));
            origin = binder.bind("临时占用; 应该写自己配置限流的配置前缀", ServerProperties.class).get();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
