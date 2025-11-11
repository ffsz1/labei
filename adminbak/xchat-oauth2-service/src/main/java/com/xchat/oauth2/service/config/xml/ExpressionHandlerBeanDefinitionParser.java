/*
 * Copyright 2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xchat.oauth2.service.config.xml;

import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import com.xchat.oauth2.service.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.w3c.dom.Element;

/**
 * @author Ryan Heaton
 * @author Dave Syer
 */
public class ExpressionHandlerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return OAuth2MethodSecurityExpressionHandler.class;
	}

}
