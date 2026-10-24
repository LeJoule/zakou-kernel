# 上传到 GitHub（公益开源）指南

这个文件夹就是完整源码，可以直接推到 GitHub。

---

## 1. 需要上传的文件

**核心源码（必须）**

| 路径 | 说明 |
| --- | --- |
| `index.html` | 整个 App：卡密页、首页、HUD、菜单、CSS、JS（单文件） |
| `manifest.webmanifest` | PWA 配置 |
| `sw.js` | Service Worker 缓存 |
| `update.json` | 版本检测 / 下载地址 / 更新日志 |
| `vendor/` | 内置 MediaPipe 姿态模型与 WASM（离线识别人体，约 25 MB） |
| `apk-src/` | 生成 APK 的 Java + WebView 源码（Manifest、MainActivity、图标、assets） |
| `android/` | Android Studio 工程（Kotlin + WebViewAssetLoader） |
| `icon-*.png`、`icon-user.jpg`、`icon.svg` | 图标资源 |
| `README.md` | 说明文档 |
| `LICENSE` | MIT 许可证 |
| `SECURITY.md` | 安全边界说明 |
| `.gitignore` | 排除 APK、签名、构建缓存 |

**可选**

- `screenshots/`：界面预览图（作者自己拍的真实截图，可以保留）
- `ZakouKernel-v1.0.0.apk`：发布版安装包。**默认已被 `.gitignore` 忽略**；如果想让别人直接下载 APK，建议改用 GitHub Releases，而不是提交进仓库（仓库会变大、每次改都要重传）。

`vendor/` 大约 25 MB，GitHub 单文件限制 100 MB、仓库建议 < 1 GB，所以直接提交没问题。

---

## 2. 最快上传方式（网页）

1. 打开 https://github.com/new
2. Repository name 填 `zakou-kernel`（或 `ZAKOU-KERNEL`）
3. 选 **Public**，**不要**勾选 “Add a README file”
4. Create repository
5. 在空仓库页面点 **uploading an existing file**
6. 把本文件夹里除以下内容外的所有文件拖进去：
   - `apk-build/`
   - `ZakouKernel-v1.0.0.apk`
   - `ZakouKernel-v1.0.0.apk.idsig`
   - `.sha256`（可选保留，体积很小）
7. Commit changes

> 网页一次拖太多文件可能失败，建议先传 `vendor/`，再传其他文件。更稳的方式是下面的命令行。

---

## 3. 命令行上传（推荐）

在 `zakou-kernel` 文件夹里打开 PowerShell：

```powershell
git init
git add .
git commit -m "杂口内核 V1.0.0 开源：体积检测、灵动岛、离线姿态识别"
git branch -M main
git remote add origin https://github.com/你的用户名/zakou-kernel.git
git push -u origin main
```

如果提示要密码，用 **Personal Access Token** 代替密码：
GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token，勾选 `repo` 权限。

---

## 4. 发布 APK（让别人能下载）

1. 仓库右侧 **Releases** → **Create a new release**
2. Tag 填 `v1.0.0`
3. 标题填 `杂口内核 V1.0.0 初始版`
4. 在描述里写更新日志（可以直接抄 `README.md` 的「更新日志」）
5. 把 `ZakouKernel-v1.0.0.apk` 拖到 Attachments 区域
6. Publish release

然后把 `update.json` 里的地址改成 Release 里的 APK 链接：

```json
{
  "latestVersion": "1.0.0",
  "downloadUrl": "https://github.com/你的用户名/zakou-kernel/releases/download/v1.0.0/ZakouKernel-v1.0.0.apk",
  "publishedAt": "2026-10-24",
  "changelog": [
    "内置离线姿态模型，离线也能识别人体",
    "注入后自动打开视觉 HUD",
    "手机主页面竖屏，平板自动横屏",
    "灵动岛可拖动、点按打开功能菜单",
    "低配模式与抗抖滤波，减少假框"
  ]
}
```

> 注意：APK 里已经打包了一份 `update.json`。要让**已安装的旧版本**也能在线检测到新版本，需要把 `index.html` 里的这行改成你的在线地址并重新打包：
>
> ```js
> const UPDATE_FEED_URL = 'https://raw.githubusercontent.com/你的用户名/zakou-kernel/main/update.json';
> ```
>
> 用 GitHub Raw 时要注意 CORS：`raw.githubusercontent.com` 支持跨域读取 JSON，可以直接用。

---

## 5. 网页版部署（可选）

`index.html` 是纯静态单页，上传到任意静态托管即可：

- GitHub Pages：仓库 Settings → Pages → Source 选 `main` / `root`
- 或 Cloudflare Pages / Vercel / Netlify

浏览器调用摄像头需要 **HTTPS**（GitHub Pages 自带）。`vendor/` 也要一起上传，否则姿态模型加载不到，只剩轻量的动态捕捉。

---

## 6. 开源注意

- 卡密 `ZaKou-0816` 是写死的本地校验，开源后所有人都能看到，这是预期的（它只是氛围门槛）。
- 请不要把 **你的 keystore**（`*.keystore` / `*.jks`）传到 GitHub，`.gitignore` 已经排除。
- 姿态模型来自 Google MediaPipe，遵循 **Apache-2.0**，`LICENSE` 里已经注明。
- 项目本身用 **MIT** 协议，别人可以自由使用、修改、二次分发。
- 建议在仓库简介里注明：**娱乐 HUD，不是真实外挂，不上传画面**。
