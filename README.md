# 杂口内核 · ZAKOU KERNEL

> 给地球 Online 加一点粉色 HUD。  
> 一个娱乐向的现实世界视觉特效 App：卡密进入、注入动画、灵动岛、摄像头 HUD、头部锁定、装饰自瞄、FOV 和可拖动数据组件。

**固定卡密：`ZaKou-0816`**  
**TG 频道：`@Zakou0816`**  
**作者 QQ：`2378197792`**

开源协议：[MIT](LICENSE)（第三方姿态模型为 Apache-2.0，见 LICENSE 末尾）  
上传 GitHub 的完整步骤：[GITHUB-UPLOAD.md](GITHUB-UPLOAD.md)  
安全边界说明：[SECURITY.md](SECURITY.md)  
Telegram 群简介 / 公告 / 欢迎语：[TG-TEXTS.md](TG-TEXTS.md)

---

## 1. 已经打包成 APK

直接安装根目录的：

```text
ZakouKernel-v1.0.0.apk
```

- 包名：`com.zakou.kernel`
- 版本：1.0.0（初始版）
- 大小：约 12 MB（内置离线姿态模型与 WASM）
- 最低系统：Android 7.0（minSdk 24）
- 目标系统：Android 14（targetSdk 34）
- SHA-256：`A293DBD9D894E2C1F5E715B3A6FFDBA7645BF2A3EEA4BCE80DADBE4CDF3E1620`
- 已使用 APK Signature Scheme v2 / v3 签名

APK 内置完整页面，不依赖网站；摄像头页面运行在 `https://appassets.androidplatform.net` 安全来源下，`getUserMedia` 可以正常申请摄像头。

### 安装

1. 把 `ZakouKernel-v1.0.0.apk` 发到 Android 手机；
2. 点击安装，允许“安装未知应用”；
3. 打开“杂口内核”，点击公告里的“查看卡密”，复制或填入 `ZaKou-0816`；
4. 点“注入内核”，动画结束后会自动进入视觉 HUD；允许摄像头。

如果之前装过测试版或自己构建的版本，提示“签名不一致”时先卸载旧版，再安装这个版本。

> 这是 Android APK。iPhone 不能直接安装 APK，请使用下面的 PWA 方式。

---

## 2. 更新日志

**V1.0.0 · 初始版**

- 卡密验证、启动次数、注入演出、工作状态。
- 摄像头 HUD：方框透视、头部透视、骨骼、射线、距离、雷达、人数 UI。
- 手写内核注入动画：mmap / 模块加载 / IAT patch / 状态日志逐行写入。
- 注入完成后自动打开视觉 HUD，并自动切换/锁定横屏。
- 手机主页面保持竖屏；平板自动检测后使用横屏。关闭 HUD 后手机会恢复竖屏。
- 卡密页加入版本检测：读取 `update.json`，显示当前版本与最新版本；发现新版本时显示下载按钮和本次更新日志。
- 首页灵动岛默认在屏幕顶部居中，可拖动并记忆位置；点一下打开功能菜单。
- HUD 顶部同样使用灵动岛作为唯一菜单入口，移除了左侧 `CTRL` 按钮和键盘快捷键。
- 手机 / 平板自动检测：设置页显示品牌、型号、Android 版本、Linux 内核、WebView、屏幕和密度。
- **姿态模型已内置**：不再依赖 jsDelivr / Google CDN，离线也能识别人体。
- 低配模式 / 流畅优先：降低检测频率、关闭姿态模型、限制渲染帧率。
- 抗抖滤波：镜头大幅移动时清空动态框，修复“没人也一大堆框”的问题。
- 装饰自瞄：FOV 开关、大小调节、彩虹循环 / 自定义颜色。
- 可拖动 FPS / PING / 本地时间组件，位置自动保存。
- 首页移除视觉模块，新增开发者标识。

遇到问题请到 TG 频道 **@Zakou0816** 或作者 QQ **2378197792** 反馈。

---

## 3. 它是什么

这个 App 会把摄像头画面当作“地球 Online”的游戏画面，在本机绘制：

- 常规方框 / 角落方框两种样式
- 头部透视：圆圈锁定头部
- 射线追踪
- 骨骼描边
- 距离估算（米）
- 空间雷达（方位 + 距离）
- 右上角人数 UI
- 装饰自瞄：FOV 圈、头部引导线、`LOCK` 标记
- 首页 / HUD 顶部灵动岛呼出功能菜单
- 手机 / 平板检测与横屏自适应布局
- 低配模式、抗抖滤波与动态框过滤
- HUD 数据组件：FPS、PING、本地时间，可拖动
- 演示训练场（没有摄像头权限也能看完整效果）

