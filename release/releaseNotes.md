## v0.2.1

修复：
- 修复会尝试把`data\hundun.fleet.amiya\AmiyaChatFunction\selfNudgeFaces`中的`.gitkeeper`文件作为表情图片发送然后报错的问题。
- 修复样例中的自定义事项提醒关于星期数的配置错误及对应文档错误
 
新特性&ConfigAndData变化：
- 不再随release提供大部分config和data文件夹压缩包。因为插件在启动后，若发现没有对应配置文件，会使用预设的样例值新建文件。故现在仅需要提供图片和音频等data文件。
   <br>对于老用户：各配置文件已存在，无事发生。也可以手动删除某个配置文件，待插件自动新建，得到样例值。
   <br>对于新用户：按说明文档操作即可。