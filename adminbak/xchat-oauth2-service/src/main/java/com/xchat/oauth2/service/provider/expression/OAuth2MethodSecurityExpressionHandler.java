package com.xchat.oauth2.service.provider.expression;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

/**
 * <p>
 * A security expression handler that can handle default method security expressions plus the set provided by
 * {@link com.xchat.oauth2.service.provider.expression.OAuth2SecurityExpressionMethods} using the variable oauth2 to access the methods. For example, the expression
 * <code>#oauth2.clientHasRole('ROLE_ADMIN')</code> would invoke {@link com.xchat.oauth2.service.provider.expression.OAuth2SecurityExpressionMethods#clientHasRole}
 * </p>
 * <p>
 * By default the {@link com.xchat.oauth2.service.provider.expression.OAuth2ExpressionParser} is used. If this is undesirable one can inject their own
 * {@link org.springframework.expression.ExpressionParser} using {@link #setExpressionParser(org.springframework.expression.ExpressionParser)}.
 * </p>
 * 
 * @author Dave Syer
 * @author Rob Winch
 * @see com.xchat.oauth2.service.provider.expression.OAuth2ExpressionParser
 */
public class OAuth2MethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

	public OAuth2MethodSecurityExpressionHandler() {
		setExpressionParser(new com.xchat.oauth2.service.provider.expression.OAuth2ExpressionParser(getExpressionParser()));
	}

	@Override
	public StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, MethodInvocation mi) {
		StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, mi);
		ec.setVariable("oauth2", new OAuth2SecurityExpressionMethods(authentication));
		return ec;
	}
}
