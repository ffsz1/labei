package com.vslk.lbgx.utils.net;

import android.support.v4.app.FragmentActivity;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hm on 2016/12/29.
 */

public class RxHelper {
    private static DialogManager dialogManager;

    public static <T> ObservableTransformer<ServiceResult<T>, T> handleMainResult() {
        return observable -> observable.compose(io_main()).compose(handleResult());
    }

    public static <T> ObservableTransformer<T,T> handleMainResultTest() {
        return observable -> observable.compose(io_main()).compose(io_main());
    }

    public static <T> ObservableTransformer<ServiceResult<T>, T> handleMainResult(final RxAppCompatActivity activity) {
        return observable -> observable.compose(activity.bindUntilEvent(ActivityEvent.DESTROY)).compose(io_main()).compose(handleResult());
    }

    public static <T> ObservableTransformer<ServiceResult<T>, T> handleMainResult(final RxFragment fragment) {
        return observable -> observable.compose(fragment.bindUntilEvent(FragmentEvent.DESTROY_VIEW)).compose(io_main()).compose(handleResult());
    }
    public static <T> ObservableTransformer<ServiceResult<T>, T> handleMainResult(final RxAppCompatActivity activity,boolean b) {
        return observable -> observable.compose(activity.bindUntilEvent(ActivityEvent.DESTROY)).compose(io_main()).compose(handleResult(activity));
    }

    public static <T> ObservableTransformer<ServiceResult<T>, T> handleMainResult(final RxFragment fragment,boolean b) {
        return observable -> observable.compose(fragment.bindUntilEvent(FragmentEvent.DESTROY_VIEW)).compose(io_main()).compose(handleResult(fragment.getActivity()));
    }


    public static <T> ObservableTransformer<ServiceResult<T>, T> handleResult(FragmentActivity mActivity) {
        if (mActivity instanceof BaseMvpActivity) {
            dialogManager = ((BaseMvpActivity) mActivity).getDialogManager();

        } else {
            dialogManager=((BaseActivity) mActivity).getDialogManager();
        }
        dialogManager.showProgressDialog(mActivity, "请稍后...");
        return handleResult();
    }

    public static <T> ObservableTransformer<ServiceResult<T>, T> handleResult() {
        return observable -> observable.flatMap(tServiceResult -> {
            if (dialogManager!=null){
                dialogManager.dismissDialog();
            }
            if (tServiceResult.isSuccess()) {
                return Observable.just(tServiceResult.getData());
            } else if(tServiceResult.getCode()==1500){
                return Observable.error(new AlreadyOpenExeption(tServiceResult.getErrorMessage()));
            }else {
                return Observable.error(new ServerException(tServiceResult.getErrorMessage()));
            }
        }).doOnError(throwable -> {
            if (dialogManager!=null){
                dialogManager.dismissDialog();
            }
        });
    }

    public static <T> ObservableTransformer<T, T> io_main() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
