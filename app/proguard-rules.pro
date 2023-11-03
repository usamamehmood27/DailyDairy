## Add project specific ProGuard rules here.
## You can control the set of applied configuration files using the
## proguardFiles setting in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
#
## Uncomment this to preserve the line number information for
## debugging stack traces.
-keepattributes SourceFile,LineNumberTable
#-optimizationpasses 5
#-allowaccessmodification
#
-keep class com.akexorcist.localizationactivity.** { *; }
-dontwarn com.akexorcist.localizationactivity.**
# Keep methods in LanguageFragment class
-keep class com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.LanguageFragment {
    void listOfCountries(java.util.List);
}

# If the listOfCountries method is public, use this rule instead
-keepclassmembers class  com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.LanguageFragment {
    public void listOfCountries(java.util.List);
}

#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
#-keep class com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.**{ *; }
#-keep class com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.ModelClasLanguges.*{ *; }
#
## If you keep the line number information, uncomment this to
## hide the original source file name.
##-renamesourcefileattribute SourceFile
#
#-keep class androidx.appcompat.widget.** { *; }
#-keep class com.squareup.okhttp.** { *; }
#-keep interface com.squareup.okhttp.** { *; }
#
#-dontwarn com.squareup.okhttp.**
#-dontwarn okio.**
#
#
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }
#
#-dontwarn okhttp3.**
#
#
# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.facebook.infer.annotation.Nullsafe$Mode
-dontwarn com.facebook.infer.annotation.Nullsafe
-dontwarn com.google.android.gms.maps.GoogleMap$SnapshotReadyCallback
-dontwarn com.google.android.gms.maps.GoogleMap
-dontwarn com.google.android.gms.maps.MapFragment
-dontwarn com.google.android.gms.maps.MapView
-dontwarn com.google.android.gms.maps.OnMapReadyCallback
-dontwarn com.google.android.gms.maps.SupportMapFragment
-dontwarn io.flutter.embedding.android.FlutterSurfaceView
-dontwarn io.flutter.embedding.engine.FlutterJNI
-dontwarn io.flutter.embedding.engine.renderer.FlutterRenderer
-dontwarn io.flutter.view.FlutterNativeView
-dontwarn io.flutter.view.FlutterView
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
#
#
#
