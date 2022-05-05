# mirai-fleet-amiya 

## 简介

插件功能：使bot扮演角色阿米娅，作为《明日方舟》游戏助手。

## 功能说明

[基础说明](https://github.com/hundun000/mirai-fleet-framework/blob/0.5.1/docs/%E5%8A%9F%E8%83%BD%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E.md)。对于本插件，文档中的`<角色名>`代入为`阿米娅`。

- [微博推送](https://github.com/hundun000/mirai-fleet-framework/blob/0.5.1/docs/starter-functions/WeiboFunction.md)
- [报时与提醒](https://github.com/hundun000/mirai-fleet-framework/blob/0.5.1/docs/starter-functions/ReminderFunction.md)
- [搜音乐](https://github.com/hundun000/mirai-fleet-music/blob/0.1/docs/share-functions/MusicCompositeFunction.md)（似乎频繁发送音乐分享卡片容易被冻结，用户自行斟酌对该功能的权限授予）
- [帮助](https://github.com/hundun000/mirai-fleet-framework/blob/0.5.1/docs/starter-functions/CharacterHelpFunction.md)
- [阿米娅闲聊](./docs/functions/AmiyaChatFunction.md)
- [复读、立刻聊天](https://github.com/hundun000/mirai-fleet-framework/blob/0.5.1/docs/starter-functions/other.md)

依赖服务：mirai框架、微博api

依赖前置插件：[SkikoMirai(v1.0.8)](https://github.com/LaoLittle/SkikoMirai/tree/1.0.8)。用户需要将该版本的前置插件也放入mirai-console的plugins文件夹，并准备好SkikoMirai所需的配置文件等(详见SkikoMirai项目的说明)。

### 声明：一切开发旨在学习，请勿用于非法用途

- 本项目是完全免费且开放源代码的软件，仅供学习和娱乐用途使用
- 鉴于项目的特殊性，开发团队可能在任何时间**停止更新**或**删除项目**。

## 作为mirai-console-plugin使用

[详细说明](https://github.com/hundun000/mirai-fleet-framework/blob/0.5.1/docs/%E4%BD%9C%E4%B8%BAmirai-console-plugin%E4%BD%BF%E7%94%A8.md)

对于本插件，`<特殊权限名>`为`hundun.fleet.amiya.cos:INSTANCE`。

