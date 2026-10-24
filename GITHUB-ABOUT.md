# GitHub 仓库信息 / About

## 英文软件名

**ZAKOU KERNEL**

副标题 / Tagline：

**Earth Online Reality HUD**

建议仓库名：

```text
zakou-kernel
```

GitHub 显示名称可以写：

```text
ZAKOU KERNEL — Earth Online Reality HUD
```

---

## GitHub 仓库 About（直接填）

```text
Entertainment-only Earth Online camera HUD for Android. Boxes, head lock, skeleton, rays, radar and a draggable island. Offline MediaPipe pose model. No game injection, no screen upload.
```

中文备选：

```text
娱乐向 Android 现实 HUD：摄像头方框、头部锁定、骨架、射线、雷达、可拖动灵动岛；内置离线人体模型，不注入游戏、不上传画面。
```

---

## Topics（标签）

```text
android
webview
computer-vision
mediapipe
pose-estimation
hud
camera
entertainment
javascript
open-source
```

---

## 仓库简介 / English Overview

```markdown
# ZAKOU KERNEL

### Earth Online Reality HUD

**ZAKOU KERNEL** is an open-source, entertainment-only camera HUD for Android.
It turns the real world into a playful "Earth Online" interface with boxes, head
locks, skeletons, rays, distance labels, a radar and a draggable dynamic island.

> **This is not a real game cheat.**
> It does not inject, hook, patch or modify any other app or game. It does not
> read other apps' memory or screen. Camera frames are processed only on-device
> and are never recorded or uploaded.

### Features

- Camera-based HUD: bounding boxes, head lock, skeleton, rays, distance, radar
- Draggable dynamic island as the single menu entry
- Offline MediaPipe pose model bundled in the APK
- Automatic visual HUD after "injection" animation
- Phone portrait home / landscape HUD; tablet landscape auto-detection
- Low-power mode and anti-shake filtering
- Device info: brand, model, Android version, Linux kernel, WebView, screen
- Decorative FOV / aim overlay (visual only)
- Draggable FPS / PING / local-time widget
- In-app version check with update notes and download link

### Tech stack

- Single-file HTML/CSS/JavaScript front end
- MediaPipe Tasks Vision (offline WASM + pose_landmarker_lite)
- Android WebView shell (Java + Kotlin/Android Studio project)
- PWA manifest + service worker

### Build

- Lightweight Java shell: `apk-src/`
- Android Studio project: `android/`
- Static web preview: serve the repository root over HTTP

### License

MIT. Bundled MediaPipe files are Apache-2.0; see `LICENSE`.

### Disclaimer

This project is for entertainment and educational purposes only. Do not use it
to record, track, harass or expose other people without consent. Do not sell the
APK or present it as a real cheat.
```

---

## Release 标题和说明

**Release title：**

```text
ZAKOU KERNEL V1.0.0 — Earth Online Reality HUD
```

**Release description：**

```text
ZAKOU KERNEL V1.0.0 is the first public release.

Highlights:
- Auto-open the visual HUD after the injection animation
- Phone portrait home / landscape HUD; tablet landscape auto-detection
- Draggable dynamic island with tap-to-open menu
- Offline MediaPipe pose model bundled (no CDN required)
- Phone/tablet info: brand, model, Android, kernel, WebView, screen
- Low-power mode + anti-shake filtering
- In-app version check and update notes
- Decorative FOV / aim overlay

This is an entertainment HUD, not a real game cheat. No injection, no hooks,
no screen upload. Camera frames stay on-device.

APK: attach ZakouKernel-v1.0.0.apk below.
SHA-256: A293DBD9D894E2C1F5E715B3A6FFDBA7645BF2A3EEA4BCE80DADBE4CDF3E1620
```
