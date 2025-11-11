package com.tongdaxing.erban.libcommon.base.factory;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p> 标注创建Presenter的注解</p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CreatePresenter {
    Class<? extends AbstractMvpPresenter> value();
}
