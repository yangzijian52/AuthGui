# AuthGui Paper 26.1 升级说明

## 概要

本文档记录 `AuthGui` 对 Paper 26.1 的兼容升级情况。

- 项目版本：`1.0.1`
- 目标 Paper API：`26.1.1.build.20-alpha`
- 所需 Java 版本：`25+`
- 升级日期：`2026-04-05`

## 本次调整

- `paper-api` 从 `1.21.11-R0.1-SNAPSHOT` 升级到 `26.1.1.build.20-alpha`
- Java 编译目标从 `21` 升级到 `25`
- `plugin.yml` 版本号改为自动读取 Maven 版本
- 插件说明文档更新为中文并同步新的运行要求

## 说明

- 截至 `2026-04-05`，PaperMC Maven 中 `paper-api` 的 `latest` 与 `release` 仍指向 `26.1.1.build.20-alpha`
- 本插件仍使用 `plugin.yml` 作为入口，本次升级不需要切换到 `paper-plugin.yml`
- 本次先以依赖、Java 版本与文档同步为主
- `maven-shade-plugin` 已升级到 `3.6.2`，以支持 Java 25 字节码的重定位打包
- 当前已完成本地构建验证，但尚未完成结合 `AuthMe`、`Floodgate` 的服内实测
- 当前暂时不能确认插件已可正常投入使用
- 未完成测试的主要原因是 `Floodgate` 还没有在当前环境完成更新与联调，基岩版表单相关流程无法验证

## 验证建议

1. 使用 Java 25 运行 `mvn package`。
2. 在装有 `AuthMe` 的 Paper 26.1 服务端测试 Java 版登录与注册流程。
3. 如需支持基岩版，再结合 `Floodgate` 测试表单登录与注册流程。
