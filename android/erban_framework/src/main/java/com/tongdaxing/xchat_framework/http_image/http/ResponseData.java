/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongdaxing.xchat_framework.http_image.http;

import com.google.gson.Gson;
import com.tongdaxing.xchat_framework.coremanager.CoreError;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import org.apache.http.HttpStatus;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
 * 网络或者cache的数据封装
 *
 * @author zhongyongsheng
 */
public class ResponseData {

    public final int statusCode;
    public final Map<String, String> headers;
    public final boolean notModified;
    private final Gson gson;
    public byte[] data;

    public ResponseData(int statusCode, byte[] data, Map<String, String> headers,
                        boolean notModified) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        gson = new Gson();
    }

    public ResponseData(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap(), false);
    }

    public ResponseData(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers, false);
    }

    public ServiceResult getResult() {
        
        ServiceResult result = gson.fromJson(new String(data, Charset.forName("ISO-8859-1")), ServiceResult.class);
        return result;
    }

    public static final int RELOGIN = 0;
    public static final int RECHECK_TICKET = 1;


    /**
     * accessToken失效的时候需要重新登录
     *
     * @return
     */
    public boolean isNeedReLogin() {
        ServiceResult result = getResult();
        if (result.getCode() == CoreError.ACCESS_TOKEN_HAS_EXPIRED || result.getCode() == CoreError.ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED || result.getCode() == CoreError.ACCESS_TOKEN_IS_MISSING || result.getCode() == CoreError.INVALID_AUTHORIZATION_CODE) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Ticket失效的时候需要重新检查tickets
     *
     * @return
     */
    public boolean isNeedReCheckTickets() {
        ServiceResult result = getResult();
        if (result.getCode() == CoreError.INVALID_TICKET || result.getCode() == CoreError.TICKET_IS_MISSING || result.getCode() == CoreError.TICKET_HAS_EXPIRED || result.getCode() == CoreError.TICKET_HAS_EXPIRED_SINCE_PASSWORD_CHANGED) {
            return true;
        } else {
            return false;
        }
    }

    public String getErrorStr() {
        String error = "";
        if (null != data) {
            ServiceResult result = getResult();
            if (null != result) {
                switch (result.getCode()) {
                    case CoreError.USERNAME_PASSWORD_MISMATCH:
                        error = "用户不存在或密码错误";
                        break;
                    case CoreError.INVALID_USER:
                        error = "用户不存在或密码错误";
                    break;
                    case CoreError.INVALID_TOKEN:
                        error = "用户不存在或密码错误";
                        break;
                    case CoreError.USER_HAS_BLOCKED:
                         error = "用户已被冻结";
                    break;
                    case CoreError.INVALID_IDENTIFYING_CODE:
                        error = "验证码错误";
                        break;
                    case CoreError.INVALID_USERNAME:
                        error = "手机号不可用";
                        break;
                    case CoreError.INVALID_RESET_CODE:
                        error = "重置密码无效";
                        break;
                    case CoreError.INVALID_NICK:
                        error = "昵称不合法";
                        break;
                    case CoreError.USER_HAS_SIGNED_UP:
                        error = "手机号码已经被注册";
                        break;

                    //------------------->accessToken
                    case CoreError.ACCESS_TOKEN_HAS_EXPIRED:
                        error = "登录凭证失效，请重新登录";
                        break;
                    case CoreError.ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED:
                        error = "登录凭证失效，请重新登录";
                        break;
                    case CoreError.ACCESS_TOKEN_IS_MISSING:
                        error = "登录凭证失效，请重新登录";
                        break;
                    case CoreError.INVALID_AUTHORIZATION_CODE:
                        error = "登录凭证失效，请重新登录";
                        break;
                    //------------------->accessToken

                    default:
                        error = "服务器正在维护";
                        break;
                }
            } else {
                error = "网络错误";
            }
        }
        return error;
    }
}