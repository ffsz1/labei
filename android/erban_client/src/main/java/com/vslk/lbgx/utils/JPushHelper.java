package com.vslk.lbgx.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.orhanobut.logger.Logger;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * 处理tagalias相关的逻辑
 */
public class JPushHelper {
    private static final String TAG = "JIGUANG-TagAliasHelper";
    public static int sequence = 1;
    /**
     * 增加tag
     */
    public static final int ACTION_ADD_TAG = 1;
    /**
     * 覆盖tag
     */
    public static final int ACTION_SET_TAG = 2;
    /**
     * 删除部分tag
     */
    public static final int ACTION_DELETE_TAG = 3;
    /**
     * 删除所有tag
     */
    public static final int ACTION_CLEAN_TAG = 4;
    /**
     * 查询tag
     */
    public static final int ACTION_GET_TAG = 5;

    public static final int ACTION_CHECK_TAG = 6;

    /**
     * 增加tag
     */
    public static final int ACTION_ADD_ALIAS = 7;
    /**
     * 覆盖tag
     */
    public static final int ACTION_SET_ALIAS = 8;
    /**
     * 删除部分tag
     */
    public static final int ACTION_DELETE_ALIAS = 9;
    /**
     * 删除所有tag
     */
    public static final int ACTION_CLEAN_ALIAS = 10;
    /**
     * 查询tag
     */
    public static final int ACTION_GET_ALIAS = 11;

    public static final int ACTION_CHECK_ALIAS = 12;

    public static final int DELAY_SEND_ACTION = 1;

    public static final int DELAY_SET_MOBILE_NUMBER_ACTION = 2;

    private WeakReference<Context> weakReference;
    private static JPushHelper mInstance;

    private JPushHelper() {
    }

