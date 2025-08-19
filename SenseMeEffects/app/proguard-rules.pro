# Android 项目混淆规则配置文件
# 默认情况下，这个文件中的规则会被添加到 proguard-android.txt 中指定的规则之后
# 可以通过修改 build.gradle 中的 proguardFiles 指令来编辑包含路径和顺序
#
# 更多详细信息，请参考：
#   http://developer.android.com/guide/developing/tools/proguard.html

#==================================【基础配置】==================================
# 保留源文件属性和行号信息，用于定位崩溃问题
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
# 保留异常表
-keepattributes Exceptions
# 保留内部类
-keepattributes InnerClasses

#  保护R文件和资源相关
-keepclassmembers class **.R$* {
    public static final int *;
}

-keepclassmembers class **.R {
    public static final int *;
}

-keepclassmembers class **.R$* {
    public static final int[] *;
}

-keep class **.R$* { *; }

-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class **.R$* { *; }
-keepclassmembers class * {
    public static final int *;
}
-keep public class com.softsugar.senseme.effects.R$*{
    public static final int *;
}
-keepclassmembers class * {
    public static int getDrawableId(android.content.Context, java.lang.String);
}

#==================================【项目配置】==================================
# 自定义View不混淆
-keep class softsugar.senseme.com.effects.view.widget.** { *; }
-keepclassmembers class softsugar.senseme.com.effects.view.widget.** {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    public *** get*();
}

# 保护实体类的序列化
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 项目实体类不混淆
-keep class softsugar.senseme.com.effects.entity.** { *; }
-keep class softsugar.senseme.com.effects.view.** { *; }
-keep class softsugar.senseme.com.effects.** { *; }
-keep class com.softsugar.library.sdk.entity.** { *; } # 素材拉取相关实体类

# ST Mobile SDK 相关类保持不被混淆
-keep class com.softsugar.stmobile.* { *;}
-keep class com.softsugar.stmobile.model.* { *;}
-keep class com.softsugar.stmobile.params.* { *;}
-keep class com.softsugar.stmobile.engine.* { *;}
-keep class com.softsugar.hardwarebuffer.* { *;}
-keep class com.softsugar.stmobile.sticker_module_types.* { *;}

#==================================【三方库配置】==================================

# ======================= Gson 混淆配置 =======================
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ======================= Retrofit 混淆配置 =======================
# 保留签名信息、内部类和封闭方法
-keepattributes Signature, InnerClasses, EnclosingMethod

# 保留运行时可见注解
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# 保留 Retrofit 接口方法
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-if interface * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation interface <1>

# Retrofit 相关警告忽略
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions

# ======================= RxJava 混淆配置 =======================
# RxJava 3.x
-keep class io.reactivex.rxjava3.** { *; }
-keep class io.reactivex.rxjava3.core.** { *; }
-keep class io.reactivex.rxjava3.internal.** { *; }
-keep class io.reactivex.rxjava3.android.** { *; }
-keep class io.reactivex.rxjava3.schedulers.** { *; }
-keep class io.reactivex.rxjava3.plugins.** { *; }
-keep class io.reactivex.rxjava3.exceptions.** { *; }
-keep class io.reactivex.rxjava3.internal.functions.** { *; }
-keep class io.reactivex.rxjava3.internal.operators.** { *; }
-keep class io.reactivex.rxjava3.internal.observers.** { *; }
-keep class io.reactivex.rxjava3.subjects.** { *; }
-keep class io.reactivex.rxjava3.processors.** { *; }
-keep class io.reactivex.rxjava3.disposables.** { *; }
-keep class io.reactivex.rxjava3.observers.** { *; }

# 保护 Consumer 接口
-keepclassmembers interface io.reactivex.rxjava3.functions.Consumer { *; }
-keepclassmembers interface io.reactivex.rxjava3.functions.BiConsumer { *; }

# 保护 Observable 相关
-keepclassmembers class io.reactivex.rxjava3.subjects.Subject { *; }
-keepclassmembers class io.reactivex.rxjava3.observables.ConnectableObservable { *; }
-keepclassmembers class io.reactivex.rxjava3.internal.operators.observable.* { *; }

# 保护错误处理相关
-keep class io.reactivex.rxjava3.exceptions.** { *; }
-keepclassmembers class io.reactivex.rxjava3.internal.util.ExceptionHelper { *; }
-keepclassmembers class io.reactivex.rxjava3.internal.functions.Functions$* { *; }
-keepclassmembers class io.reactivex.rxjava3.internal.observers.* { *; }
-keepclassmembers class io.reactivex.rxjava3.internal.operators.* { *; }

# 保留泛型
-keepattributes Signature
# 保留异常信息
-keepattributes Exceptions
# 保留源文件和行号
-keepattributes SourceFile,LineNumberTable
# 保留注解
-keepattributes *Annotation*

# RxJava 2.x
-keep class io.reactivex.** { *; }
-keep class io.reactivex.schedulers.** { *; }
-keep class io.reactivex.internal.** { *; }
-keep class io.reactivex.android.** { *; }
-keep class io.reactivex.flowable.** { *; }
-keep class io.reactivex.processors.** { *; }
-keep class io.reactivex.subjects.** { *; }

# 保护所有实体中的字段名称
-keepclassmembers class * {
    @io.reactivex.rxjava3.annotations.** *;
}

# 保护 RxJava 的回调方法
-keepclassmembers class * {
    @io.reactivex.rxjava3.annotations.SchedulerSupport *;
}

# 忽略 RxJava 的警告
-dontwarn io.reactivex.**
-dontwarn io.reactivex.rxjava3.**
-dontwarn rx.**
-dontwarn sun.misc.**

# 保留 Exceptions 相关
-keep class io.reactivex.rxjava3.exceptions.** { *; }
-keepclassmembers class io.reactivex.rxjava3.internal.util.ExceptionHelper { *; }
-keepclassmembers class io.reactivex.rxjava3.internal.functions.Functions$* { *; }

# ======================= BaseRecyclerViewAdapterHelper 混淆配置 =======================
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.viewholder.BaseViewHolder
-keepclassmembers class * extends com.chad.library.adapter.base.BaseQuickAdapter {
    public <init>(int, java.util.List);
    public <init>();
}
-keepclassmembers class * extends com.chad.library.adapter.base.viewholder.BaseViewHolder {
    public <init>(android.view.View);
}

# RecyclerView 相关
-keep class androidx.recyclerview.widget.** { *; }
-keep class androidx.recyclerview.widget.RecyclerView$LayoutManager { *; }
-keepclassmembers class * extends androidx.recyclerview.widget.RecyclerView$ViewHolder {
   public <init>(android.view.View);
}