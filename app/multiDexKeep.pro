-keep class android.support.**{*;}
-keep class butterknife.**{*;}
-keep class com.bumptech.glide.**{*;}
-keep class com.zwh.mvparms.eyepetizer.app.**{*;}
-keep class com.google.gson.**{*;}
-keep class com.jess.**{*;}
-keep class me.jessyan.**{*;}
-keep class com.trello.rxlifecycle2.android.**{*;}
-keep class dagger.**{*;}
-keep class io.reactivex.**{*;}
-keep class io.rx_cache2.**{*;}
-keep class io.victoralbertos.jolyglot.**{*;}
-keep class okhttp3.**{*;}
-keep class okio.**{*;}
-keep class org.simple.eventbus.**{*;}
-keep class timber.**{*;}
-keep class retrofit2.** { *; }
-keep class cn.bmob.** { *; }
-keep class com.chad.** { *; }
-keep class com.tencent.bugly.** { *; }
#tinker multidex keep patterns:
-keep public class * implements com.tencent.tinker.loader.app.ApplicationLifeCycle {
    <init>(...);
    void onBaseContextAttached(android.content.Context);
}

-keep public class * extends com.tencent.tinker.loader.TinkerLoader {
    <init>(...);
}

-keep public class * extends android.app.Application {
     <init>();
     void attachBaseContext(android.content.Context);
}

-keep class com.tencent.tinker.loader.TinkerTestAndroidNClassLoader {
    <init>(...);
}

#your dex.loader patterns here
-keep class tinker.sample.android.app.SampleApplication {
    <init>(...);
}

-keep class com.tencent.tinker.loader.** {
    <init>(...);
}