# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles

#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keep class packagename.** {*;}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保留行号
-keepattributes SourceFile,LineNumberTable

#-dontwarn  #//dontwarn去掉警告
#-dontskipnonpubliclibraryclassmembers
#-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService
-keepclasseswithmembernames class * {
    native <methods>;
}
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#----------------enum-----------------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#----------------Parcelable-----------------
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
    <fields>;
    <methods>;
}

-keep class * implements java.io.Serializable {
    *;
}
##---------------Begin: proguard configuration for Gson ----------
# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepattributes Signature
-keepattributes *Annotation*

#----------------android-----------------
-dontwarn android.**
-keep class android.** { *;}
#----------------v4-----------------
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
#----------------v7-----------------
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *;}

#----------------EventBus事件巴士-----------------
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}


#-------------云信相关的混淆配置------------
-dontwarn com.netease.**
-keep class com.netease.** {*;}
#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}
#-keep class com.tongdaxing.erban.ui.im.chat.** {*;}
-keep class com.vslk.lbgx.im.holder.** {*;}
#-------------云信相关的混淆配置------------


#-------------TakePhoto的混淆配置------------
-keep class com.jph.takephoto.** { *; }
-dontwarn com.jph.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**

#-------------TakePhoto的混淆配置------------



#腾讯崩溃收集
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# glide4.0
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# BaseAdapter
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}


# Ping++ 混淆过滤
-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}
# 支付宝混淆过滤
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}
# 微信或QQ钱包混淆过滤
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

# 银联支付混淆过滤
#-dontwarn  com.unionpay.**
#-keep class com.unionpay.** {*;}
#
## 招行一网通混淆过滤
#-keepclasseswithmembers class cmb.pb.util.CMBKeyboardFunc {
#    public <init>(android.app.Activity);
#    public boolean HandleUrlCall(android.webkit.WebView,java.lang.String);
#    public void callKeyBoardActivity();
#}

# 内部WebView混淆过滤
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class org.json.** {*;}

-dontwarn com.hncx.xxm.base.bindadapter.**
-keep class com.vslk.lbgx.base.bindadapter.** {*;}
# 网络加载
-dontwarn com.tongdaxing.xchat_core.**
-keep class com.tongdaxing.xchat_core.** {*;}

-keepclassmembers class * {
    @com.tongdaxing.xchat_framework.coremanager.CoreEvent <methods>;
}

-keep class com.baidu.bottom.** { *; }
-keep class com.baidu.kirin.** { *; }
-keep class com.baidu.mobstat.** { *; }
-keep class io.agora.** { *; }

# 七牛
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings

# shareSdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

# fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*; }

# retrofit2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# okhttp3
-dontwarn okhttp3.**
#  okio
-dontwarn okio.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# JPush 极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }


-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#网易图片验证码
-keepattributes *Annotation*
-keep public class com.netease.nis.captcha.**{*;}

-keep public class android.webkit.**

-keepattributes SetJavaScriptEnabled
-keepattributes JavascriptInterface

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 安全检测库的bean不混淆
-dontwarn com.hncxco.safetychecker.bean.**
-keep class com.hncxco.safetychecker.bean.**{*;}

-dontwarn com.tongdaxing.xchat_framework.im.**
-keep class com.tongdaxing.xchat_framework.im.**{*;}

# 即构混淆代码
-keep class com.zego.**{*;}
#实体
-keep class com.vslk.lbgx.ui.me.withdraw.**{*;}
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl { *; }


# 头条适配混淆
 -keep class me.jessyan.autosize.** { *; }
 -keep interface me.jessyan.autosize.** { *; }


# 消息
-keep class com.tongdaxing.xchat_core.im.custom.bean.** { *; }
-keep class com.vslk.lbgx.im.holder.** { *; }
-keep class com.vslk.lbgx.room.** { *; }
-keep class com.vslk.lbgx.event.** { *; }

-keepclasseswithmembernames class android.support.design.widget.TabLayout {
    *;
}