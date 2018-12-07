#!/usr/bin/env bash
./gradlew clean
./gradlew assembleDebug
adb remount
adb push /Users/liuguangli/person/SystemUIApk/app/build/outputs/apk/debug/SystemUI.apk system/priv-app/AliSystemUI/AliSystemUI.apk
adb reboot