/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.bean;

import java.io.ByteArrayInputStream;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.simple.Simple;
import org.junit.jupiter.api.Test;

public class BeanParameterBindingStreamCachingTest extends ContextTestSupport {

    @Test
    public void testBean() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("abcABC");

        template.sendBody("direct:start", new ByteArrayInputStream("aBc".getBytes()));

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start").streamCaching().bean(Foo.class).to("mock:result");
            }
        };
    }

    public static class Foo {

        public static String hello(@Simple("${body}") String body, @Simple("${body}") String body2) {
            // we should be able to read the stream multiple times
            return body.toLowerCase() + body2.toUpperCase();
        }
    }

}