    public static JPushHelper getInstance() {
        if (mInstance == null) {
            synchronized (JPushHelper.class) {
                if (mInstance == null) {
                    mInstance = new JPushHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        if (context != null) {
            this.weakReference = new WeakReference<>(context.getApplicationContext());
        }
    }

    private SparseArray<Object> setActionCache = new SparseArray<Object>();

    public Object get(int sequence) {
        return setActionCache.get(sequence);
    }

    public Object remove(int sequence) {
        return setActionCache.get(sequence);
    }

    public void put(int sequence, Object tagAliasBean) {
        setActionCache.put(sequence, tagAliasBean);
    }

    private JPushHelperHandler delaySendHandler = new JPushHelperHandler(this);

    static class JPushHelperHandler extends Handler {
        WeakReference<JPushHelper> jpushHelper;

        public JPushHelperHandler(JPushHelper jpushHelper) {
            this.jpushHelper = new WeakReference<>(jpushHelper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (jpushHelper == null || jpushHelper.get() == null)
                return;
            switch (msg.what) {
                case DELAY_SEND_ACTION:
                    if (msg.obj != null && msg.obj instanceof TagAliasBean) {
                        Logger.i(TAG, "on delay time");
                        sequence++;
                        TagAliasBean tagAliasBean = (TagAliasBean) msg.obj;
                        jpushHelper.get().setActionCache.put(sequence, tagAliasBean);
                        jpushHelper.get().handleAction(sequence, tagAliasBean);
                    } else {
                        Logger.w(TAG, "#unexcepted - msg obj was incorrect");
                    }
                    break;
                case DELAY_SET_MOBILE_NUMBER_ACTION:
                    if (msg.obj != null && msg.obj instanceof String) {
                        Logger.i(TAG, "retry set mobile number");
                        sequence++;
                        String mobileNumber = (String) msg.obj;
                        jpushHelper.get().setActionCache.put(sequence, mobileNumber);
                        jpushHelper.get().handleAction(sequence, mobileNumber);
                    } else {
                        Logger.w(TAG, "#unexcepted - msg obj was incorrect");
                    }
                    break;
            }
        }
    }

    /**
     * tag操作
     *
     * @param context
     * @param tags    类型 增加  设置  删除 获取 清除
     * @param action
     */
    public void onTagsAction(Context context, Set<String> tags, int action) {
        if (tags == null)
            return;
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = action;
        sequence = action;
        tagAliasBean.tags = tags;
        tagAliasBean.isAliasAction = false;
        init(context);
        handleAction(sequence, tagAliasBean);
    }

    /**
     * alias操作
     *
     * @param context
     * @param alias   类型 检查 设置  删除 获取
     * @param action
     */
    public void onAliasAction(Context context, String alias, int action) {
        if (StringUtils.isEmpty(alias)) {
            return;
        }
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = action;
        sequence = action;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = true;
        init(context);
        handleAction(sequence, tagAliasBean);
    }

    public void handleAction(int sequence, String mobileNumber) {
        put(sequence, mobileNumber);
        Logger.d(TAG, "sequence:" + sequence + ",mobileNumber:" + mobileNumber);
        if (weakReference != null && weakReference.get() != null) {
            JPushInterface.setMobileNumber(weakReference.get(), sequence, mobileNumber);
        } else {
            Logger.e(TAG, "#unexcepted - context was null");
        }
    }

    /**
     * 处理设置tag
     */
    public void handleAction(int sequence, TagAliasBean tagAliasBean) {
        Context context = weakReference.get();
        if (context == null) {
            Logger.e(TAG, "#unexcepted - context was null");
            return;
        }
        if (tagAliasBean == null) {
            Logger.w(TAG, "tagAliasBean was null");
            return;
        }
        put(sequence, tagAliasBean);
        if (tagAliasBean.isAliasAction) {
            switch (tagAliasBean.action) {
                case ACTION_GET_ALIAS:
                    JPushInterface.getAlias(context, sequence);
                    break;
                case ACTION_DELETE_ALIAS:
                    JPushInterface.deleteAlias(context, sequence);
                    break;
                case ACTION_SET_ALIAS:
                    JPushInterface.setAlias(context, sequence, tagAliasBean.alias);
                    break;
                default:
                    Logger.w(TAG, "unsupport alias action type");
                    return;
            }
        } else {
            switch (tagAliasBean.action) {
                case ACTION_ADD_TAG:
                    JPushInterface.addTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_SET_TAG:
                    JPushInterface.setTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_DELETE_TAG:
                    JPushInterface.deleteTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_CHECK_TAG:
                    //一次只能check一个tag
                    String tag = (String) tagAliasBean.tags.toArray()[0];
                    JPushInterface.checkTagBindState(context, sequence, tag);
                    break;
                case ACTION_GET_TAG:
                    JPushInterface.getAllTags(context, sequence);
                    break;
                case ACTION_CLEAN_TAG:
                    JPushInterface.cleanTags(context, sequence);
                    break;
                default:
                    Logger.w(TAG, "unsupport tag action type");
                    return;
            }
        }
    }

    private boolean RetryActionIfNeeded(int errorCode, TagAliasBean tagAliasBean) {
        Context context = weakReference.get();
        if (context == null) {
            return false;
        }
        if (!NetworkUtil.isNetworkConnected(context)) {
            Logger.w(TAG, "no network");
            return false;
        }
        //返回的错误码为6002 超时,6014 服务器繁忙,都建议延迟重试
        if (errorCode == 6002 || errorCode == 6014) {
            Logger.d(TAG, "need retry");
            if (tagAliasBean != null) {
                Message message = new Message();
                message.what = DELAY_SEND_ACTION;
                message.obj = tagAliasBean;
                delaySendHandler.sendMessageDelayed(message, 1000 * 60);
                String logs = getRetryStr(tagAliasBean.isAliasAction, tagAliasBean.action, errorCode);
//                ExampleUtil.showToast(logs, context);
                return true;
            }
        }
        return false;
    }

    private boolean RetrySetMObileNumberActionIfNeeded(int errorCode, String mobileNumber) {
        Context context = weakReference.get();
        if (context == null) {
            return false;
        }
        if (!NetworkUtil.isNetworkConnected(context)) {
            Logger.w(TAG, "no network");
            return false;
        }
        //返回的错误码为6002 超时,6024 服务器内部错误,建议稍后重试
        if (errorCode == 6002 || errorCode == 6024) {
            Logger.d(TAG, "need retry");
            Message message = new Message();
            message.what = DELAY_SET_MOBILE_NUMBER_ACTION;
            message.obj = mobileNumber;
            delaySendHandler.sendMessageDelayed(message, 1000 * 60);
            String str = "Failed to set mobile number due to %s. Try again after 60s.";
            str = String.format(Locale.ENGLISH, str, (errorCode == 6002 ? "timeout" : "server internal error”"));
//            ExampleUtil.showToast(str, context);
            return true;
        }
        return false;

    }

    private String getRetryStr(boolean isAliasAction, int actionType, int errorCode) {
        String str = "Failed to %s %s due to %s. Try again after 60s.";
        str = String.format(Locale.ENGLISH, str, getActionStr(actionType), (isAliasAction ? "alias" : " tags"), (errorCode == 6002 ? "timeout" : "server too busy"));
        return str;
    }

    private String getActionStr(int actionType) {
        switch (actionType) {
            case ACTION_ADD_TAG:
                return "add_tag";
            case ACTION_SET_TAG:
                return "set_tag";
            case ACTION_DELETE_TAG:
                return "delete_tag";
            case ACTION_GET_TAG:
                return "get_tag";
            case ACTION_CLEAN_TAG:
                return "clean_tag";
            case ACTION_CHECK_TAG:
                return "check_tag";
            case ACTION_ADD_ALIAS:
                return "add_alias";
            case ACTION_SET_ALIAS:
                return "set_alias";
            case ACTION_DELETE_ALIAS:
                return "delete_alias";
            case ACTION_GET_ALIAS:
                return "get_alias";
            case ACTION_CLEAN_ALIAS:
                return "clean_alias";
            case ACTION_CHECK_ALIAS:
                return "check_alias";
        }
        return "unkonw operation";
    }

    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Logger.i(TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.getTags());
        Logger.i(TAG, "tags size:" + jPushMessage.getTags().size());
        init(context);
        //根据sequence从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = (TagAliasBean) setActionCache.get(sequence);
        if (tagAliasBean == null) {
//            ExampleUtil.showToast("获取缓存记录失败", context);
            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            Logger.i(TAG, "action - modify tag Success,sequence:" + sequence);
            setActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " tags success";
            Logger.i(TAG, logs);
//            ExampleUtil.showToast(logs, context);
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags";
            if (jPushMessage.getErrorCode() == 6018) {
                //tag数量超过限制,需要先清除一部分再add
                logs += ", tags is exceed limit need to clean";
            }
            logs += ", errorCode:" + jPushMessage.getErrorCode();
            Logger.e(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
//                ExampleUtil.showToast(logs, context);
            }
        }
    }

    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Logger.i(TAG, "action - onCheckTagOperatorResult, sequence:" + sequence + ",checktag:" + jPushMessage.getCheckTag());
        init(context);
        //根据sequence从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = (TagAliasBean) setActionCache.get(sequence);
        if (tagAliasBean == null) {
//            ExampleUtil.showToast("获取缓存记录失败", context);
            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            Logger.i(TAG, "tagBean:" + tagAliasBean);
            setActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " tag " + jPushMessage.getCheckTag() + " bind state success,state:" + jPushMessage.getTagCheckStateResult();
            Logger.i(TAG, logs);
//            ExampleUtil.showToast(logs, context);
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags, errorCode:" + jPushMessage.getErrorCode();
            Logger.e(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
//                ExampleUtil.showToast(logs, context);
            }
        }
    }

    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Logger.i(TAG, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:" + jPushMessage.getAlias());
        init(context);
        //根据sequence从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = (TagAliasBean) setActionCache.get(sequence);
        if (tagAliasBean == null) {
//            ExampleUtil.showToast("获取缓存记录失败", context);
            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            Logger.i(TAG, "action - modify alias Success,sequence:" + sequence);
            setActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " alias success";
            Logger.i(TAG, logs);
//            ExampleUtil.showToast(logs, context);
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " alias, errorCode:" + jPushMessage.getErrorCode();
            Logger.e(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
//                ExampleUtil.showToast(logs, context);
            }
        }
    }

    //设置手机号码回调
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Logger.i(TAG, "action - onMobileNumberOperatorResult, sequence:" + sequence + ",mobileNumber:" + jPushMessage.getMobileNumber());
        init(context);
        if (jPushMessage.getErrorCode() == 0) {
            Logger.i(TAG, "action - set mobile number Success,sequence:" + sequence);
            setActionCache.remove(sequence);
        } else {
            String logs = "Failed to set mobile number, errorCode:" + jPushMessage.getErrorCode();
            Logger.e(TAG, logs);
            if (!RetrySetMObileNumberActionIfNeeded(jPushMessage.getErrorCode(), jPushMessage.getMobileNumber())) {
//                ExampleUtil.showToast(logs, context);
            }
        }
    }

    public static class TagAliasBean {
        int action;
        Set<String> tags;
        String alias;
        boolean isAliasAction;

        @Override
        public String toString() {
            return "TagAliasBean{" +
                    "action=" + action +
                    ", tags=" + tags +
                    ", alias='" + alias + '\'' +
                    ", isAliasAction=" + isAliasAction +
                    '}';
        }
    }


}
