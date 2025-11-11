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

import android.os.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhongyongsheng
 */
@SuppressWarnings("rawtypes")
public class NetworkDispatcher extends Thread {
    private final BlockingQueue<Request> mQueue;
    private volatile boolean mQuit = false;
    private RequestProcessor mRequestProcessor;

    public NetworkDispatcher(BlockingQueue<Request> queue, String name,
                             RequestProcessor requestProcessor) {
        super(name + "NetworkThread");
        mQueue = queue;
        mRequestProcessor = requestProcessor;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Request request;
        while (true) {

            try {
                request = mQueue.take();
            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                }
                continue;
            }

            try {

                waitIfPaused();

                HttpLog.v("Network queue take");

                if (request.isCanceled()) {
                    request.finish("Network discard cancelled");
                    continue;
                }

                ResponseData responseData = request.getNetwork().performRequest(request);
                HttpLog.v("Network http complete");

                if (responseData.notModified && request.hasHadResponseDelivered()) {
                    request.finish("Network not modified");
                    continue;
                }

                request.parseDataToResponse(responseData);
                HttpLog.v("Network parse complete");

                request.markDelivered();
                request.postResponse();

                if (request.shouldCache() && request.getResponse().cacheEntry != null) {
                    request.getCache().put(request.getKey(), request.getResponse().cacheEntry);
                    HttpLog.v("Network cache written");
                }
            } catch (RequestError error) {
                parseAndDeliverNetworkError(request, error);
            } catch (Exception e) {
                HttpLog.e(e, "Unhandled exception " + e.toString());
                request.postError(new RequestError(e));
            } catch (Error e) {
                HttpLog.e(e, "Unhandled error " + e.toString());
                request.postError(new RequestError(e));
            }
        }
    }

    private void waitIfPaused() {
        AtomicBoolean pause = mRequestProcessor.getPause();
        synchronized (pause) {
            if (pause.get()) {
                HttpLog.v("Network Wait for pause");
                try {
                    pause.wait();
                } catch (InterruptedException e) {
                    HttpLog.e(e, "Network Wait for pause interrupted");
                    return;
                }
                HttpLog.v("Network Resume pause");
            }
        }
    }

    private void parseAndDeliverNetworkError(Request<?> request, RequestError error) {
        request.postError(error);
    }
}
