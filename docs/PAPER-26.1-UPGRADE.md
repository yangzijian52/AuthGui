# AuthGui Paper 26.1 升级说明

## 概要

本文档记录 `AuthGui` 对 `Paper 26.1` 的兼容升级情况。

- 项目版本：`1.0.1`
- 目标 Paper API：`26.1.1.build.20-alpha`
- 所需 Java 版本：`25+`
- 升级日期：`2026-04-05`

## 本次调整

- `paper-api` 从 `1.21.11-R0.1-SNAPSHOT` 升级到 `26.1.1.build.20-alpha`
- Java 编译目标从 `21` 升级到 `25`
- `plugin.yml` 版本号改为自动读取 Maven 版本
- Java 版暂时移除 `AnvilGUI` 依赖，改为聊天引导输入流程
- 插件说明文档更新为中文并同步新的运行要求

## 重要说明

- 截至 `2026-04-05`，PaperMC Maven 中 `paper-api` 的 `latest` 和 `release` 仍指向 `26.1.1.build.20-alpha`
- 本插件仍使用 `plugin.yml` 作为入口，本次升级不需要切换到 `paper-plugin.yml`
- 当前 Java 版聊天引导仅为临时降级方案，不代表最终设计
- 这样处理的原因是原有 GUI 方案在 `Paper 26.1.1` 下出现运行时兼容问题，继续保留会导致插件报错
- 后续如果找到稳定可用的 GUI 兼容方案，会重新恢复图形登录/注册界面
- 当前已完成本地构建验证，但尚未完成结合 `AuthMe`、`Floodgate` 的完整服内实测
- `Floodgate` 还未在当前环境完成更新与联调，因此基岩版表单流程暂未验证

## 验证建议

1. 使用 `Java 25` 运行 `mvn clean package`
2. 在安装 `AuthMe` 的 `Paper 26.1` 服务端测试 Java 版登录与注册流程
3. 如需支持基岩版，再结合 `Floodgate` 测试表单登录与注册流程
