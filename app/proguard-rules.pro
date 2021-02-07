# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# 引用的Jar包不能混淆
# SFTP上传下载需要使用的Jar
-libraryjars libs/jsch-0.1.53.jar
-dontwarn com.jcraft.jsch.**
-keep class com.jcraft.jsch.** {*;}

# FTP上传下载需要使用的Jar
-libraryjars libs/commons-net-3.3.jar
-dontwarn org.apache.commons.net.ftp.**
-keep class org.apache.commons.net.ftp.** {*;}

# 调用WebService需要使用的Jar
-libraryjars libs/ksoap2-android-assembly-3.6.4-jar-with-dependencies.jar
-dontwarn org.ksoap2.serialization.**
-dontwarn org.ksoap2.transport.**
-keep class org.ksoap2.serialization.** {*;}
-keep class org.ksoap2.transport.** {*;}

# androidx包使用混淆
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

# Fastjson 混淆配置
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** {*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** {*;}

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