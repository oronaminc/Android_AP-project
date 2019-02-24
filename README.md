# Android_AP-project
Android_AP-project with krinfra

## Function

1. Camera Activity
2. Memo Activity
3. Google Map Activity
4. IP/PORT/USER Input Activity
5. TCP/IP Socket Communication
6. Customed Grid Album Activity (Made by Recycling View)
7. Work List Activity (Made by ListView)


## ScreenShot
<div>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929543-26246900-3388-11e9-98e1-916c7d99411f.PNG"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929544-26bcff80-3388-11e9-811b-5f81259fa0c7.PNG"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929545-26bcff80-3388-11e9-94ac-746500fd48d5.PNG"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929537-258bd280-3388-11e9-8d1a-7ae47cba8723.PNG"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929538-258bd280-3388-11e9-915b-d03938df1532.PNG"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929539-26246900-3388-11e9-999c-60f934e9494b.PNG"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929540-26246900-3388-11e9-9971-262363231b18.PNG"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/52929542-26246900-3388-11e9-9ed8-ff86b5bb0c48.PNG"/>
</div>

### dependencies
```java
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support:animated-vector-drawable:28.0.0'
    implementation 'com.android.support:support-media-compat:28.0.0'
    implementation 'com.android.support:support-v4:28.0.0'
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation 'com.github.bumptech.glide:glide:4.8.0'
    implementation 'gun0912.ted:tedpermission:2.0.0'
    implementation 'com.android.volley:volley:1.1.1'
    implementation 'com.google.code.gson:gson:2.8.2'
    implementation 'com.google.android.gms:play-services-maps:16.1.0'
    implementation 'com.google.android.gms:play-services-location:16.0.0'
    implementation project(':json-simple-1.1.1')
}
```

### AndroidManifest.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.techtown.ap_project">
    <uses-permission android:name="android.permisson.CAMERA" />
    <uses-feature android:name="android.hardware.camera2" />
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.FLASHLIGHT" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <application
        android:allowBackup="true"
        android:icon="@drawable/logo2"
        android:label="@string/app_name"
        android:noHistory="true"
        android:roundIcon="@drawable/logo2"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="AIzaSyCDhPjj1TzueNtfaFzkOrmMomfBp844SY8" />

        <activity android:name=".GalleryDetailActivity"></activity>
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.test.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
        <activity android:name=".UploadMemoPicture" />
        <activity android:name=".CameraActivity" />
        <activity android:name=".WorkList" />
        <activity
            android:name=".PopupActivity"
            android:theme="@android:style/Theme.Dialog" />
        <activity android:name=".MainScreen" />
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```
