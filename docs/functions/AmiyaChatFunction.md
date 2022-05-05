### 阿米娅闲聊模块

#### 【群消息事件】下班

当群消息包含“下班”时触发。根据是否是工作时间（周一至周五9点至17点），bot会做不同回复。

#### 【群消息事件】方舟梗

(1) damedane

当群员发言里包含“damedane”，bot会发送damedane音频。

(2) 海猫all

当群员发言里包含“海猫”和“all”时，bot会发送海猫摸转盘gif。

#### 【群消息事件】戳一戳

(1) 若bot被戳

##### 选择自身被戳时的回应方式

配置方法：

手动编辑（重启后生效）`config\hundun.fleet.amiya\AmiyaChatFunction\NudgeConfig.json`

配置格式：

|值|含义|
|---|---|
|RANDOM_FACE|以随机表情图片回应|
|PATPAT|以摸头图片回应|

配置样例（亦是配置文件自动创建时的默认值）：

```json
{ 
    "SINGLETON": {
        "nudgeReply": "PATPAT"
    }
}
```

##### 以随机表情图片回应

戳bot时，发送的图片为`data\hundun.fleet.amiya\AmiyaChatFunction\selfNudgeFaces`里的随机图片。

##### 以摸头图片回应

戳bot时，以摸头gif为模板，填充戳一戳发起者的qq头像，发送生成的图片。

(2) 若bot以外的群员被戳

戳一戳任一群员，若存在响应配置，bot会发送配置指定的图片。

戳qq号=123456的群员时，若在`data\hundun.fleet.amiya\AmiyaChatFunction\otherNudgeFaces`里有名为`123456.png`的图片，则发送该图片。否则无事发生。

#### 【群消息事件】自定义回复

可自定义若干对触发词和消息内容候选。当群消息包含触发词时触发，发送一次对应的消息内容候选里的随机一条。

配置方法：

手动编辑（重启后生效）`config\hundun.fleet.amiya\AmiyaChatFunction\ListenConfig.json`

配置格式：

触发词：以`|`分割，满足其中任意一项即为触发。

配置样例：

```json
{ 
    "SINGLETON": {
        "listens": {
            "<角色名>可爱|兔兔可爱": [
                "虽然这可能是我一厢情愿的想法，但我希望罗德岛能成为大家的第二个故乡......",
                "嘿嘿，博士，悄悄告诉你一件事——我重新开始练小提琴了。",
                "博士，我们的脚下，是一条漫长的道路......也许这是一次没有终点的旅行，但如果是和您一起，我觉得，非常幸福。"
            ]
        }
    }
}
```

>  -> <角色名>可爱嘿嘿嘿  
>  <- 嘿嘿，博士，悄悄告诉你一件事——我重新开始练小提琴了。  