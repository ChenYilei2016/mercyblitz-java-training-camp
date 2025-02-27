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
package com.acme.biz.api.i18n;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link PropertySourceMessageSource} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class PropertySourceMessageSourceTest {

    private PropertySourceMessageSource propertySourceMessageSource;

    @BeforeEach
    public void init() throws Exception {
        ConfigurableEnvironment environment = new MockEnvironment();
        propertySourceMessageSource = new PropertySourceMessageSource(environment);
        propertySourceMessageSource.afterPropertiesSet();
    }

    @Test
    public void test() {
        String code = "my.name";
        Object[] args = new Object[0];
        assertEquals("小马哥", propertySourceMessageSource.getMessage(code,args, Locale.getDefault()));
        assertEquals("mercyblitz", propertySourceMessageSource.getMessage(code,args, Locale.ENGLISH));
        assertEquals("mercy blitz", propertySourceMessageSource.getMessage(code,args, Locale.US));
        assertEquals("default message", propertySourceMessageSource.getMessage("not.exist.code", args, "default message", Locale.US));
    }

}