所有“注入内核 / 工作状态 / 内核 / 雷达 / 自瞄”都是界面演出。  
**不会注入系统、不会 Hook 其他 App、不会修改游戏、不会录屏、不会上传画面、不做人脸身份识别。**

---

## 4. 卡密与启动次数

- 固定卡密：`ZaKou-0816`（区分大小写，包含短横线）
- 卡密页上方的“查看卡密”弹窗里会显示，可一键复制或直接填入。
- 验证只在本机进行，没有服务器。
- 输错会提示“卡密错误”；输对会播放内核载入动画。
- 每次冷启动通过验证后，“启动次数”+1，保存在本机 `localStorage`。
- 清除 App 数据 / 点设置里的“清除本机数据”会重置次数、HUD 设置和组件位置。

> 因为是纯前端 + 固定卡密，懂技术的人查看源码就能找到卡密。它只是氛围门槛，不是安全措施。

---

## 5. 使用流程

1. **卡密页**：
   - 先看“更新日志”和 TG / QQ 反馈方式；
   - 点“查看卡密”弹窗，复制 `ZaKou-0816` 或点“填入卡密”；
   - 点“验证并载入”。
2. **控制台**：
   - 顶部是灵动岛，点一下（或右侧 `^`）打开功能菜单；岛可以拖动，位置会记忆；
   - 查看“运行游戏：地球 Online”、启动次数；
   - 点 **注入内核**，会播放内核写入日志，完成后**自动打开视觉 HUD**并切换横屏；
   - 之后再点 **进入视觉 HUD** 也会直接进入。
3. **视觉 HUD**：
   - 首次进入会申请摄像头；拒绝后可点“进入演示模式”；
   - 手机关闭 HUD 后主页面恢复竖屏；平板保持横屏；
   - 顶部灵动岛是唯一的菜单入口；没有键盘快捷键；
   - 菜单内可开关：方框、头部、射线、骨骼、距离、雷达、人数、装饰自瞄、FOV、低配模式；
   - 方框样式：**常规 / 角落**；
   - 右下角是雷达，右上角是人数；
   - 顶部数据组件显示 FPS / PING / 本地时间，按住可拖动；
   - 菜单底部提供切换演示 / 切换镜头 / 载入姿态 / 关闭 HUD。
4. **设置**：
   - 授权信息、启动次数；
   - 设备检测：品牌、型号、系统、Linux 内核、WebView、屏幕；
   - 音效 / 触感；
   - 安装到主屏幕（PWA）、分享；
   - 退出授权、清除本机数据。

---

## 6. 功能控制中心

点击首页或 HUD 顶部灵动岛，右侧 `^` 会打开功能菜单。HUD 没有左侧 `CTRL` 按钮，也没有键盘快捷键；移动端只用点按和拖动。

可以集中控制：

- 方框透视 / 头部透视 / 射线 / 骨骼 / 距离 / 空间雷达 / 人数 UI
- 方框样式：常规 / 角落
- 装饰自瞄开关
- FOV 圈开关
- FOV 大小（80–360 px）
- FOV 颜色：彩虹循环 / 自定义取色
- FPS / PING / 时间组件开关

> “装饰自瞄”只绘制 FOV 圈和头部引导线，不会控制准星，也不会修改游戏画面。  
> PING 是本地模拟值，仅作界面氛围。

### 版本检测与更新

卡密页的更新日志上方有版本检测条：

- 当前版本：`V1.0.0`
- 自动读取 `update.json` 的 `latestVersion`、`downloadUrl` 和 `changelog`
- 已是最新：显示“已是最新版本”，可点“重新检测”
- 发现新版本：显示“发现新版本 Vx.x.x”、下载按钮和本次更新日志
- 下载通过系统浏览器打开，不会在应用内静默安装

> 仓库里的 `update.json` 是本地演示源，默认就是 `1.0.0`。发布新版本时把它一起更新到托管地址；如果要做真正跨 APK 的在线检测，把 `index.html` 里的 `UPDATE_FEED_URL` 改成你的 HTTPS JSON 地址即可，格式与 `update.json` 相同。

### 拖动数据组件

进入 HUD 后，按住顶部的 **FPS / PING / 时间** 组件拖动到任意位置，松手自动保存。  
“恢复默认设置”会同时重置组件位置。

---

## 7. 识别引擎

应用有三条视觉路径，会自动选择：

