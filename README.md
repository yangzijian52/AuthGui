# AuthGui

> [!IMPORTANT]
> **本项目已停止维护。**
>
> AuthGui 最初是为离线模式服务器中的玩家登录与注册流程而开发的。作者现已不再计划进行功能更新、版本适配或问题修复。
>
> 建议购买并使用正版 Minecraft，通过官方账号登录，并在服务器中启用正版验证（`online-mode=true`）。感谢大家此前对本项目的关注与使用。

一个为 `AuthMe` 提供登录与注册引导的 Paper 插件，同时兼容 Java 版和基岩版玩家。

## 当前版本

- 插件版本：`1.0.1`
- Paper API：`26.1.1.build.20-alpha`
- Java 要求：`25+`

## 当前状态

- 已完成 `Paper 26.1` 与 `Java 25` 构建升级
- 当前可生成 `AuthGui-1.0.1.jar`
- `Floodgate` 链路尚未完成联调，基岩版流程暂未验证

## 停止维护说明

仓库中的代码与发行文件仅供留档。由于项目已经停止维护，不保证其能够兼容后续版本的 Paper、Java、AuthMe 或 Floodgate，也不建议在新的服务器中继续部署。

当前 Java 版因原有 `AnvilGUI` 方案存在运行时兼容问题，使用聊天引导完成登录与注册；该方案不会再继续更新。

## 主要功能

- Java 玩家当前使用聊天引导方式完成登录和注册
- 基岩版玩家可通过 `Floodgate` 表单进行登录和注册
- 未完成认证时可重复提示玩家完成流程
- 基于 `AuthMe` 现有认证能力扩展更友好的输入体验

## 运行要求

- Paper `26.1.x`
- Java `25+`
- `AuthMe`
- `Floodgate`（如需支持基岩版）

## 安装方法

1. 先安装 `AuthMe`
2. 如需支持基岩版，再安装 `Floodgate`
3. 将 `AuthGui` 的 jar 放入 `plugins` 目录
4. 启动服务器并进行登录流程测试

## 命令

- `/openlogin`：手动打开登录或注册引导
- 权限节点：`authgui.use`

## 构建

```bash
mvn clean package
```

构建完成后可在 `target/` 目录获取插件 jar。

## 许可

MIT
