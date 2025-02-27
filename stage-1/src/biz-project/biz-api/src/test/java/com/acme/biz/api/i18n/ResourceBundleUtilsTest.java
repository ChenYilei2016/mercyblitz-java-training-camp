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

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * {@link ResourceBundleUtils} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ResourceBundleUtilsTest {

    @Test
    public void testJavaPropertiesResourceBundle() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("META-INF.Messages");
        System.out.println(resourceBundle.getString("my.name"));
    }

    @Test
    public void testJavaClassResourceBundle() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.acme.biz.api.i18n.HardCodeResourceBundle");
        System.out.println(resourceBundle.getString("my.name"));
    }
    /**
     * i18n resouceBundle soft缓存 , 内存和效率的兼顾
     * {@link ResourceBundle#getBundle(String, Locale, ClassLoader)}
     */
}
