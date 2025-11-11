package com.tongdaxing.xchat_core.crash;

import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

/**
 * Created by chenran on 2017/4/2.
 */

public class CrashCoreImpl extends AbstractBaseCore implements ICrashCore {
    @Override
    public void uploadCrashFile() {
//        final File file  = BasicConfig.INSTANCE.getLogDir();
//        final File[] fileList = file.listFiles();
//        if (fileList != null && fileList.length > 0) {
//            int count = fileList.length;
//            for (int i = 0; i < fileList.length; i++) {
//                final File f = fileList[i];
//                if (!file.exists()) {
//                    continue;
//                }
//                RequestParam params = CommonParamUtil.fillCommonParam();
//                params.put("imei", CoreManager.getCore(IHomeCore.class).getImei());
//                params.put("osVersion,", TelephonyUtils.getOSVersion());
//                params.put("deviceModel", TelephonyUtils.getDeviceModel());
//                params.put("file", new RequestParam.FileWrapper(f, FileUtils.getFileName(f.getPath())));
//                ResponseListener listener = new ResponseListener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        f.delete();
//                    }
//                };
//                ResponseErrorListener errorListener = new ResponseErrorListener() {
//                    @Override
//                    public void onErrorResponse(RequestError error) {
//                    }
//                };
//
//                RequestManager.instance()
//                        .submitMultipartPostRequest(UriProvider.reportCrash(),
//                                CommonParamUtil.getDefaultHeaders(getContext()),
//                                params,
//                                String.class, listener, errorListener, new ProgressListener() {
//                                    @Override
//                                    public void onProgress(ProgressInfo info) {
//                                    }
//                                });
//            }
//        }
    }
}
