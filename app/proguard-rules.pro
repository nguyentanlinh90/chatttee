# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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



-dontwarn com.appsflyer.**
-dontwarn com.sun.activation.**
-dontwarn net.bytebuddy.**
-dontwarn org.glassfish.**
-dontwarn org.mockito.**
-dontwarn javassist.**
-dontwarn jersey.**
-dontwarn okhttp3.**
-dontwarn android.**
-dontwarn rx.**


-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
#    rx.internal.util.atomic.LinkedQueueNode producerNode;
#}

#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
#    rx.internal.util.atomic.LinkedQueueNode consumerNode;
#}

# -dontwarn sun.misc.Unsafe


######### KEEP ANDROID SUPPORT V7 AND DESIGN
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }


#-keep class com.google.** { *; }
#-keep class com.github.** { *; }
#-keep class org.apache.** { *; }
#-keep class com.android.** { *; }
#-keep class junit.** { *; }
#-keep class org.mockito.** { *; }


#-keep class android.support.v7.widget.RecyclerView { *; }
#-keep class android.view.** {*;}
#-keep class com.teecoin.model.** {*;}
#-keep class core.view.** {*;}

#-keep class * extends core.view.RecycleAdapter {*;}
-keep class * extends core.view.ItemViewHolder {*;}

#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#    public static *** w(...);
#    public static *** i(...);
#    public static *** e(...);
#}


#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#-keepattributes *Annotation*
#-keepattributes Signature
#-keepattributes SourceFile,LineNumberTable
#-keepattributes Exceptions,InnerClasses,Signature

#-keep class au.com.flightcentre.fragment.** { *; }

# Preserve the special static methods that are required in all enumeration classes.
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}

#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}

#-keep class * implements android.os.Serializable {
#  public static final android.os.Serializable$Creator *;
#}

#-keepclassmembers class **.R$* {
#    public static <fields>;
#}