| 引擎 | 触发条件 | 特点 |
| --- | --- | --- |
| **POSE** | 摄像头可用 + 本地 MediaPipe 姿态模型加载成功 | 人体骨架、头部和身体框，推荐 |
| **LIGHT** | 模型仍在加载 / 低配模式 / 加载失败 | 动态捕捉 + 可选浏览器人脸检测，完全本机 |
| **DEMO** | 点“进入演示模式” | 程序生成的训练场，不调用摄像头 |

POSE 模型和 WASM 已经放在 `vendor/` 并打包进 APK：

- `vendor/vision_bundle.mjs`
- `vendor/wasm/vision_wasm_*.{js,wasm}`
- `vendor/pose_landmarker_lite.task`

**不再从 CDN 拉取**。首次进入 HUD 需要几秒钟把模型读进内存，HUD 左上角会先显示 `POSE` 或 `LOADING`；加载完成后显示 `POSE`。加载失败会自动留在 LIGHT 模式；HUD 菜单里可以点“载入姿态模型”重试。

> 低配模式会主动关闭姿态模型，只保留轻量动态捕捉。

**距离是估算值**：根据人体在画面中的高度 / 肩宽和假设焦距计算，不是激光测距，误差会随镜头、姿势、裁剪变化。

---

## 8. 网页版 / iPhone

同一套页面也可以作为 PWA 使用：

1. 把整个 `zakou-kernel` 文件夹上传到任意静态网站托管
   （Cloudflare Pages、GitHub Pages、Vercel、Netlify 等）；
2. 手机浏览器打开网址；
3. 点击“查看卡密”，使用 `ZaKou-0816`；
4. 点“启动视觉 HUD”，允许摄像头；
5. 浏览器菜单 → **添加到主屏幕**。

> 手机浏览器调用摄像头要求 **HTTPS**（或 localhost）。这是浏览器的安全限制，不是应用在联网。

也可以直接双击 `index.html` 看界面；部分浏览器会因 `file://` 限制摄像头，此时用“演示模式”。

本地预览：

```bash
python -m http.server 8080
# 打开 http://localhost:8080
```

---

## 9. 自己构建 APK

### 方式 A：Android Studio 工程

`android/` 是完整的 Android Studio 工程（Kotlin + WebView + WebViewAssetLoader）。

1. 安装 Android Studio（自带 JDK 17）和 Android SDK 34；
2. 打开 `android/` 文件夹，等待 Gradle Sync；
3. `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`；
4. debug APK 一般在 `android/app/build/outputs/apk/debug/app-debug.apk`。

Windows 也可以双击：

```text
android\build-apk.bat
```

脚本会自动找 Android Studio 的 JDK / SDK；如果系统没有 Gradle，会第一次下载 Gradle 8.7，再执行 `assembleDebug`。

> 仓库没有内置 `gradle-wrapper.jar`。Android Studio 提示创建 / 下载 Gradle 时选择 **Gradle 8.7 或更高**；如果提示找不到 wrapper，在 `Settings → Build, Execution, Deployment → Build Tools → Gradle` 里改用本地 Gradle 8.7，或直接双击 `build-apk.bat`。  
> 自己重新构建的 debug APK 会使用另一套签名；如果手机上已经装了根目录的正式版，更新前需要先卸载旧版。

### 方式 B：轻量源码

`apk-src/` 是制作本次 APK 用的 Java + WebView 源码（不依赖 AndroidX），包含：

- `AndroidManifest.xml`
- `MainActivity.java`
- `res/drawable-nodpi/ic_launcher.png`
- `assets/`（页面与图标）

最终 APK 已包含原生启动画面、摄像头权限、返回键处理。

---

## 10. 图标

你给的粉发异色瞳原图已经内置：

- `icon-user.jpg`：卡密页、主页左上角、设置页头像
- `icon-512.png` / `icon-192.png`：PWA 图标
- `icon-180.png`：iPhone 主屏幕图标
- `icon-maskable.png`：maskable 图标
- `icon.svg`：备用矢量重绘版

APK 的启动图标也是从原图裁切生成的 PNG。

---

## 11. 文件结构

```text
zakou-kernel/
├─ ZakouKernel-v1.0.0.apk  # 可直接安装的 Android APK
├─ ZakouKernel-v1.0.0.apk.sha256
├─ index.html              # 单文件 App
├─ icon-user.jpg
├─ icon-192.png
├─ icon-512.png
├─ icon-maskable.png
├─ icon-180.png
├─ icon.svg
├─ manifest.webmanifest
├─ sw.js
├─ update.json             # 版本检测 / 下载地址 / 更新日志
├─ vendor/                 # 内置 MediaPipe 姿态模型与 WASM（离线识别）
├─ README.md
├─ screenshots/            # 新版界面预览
├─ android/                # Android Studio 构建工程
│  ├─ build-apk.bat
│  └─ app/...
└─ apk-src/                # 本次 APK 的轻量 Java/WebView 源码
   ├─ AndroidManifest.xml
   ├─ MainActivity.java
   ├─ res/
   └─ assets/
```

