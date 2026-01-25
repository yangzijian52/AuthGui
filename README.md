# AuthGui 🔐

![Java](https://img.shields.io/badge/Java-21-orange)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green)
![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Velocity-blue)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

**AuthGui** 是一个轻量级且现代化的 Minecraft 服务器登录辅助插件。
它作为 [AuthMeReloaded](https://github.com/AuthMe/AuthMeReloaded) 的扩展，为 **Java版** 和 **基岩版 (Bedrock)** 玩家提供了原生级的图形化交互体验，彻底告别枯燥的聊天栏指令输入。

## ✨ 主要功能 (Features)

### 🖥️ Java版玩家体验
*   **铁砧菜单 (Anvil GUI)**：利用铁砧重命名机制，提供类原生的输入框体验。
*   **无缝输入**：输入框预置处理，玩家打开界面即可直接打字，无需手动删除预设文字。
*   **分步注册**：注册流程分为“输入密码”和“确认密码”两个步骤，逻辑清晰。

### 📱 基岩版玩家体验 (Geyser/Floodgate)
*   **原生表单 (Forms)**：自动检测基岩版玩家，发送手机端原生的输入表单。
*   **一页式注册**：在同一个页面内完成密码输入和确认，符合手机操作习惯。

### 🛡️ 通用特性
*   **防逃逸机制 (Anti-Escape)**：强制未登录玩家停留在交互界面。如果玩家关闭菜单（ESC），系统会自动重新弹出，直到登录成功。
*   **智能流程**：
    *   注册成功后，自动检测 AuthMe 是否已自动登录。
    *   若未自动登录，插件会自动弹出登录框，引导玩家完成最后一步。
*   **异步优化**：所有耗时操作和状态检测均在异步或延迟任务中处理，不卡主线程。

---

## 🛠️ 前置要求 (Requirements)

运行本插件需要以下环境：

1.  **Java 21** (因为本项目基于 Java 21 开发)
2.  **Server Core**: Paper 1.21+ (或其分支 Leaves, Purpur 等)
3.  **AuthMeReloaded**: 必须安装，本插件基于此插件运行。
4.  **Floodgate**: (可选) 如果你需要支持基岩版玩家，必须安装此插件。

---

## 📥 安装与使用 (Installation)

1.  下载 `AuthGui-1.0.jar`。
2.  将文件放入服务器的 `plugins` 文件夹。
3.  确保服务器已安装 `AuthMe` 和 `Floodgate`。
4.  重启服务器。

### 命令与权限

*   `/openlogin` - 手动打开登录/注册界面 (主要用于测试)。
    *   权限: `authgui.use` (默认所有人拥有)

---

## 🏗️ 构建指南 (Build)

如果你想自己修改源码或编译插件，请按照以下步骤操作：

### 环境要求
*   JDK 21
*   Maven 3.8+

### 构建步骤
1.  克隆本仓库：
    ```bash
    git clone https://github.com/yangzijian52/AuthGui.git
    ```
2.  进入目录并打包：
    ```bash
    cd AuthGui
    mvn clean package
    ```
3.  构建成功后，在 `target/` 目录下找到 `AuthGui-1.0.jar`。

> **注意**: 本项目使用了 `maven-shade-plugin` 将 `AnvilGUI` 库打包进插件中，请确保 Maven 能正常访问网络以下载依赖。

---

## 📂 项目结构

```text
com.example.authgui
├── AuthGuiPlugin.java       # 插件主类
├── listener
│   └── JoinListener.java    # 玩家加入事件监听
└── manager
    ├── BedrockManager.java  # 处理基岩版表单逻辑 (Floodgate API)
    └── JavaGuiManager.java  # 处理Java版铁砧逻辑 (AnvilGUI)
```

## 🤝 贡献与反馈
这是一个开源项目，欢迎提交 Issue 或 Pull Request 来改进代码。<br>
*   如果你发现了 Bug，请在 Issues 中反馈。<br>
*   如果你有更好的想法，欢迎 Fork 本项目。<br>

## 📄 开源协议
本项目采用 MIT License 开源。