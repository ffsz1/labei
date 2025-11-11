/**
 * 用于封装core层的异常
 */
package com.tongdaxing.xchat_framework.coremanager;

/**
 * @author daixiang
 *
 */
public class CoreException extends Exception {

	private CoreError error;

	private static final long serialVersionUID = 1L;

	public CoreException(CoreError coreError) {
		super(coreError.message, coreError.throwable);
		error = coreError;
	}

	public CoreException(String detailMessage) {
		super(detailMessage);
	}

	public CoreException(Throwable throwable) {
		super(throwable);
	}

	public CoreException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public CoreError getError() {
		return error;
	}
}