---

## 12. 隐私与边界

- 摄像头画面只进入本机 `<video>` 和 `<canvas>` 处理。
- 不保存照片 / 视频，不建立人脸库，不上传任何画面。
- 不提供“隐身 / 偷拍 / 后台运行 / 系统注入”能力。
- 固定卡密是本地校验，没有服务器，无法防止源码查看。
- 请只在他人知情、同意的场合拍摄。不要用这个玩具跟踪、偷拍、骚扰任何人。

---

## 13. 常见问题

**Q：卡密明明对的，还是提示错误？**  
A：本版本卡密是 `ZaKou-0816`，注意大小写和中间的短横线。可以直接在公告里点“查看卡密”一键填入。

**Q：摄像头黑屏 / 打不开？**  
A：在系统设置里允许“杂口内核”使用摄像头；关闭其他占用相机的 App。网页版需要 `https://`（或 localhost）；iPhone 需要 iOS 14.3+ 的 Safari。仍不行就用演示模式。

**Q：没有框？**  
A：先看 HUD 左上角引擎标记。`POSE` 表示人体模型已加载，正常会出框；`LIGHT` 只捕捉运动，人站着不动时可能没有框；`LOADING` 表示还在读内置模型，等几秒；`DEMO` 是演示场。低配模式会关闭姿态模型，需要关掉低配模式再重载。镜头大幅移动时抗抖滤波会清空动态框约 0.9 秒。

**Q：为什么姿态模型不是从网上下载？**  
A：为了国内网络和离线场景，`vision_bundle.mjs`、WASM 和 `pose_landmarker_lite.task` 已经打包在 APK 的 `vendor/` 里。APK 因此约 12 MB，但第一次加载后人体识别不依赖 CDN。

**Q：手机低配、容易卡？**  
A：点灵动岛打开功能菜单，打开 **低配模式 · 流畅优先**。它会降低动态检测频率、关闭姿态模型和人脸检测，并把 HUD 渲染限制在约 30 FPS。再卡可以在设置里关闭音效 / 触感。

**Q：灵动岛能拖吗？**  
A：可以。首页灵动岛默认在屏幕顶部居中，按住拖动即可换位置，松手自动保存；点一下（不拖动）打开菜单。注入后会自动横屏。

**Q：手机信息在哪看？**  
A：设置页的 **设备检测** 卡片会显示品牌、型号、Android 版本、Linux 内核、WebView、屏幕尺寸和密度。

**Q：FOV / 自瞄是真的吗？**  
A：不是。它们只是 HUD 上的装饰绘制，不会移动准星、不会修改游戏，也不读取其他 App 的数据。

**Q：PING 是真实网络延迟吗？**  
A：不是。这是本地模拟值，用于界面氛围；应用没有游戏服务器。

**Q：数据组件拖不动？**  
A：确认“FPS / PING / 时间”开关已打开，然后在 HUD 里按住组件拖动；松手保存。点“恢复默认设置”可重置位置。

**Q：能后台悬浮在其他 App 上吗？**  
A：不能。这个版本是应用内 HUD，不做系统级悬浮窗，也不碰其他 App。

**Q：能直接发给朋友吗？**  
A：可以，直接发 `ZakouKernel-v1.0.0.apk`。对方是 Android 手机，允许安装未知应用即可；iPhone 请发部署后的 PWA 链接。

**Q：安装时提示“应用未安装 / 签名不一致”？**  
A：手机上之前装过测试版或自己构建的版本，先卸载旧版，再安装这个 APK。

---

## 14. 制作说明

- 名称：杂口内核 / ZAKOU KERNEL
- 版本：1.0.0（初始版）
- 类型：娱乐向现实 HUD / Android APK + PWA
- 卡密：`ZaKou-0816`
- TG：`@Zakou0816`
- 作者 QQ：`2378197792`
- 开发模型：**deepseek-V4.1-Flash**
- 视觉：粉色赛博 + 玻璃拟态 + 二次元
- 图标：使用你提供的粉发异色瞳原图
- 无服务器、无账号、无上传、无人脸身份识别

玩得开心，Player One。
