package com.xchat.oauth2.service.core.exception;

import java.util.Map;
import java.util.TreeMap;

/**
 * Base exception for OAuth 2 exceptions.
 * 
 * @author Ryan Heaton
 * @author Rob Winch
 * @author Dave Syer
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ServiceExceptionJacksonSerializer.class)
public class ServiceException extends RuntimeException {

	protected StatusCode statusCode;

	protected int HttpCode = 400;

	private Map<String, String> additionalInformation = null;

	public ServiceException(String msg, Throwable t) {
		super(msg, t);
		statusCode = StatusCode.BUSINESS_ERROR;
	}

	public ServiceException(String msg) {
		super(msg);
		statusCode = StatusCode.BUSINESS_ERROR;
	}


	public StatusCode getStatusCode(){
		return statusCode;
	}

	public int getHttpCode() {
		return HttpCode;
	}

	/**
	 * Get any additional information associated with this error.
	 * 
	 * @return Additional information, or null if none.
	 */
	public Map<String, String> getAdditionalInformation() {
		return this.additionalInformation;
	}

	/**
	 * Add some additional information with this OAuth error.
	 * 
	 * @param key The key.
	 * @param value The value.
	 */
	public void addAdditionalInformation(String key, String value) {
		if (this.additionalInformation == null) {
			this.additionalInformation = new TreeMap<String, String>();
		}

		this.additionalInformation.put(key, value);

	}

	
	@Override
	public String toString() {
		return getSummary();
	}

	/**
	 * @return a comma-delimited list of details (key=value pairs)
	 */
	public String getSummary() {
		
		StringBuilder builder = new StringBuilder();

		String delim = "";

		StatusCode statusCode = this.getStatusCode();
		builder.append(delim).append("code=\"").append(statusCode.value()).append("\"");
		delim = ", ";

		String errorMessage = this.getMessage();
		if (errorMessage != null) {
			builder.append(delim).append("message=\"").append(statusCode.getReasonPhrase()).append("\"");
			delim = ", ";
		}

		Map<String, String> additionalParams = this.getAdditionalInformation();
		if (additionalParams != null) {
			for (Map.Entry<String, String> param : additionalParams.entrySet()) {
				builder.append(delim).append(param.getKey()).append("=\"").append(param.getValue()).append("\"");
				delim = ", ";
			}
		}
		
		return builder.toString();

	}
}
