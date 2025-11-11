/*
 * Copyright 2006-2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.xchat.oauth2.service.provider.expression;

import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/**
 * <p>
 * A security expression handler that can handle default web security expressions plus the set provided by
 * {@link com.xchat.oauth2.service.provider.expression.OAuth2SecurityExpressionMethods} using the variable oauth2 to access the methods. For example, the expression
 * <code>#oauth2.clientHasRole('ROLE_ADMIN')</code> would invoke {@link com.xchat.oauth2.service.provider.expression.OAuth2SecurityExpressionMethods#clientHasRole}.
 * </p>
 * <p>
 * By default the {@link com.xchat.oauth2.service.provider.expression.OAuth2ExpressionParser} is used. If this is undesirable one can inject their own
 * {@link org.springframework.expression.ExpressionParser} using {@link #setExpressionParser(org.springframework.expression.ExpressionParser)}.
 * </p>
 * 
 * @author Dave Syer
 * @author Rob Winch
 * 
 * @see com.xchat.oauth2.service.provider.expression.OAuth2ExpressionParser
 */
public class OAuth2WebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {
	public OAuth2WebSecurityExpressionHandler() {
		setExpressionParser(new com.xchat.oauth2.service.provider.expression.OAuth2ExpressionParser(getExpressionParser()));
	}

	@Override
	protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication,
			FilterInvocation invocation) {
		StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, invocation);
		ec.setVariable("oauth2", new OAuth2SecurityExpressionMethods(authentication));
		return ec;
	}
}
