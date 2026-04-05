# AuthGui

一个为 `AuthMe` 提供登录与注册引导的 Paper 插件，同时兼容 Java 版和基岩版玩家。

## 当前版本

- 插件版本：`1.0.1`
- Paper API：`26.1.1.build.20-alpha`
- Java 要求：`25+`

## 主要功能

- Java 玩家使用聊天引导方式完成登录和注册
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

## 当前状态

- 已完成 `Paper 26.1` 与 `Java 25` 构建升级
- 当前可生成 `AuthGui-1.0.1.jar`
- Java 版已移除 `AnvilGUI` 依赖，改为稳定的聊天引导输入流程
- 详细升级记录见 [docs/PAPER-26.1-UPGRADE.md](docs/PAPER-26.1-UPGRADE.md)
- 当前仍需继续服内测试
- `Floodgate` 链路尚未完成联调，基岩版流程暂未验证

## 构建

```bash
mvn clean package
```

构建完成后可在 `target/` 目录获取插件 jar。

## 许可

MIT
