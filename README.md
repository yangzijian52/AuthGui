# AuthGui

一个为 `AuthMe` 提供图形化登录与注册交互的 Paper 插件，同时面向 Java 版和基岩版玩家。

## 当前版本

- 插件版本：`1.0.2`
- Paper API：`26.1.1.build.20-alpha`
- Java 要求：`25+`

## 主要功能

- Java 玩家使用铁砧输入界面完成登录和注册
- 基岩版玩家可通过 `Floodgate` 表单进行登录和注册
- 未完成认证时可重复弹出界面，引导玩家完成流程
- 基于 `AuthMe` 现有认证能力扩展图形化交互

## 运行要求

- Paper 26.1.x
- Java 25+
- AuthMe
- Floodgate（如需支持基岩版）

## 安装方法

1. 先安装 `AuthMe`
2. 如需支持基岩版，再安装 `Floodgate`
3. 将 `AuthGui` 的 jar 放入 `plugins` 目录
4. 启动服务器并进行登录流程测试

## 命令

- `/openlogin`：手动打开登录或注册界面

权限节点：

- `authgui.use`

## 当前状态

- 当前已完成 26.1 依赖与 Java 25 构建升级
- 当前已确认可生成 `AuthGui-1.0.2.jar`
- 详细升级记录见 [docs/PAPER-26.1-UPGRADE.md](docs/PAPER-26.1-UPGRADE.md)
- 当前暂未确认可直接投入使用，仍处于待测试状态
- 尚未完成 `AuthMe` 与 `Floodgate` 组合环境下的完整服内验证
- 主要原因是 `Floodgate` 还未在当前测试环境中完成版本更新与联调，导致基岩版登录注册链路暂时无法测试
- 已根据 Paper 26.1 运行报错将 `AnvilGUI` 升级到更高版本，修复 Java 版登录界面的兼容风险
- 建议在安装 `AuthMe` 与 `Floodgate` 的完整环境中再做登录、注册与重复弹窗流程测试

## 构建

```bash
mvn clean package
```

构建完成后可在 `target/` 目录获取插件 jar。

## 许可证

MIT
