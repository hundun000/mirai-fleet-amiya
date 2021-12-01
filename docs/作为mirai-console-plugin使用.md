### 1. 环境准备

- java 11
- [mirai-console](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md)
- 确保可以 [在聊天环境执行指令](https://github.com/project-mirai/chat-command)  

### 2. 下载本项目制品

- mirai-fleet-XXX-XXX.mirai.jar

插件本体，放入mirai-console的plugins文件夹。

- ConfigAndData.zip

解压后得到config、data合并至mirai-console的同名文件夹。

### 3. 配置mirai-console权限

除了常规插件需要允许群员使用指令的权限，还需要一个特殊权限来作为本插件所有服务开关，名为`hundun.fleet.amiya.cos:INSTANCE`。该权限精确到 群号+bot账号 ，也就是说如果在console里运行了多个bot，可以通过权限配置仅让指定的bot，仅在指定的群，启用本插件的服务。

[用PermissionCommand授权群员使用指令的权限](https://github.com/mamoe/mirai-console/blob/master/docs/BuiltInCommands.md#%E6%8E%88%E4%BA%88%E4%B8%80%E4%B8%AA%E7%94%A8%E6%88%B7%E6%89%A7%E8%A1%8C%E6%89%80%E6%9C%89%E6%8C%87%E4%BB%A4%E7%9A%84%E6%9D%83%E9%99%90)

用PermissionCommand授权特殊权限：
> /perm permit m111111.222222 hundun.fleet.amiya.cos:INSTANCE
> 表示bot账号222222在群1111111启用本插件

注意，这里的222222是bot自身的账号。

授权后的config/Console/PermissionService.yml内容示例：
```
grantedPermissionMap: 
  ……
  'hundun.fleet.amiya:*': # 常规权限
    - 'm111111.*' # 允许111111群的任意群员使用本插件的所有指令
  'hundun.fleet.amiya.cos:INSTANCE':  # 特殊权限，表示本插件的开关
    - m111111.222222 # bot账号222222在群1111111启用本插件
```

### 4. 启动和登录

启动mirai-console，在mirai-console里登录。
