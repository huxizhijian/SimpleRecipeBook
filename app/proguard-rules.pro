# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\wei\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#okhttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }

#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}

#glide配置文件
-keep public class * implements com.bumptech.glide.module.GlideModule

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

#LRecyclerview
-dontwarn com.github.jdsjlzx.**
-keep class com.github.jdsjlzx.**{*;}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
long producerIndex;
long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# gson
-keep class com.google.**{*;}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.Android.model.** { *; }

### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

#自己定义的model不混淆
-dontwarn org.huxizhijian.sdk.**
-keep class org.huxizhijian.sdk.**{*;}
-keep interface org.huxizhijian.sdk.**{*;}

#根据代码提示调整的混淆设置
-dontwarn jp.wasabeef.glide.transformations.gpu.**
-keep class jp.wasabeef.glide.transformations.gpu.**{*;}
-keep interface jp.wasabeef.glide.transformations.gpu.**{*;}

-dontwarn freemarker.**
-keep class freemarker.**{*;}
-keep interface freemarker.**{*;}

#实体类不要混淆
-dontwarn org.huxizhijian.simplerecipebook.bean.**
-dontwarn org.huxizhijian.simplerecipebook.greendao.**
-keep class org.huxizhijian.simplerecipebook.bean.**{*;}
-keep class org.huxizhijian.simplerecipebook.greendao.**{*;}
-keep interface org.huxizhijian.simplerecipebook.bean.**{*;}
-keep interface org.huxizhijian.simplerecipebook.greendao.**{*;}

#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**

#缺省proguard 会检查每一个引用是否正确，但是第三方库里面往往有些不会用到的类，没有正确引用。
#如果不配置的话，系统就会报错。
-dontwarn android.support.v4.**
-dontwarn android.os.**
-keep class android.support.v4.** { *; }        # 保持哪些类不被混淆
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class android.os.**{*;}

-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.widget
-keep public class * extends com.sqlcrypt.database
-keep public class * extends com.sqlcrypt.database.sqlite

-keepclasseswithmembernames class * {     # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {         # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {         # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity { #保持类成员
   public void *(android.view.View);
}

-keepclassmembers enum * {                  # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {    # 保持Parcelable不被混淆
  public static final android.os.Parcelable$Creator *;
}

-optimizationpasses 5                   # 指定代码的压缩级别
-dontusemixedcaseclassnames             # 是否使用大小写混合
-dontpreverify                          # 混淆时是否做预校验
-verbose                                # 混淆时是否记录日志
-dontskipnonpubliclibraryclasses        # 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclassmembers   # 指定不去忽略非公共的库的类的成员

