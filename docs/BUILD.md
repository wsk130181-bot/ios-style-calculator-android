# Build Guide / 构建指南

This document describes how to build and sign the iOS Calculator for Android app.

本文档描述如何构建和签名 iOS Calculator for Android 应用。

---

## Prerequisites / 前置条件

- **JDK 17** or later
- **Android SDK** Platform 34, Build-Tools 34.0.0
- **Gradle 8.7** (or use the Gradle wrapper if available)

## Environment Variables / 环境变量

```bash
export JAVA_HOME=/path/to/jdk-17
export ANDROID_HOME=/path/to/AndroidSDK
export ANDROID_SDK_ROOT=/path/to/AndroidSDK
```

---

## Build / 构建

### Debug Build / Debug 构建

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build / Release 构建

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Clean Build / 清理构建

```bash
./gradlew clean
./gradlew assembleRelease
```

---

## Signing / 签名

To sign the release APK, you need a keystore file. Create one if you don't have it:

```bash
keytool -genkeypair -v \
  -keystore ioscalc.keystore \
  -alias ioscalc \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Sign the APK using `apksigner`:

```bash
$ANDROID_HOME/build-tools/34.0.0/apksigner sign \
  --ks ioscalc.keystore \
  --ks-key-alias ioscalc \
  --ks-pass pass:YOUR_STORE_PASSWORD \
  --key-pass pass:YOUR_KEY_PASSWORD \
  --out iOSCalculator-signed.apk \
  app/build/outputs/apk/release/app-release-unsigned.apk
```

Verify the signature:

```bash
$ANDROID_HOME/build-tools/34.0.0/apksigner verify --print-certs iOSCalculator-signed.apk
```

---

## Install / 安装

Install to a connected device or emulator:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or install the signed release APK:

```bash
adb install -r iOSCalculator-signed.apk
```

---

## Troubleshooting / 故障排除

### Chinese Path Issue / 中文路径问题

**Problem:** Build fails with D8 error.
**Solution:** Build from a pure-English path. Android's D8 compiler crashes on paths containing Chinese characters.

```bash
# Copy project to an English path first
cp -r ios-style-calculator-android /tmp/build/ioscalc
cd /tmp/build/ioscalc
./gradlew assembleRelease
```

### Gradle Sync Failed / Gradle 同步失败

**Problem:** Gradle cannot download dependencies.
**Solution:** The `settings.gradle.kts` is configured with Aliyun mirrors for China. If you're outside China, you may remove the Aliyun mirror entries and keep only `google()` and `mavenCentral()`.

### Build Cache Issues / 构建缓存问题

**Problem:** Stale build cache causes unexpected errors.
**Solution:** Clean the build before rebuilding:

```bash
./gradlew clean
rm -rf .gradle app/build build
```
